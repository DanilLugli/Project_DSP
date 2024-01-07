package greenfield;

import GRPC.NotifyNewRobot;
import GRPC.RemoveRobot;
import GRPC.RobotAlive;
import GRPC.RobotServiceImpl;
import MQTT.MQTTClient;
import beans.CoordRobot;
import beans.Robot;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.*;
import proto.Grpc;
import proto.RobotServiceGrpc;
import simulators.BufferImpl;
import simulators.Measurement;
import simulators.PM10Simulator;
import utils.My_CountdownLatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class RobotProcess {
    static Robot robot;
    static String robotId;
    static int robotPort;
    static String robotIp;
    static ArrayList<Measurement> measurementList = new ArrayList<>();
    static Server server;
    static CoordRobot coordRobot;
    static MQTTClient mqttPublisher = new MQTTClient("tcp://localhost:1883");



    public static void main(String[] args) throws IOException {
        robotId = "Robot" + (int) Math.floor(Math.random() * (50 - 1 + 1) + 1);
        robotPort = (int) Math.floor(Math.random() * (9021 - 8080)) + 8080;
        robotIp = "localhost";

        //Create, add robot to listRobots, start pollution and manage robots
        createStartRobot();

        //Manage Robot stuff
        manageRobotExit();

    }

    public static void createStartRobot() {

        System.out.print("Write 'exit' if you want to quit Greenfield... \n");

        try {

            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:1993/Robot/addRobot");
            String input = "{\n" +
                    "  \"id\": \"" + robotId + "\",\n" +
                    "  \"ip\": \"" + robotIp + "\",\n" +
                    "  \"port\": \"" + robotPort + "\"}";

            Date date = new Date();
            long lamportTimestamp = date.getTime();

            robot = new Robot(robotId, robotIp, robotPort, lamportTimestamp);

            ClientResponse result = webResource.type("application/json").put(ClientResponse.class, robot);

            if (result.getStatus() == 200) {

                coordRobot = result.getEntity(CoordRobot.class);

                robot.setDistrict(coordRobot.getDistrict());

                System.out.println("<-- Hi, I'm "+ robotId + " -->");
                System.out.println("From: " + coordRobot.getDistrict());
                //System.out.println("Robot: " + robot.hashCode());
                System.out.println("Lamport: " + robot.getLamportTimestamp());

                ModelRobot modelRobot = ModelRobot.getInstance();
                modelRobot.setRobot(robot);
                modelRobot.setRobotArrayList(coordRobot.getRobotsList());
                modelRobot.incrementValue(modelRobot.getDistrictMap(), robot.getDistrict());

                //System.out.println("\nFOR --> " + modelRobot.getCurrentRobot().getID() + " the instance of Model Robot is : " + modelRobot + "\n");


                if (modelRobot.getRobotArrayList().isEmpty()) {
                    System.out.println("List of Robot is empty.");
                } else {
                    ArrayList<Robot> l = modelRobot.getRobotArrayList();
                    System.out.println("\n->There are " + l.size() + " alive robot:");
                    for (Robot robot : l
                    ) {
                        if(robot.getID().equals(robotId)){
                            System.out.println(robot.getID() + " (ME)");
                        }
                        else System.out.println(robot.getID());
                    }
                }
            } else {
                System.out.println("Error: " + result.getStatus());
            }

            //Start Take Data Pollution - THREAD
            sensorTake();

            //GRPC -> Start Server
            notifyNew();

            //MQTT -> Send Pollution to topic - THREAD
            sensorSend();

            //GRPC Alive - THREAD
            checkRobotAlive();

            //Request Mechanic - THREAD
            requestMechanic();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void notifyNew() throws IOException {
        try {

            int port = robot.getPort();

            server = ServerBuilder.forPort(port)
                    .addService(new RobotServiceImpl())
                    .build();

            server.start();

            for (Robot r : coordRobot.getRobotsList()) {
                if (coordRobot.getRobotsList().size() > 1 && !r.getID().equals(robot.getID())) {
                    if (robot != null) {
                        NotifyNewRobot n = new NotifyNewRobot(robot, r);
                        n.start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sensorTake() {
        System.out.println("Start taking PM_10 by sensor...");
        BufferImpl pollutionImpl = new BufferImpl();                     //Create pollution Buff

        PM10Simulator pm10Simulator = new PM10Simulator(pollutionImpl);  //Create a Simulator for sensor
        pm10Simulator.start();                                           //Start sensor


        Thread sensorThread = new Thread(() -> {
            while (true) {
                ArrayList<Measurement> measurements = (ArrayList<Measurement>) pollutionImpl.readAllAndClean();

                double avg = 0;
                long time = 0;

                for (Measurement m : measurements) {
                    avg += m.getValue();
                    time = m.getTimestamp();
                }

                Measurement m = new Measurement("PM-10", "PM-10", avg / 8, time);
                measurementList.add(m);

            }
        });

        // Imposta il thread come daemon thread
        sensorThread.setDaemon(true);

        // Avvia il thread
        sensorThread.start();
    }

    static Thread sendThread = null;

    public static void sensorSend() {

        String topic = "greenfield/pollution/district/" + robot.getDistrict();

        if (sendThread != null && sendThread.isAlive()) {
            sendThread.interrupt();
        }

         sendThread = new Thread(() -> {
            while (ModelRobot.isRunning()) {

                synchronized (ModelRobot.getInstance().getMechanicLock()){
                    try{
                        ModelRobot.getInstance().getMechanicLock().wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                if (!measurementList.isEmpty()) {
                    Measurement measurement = measurementList.remove(0);
                    String payload = robotId + "," + (measurement.getValue()) + "," + (measurement.getTimestamp());
                    mqttPublisher.publishToTopic(topic, payload);
                    System.out.println("Publish MQTT district " + robot.getDistrict()+ " : "+ payload.split(",")[1]);
                }

                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    break;
                }
            }
        });

        sendThread.start();
    }

    public static void stopSensorSend() {
        if (sendThread != null && sendThread.isAlive()) {
            sendThread.interrupt();
        }
    }

    private static void manageRobotExit() {
        ArrayList<Robot> robotArrayList = ModelRobot.getInstance().getRobotArrayList();
        new Thread(() -> {
            try {
                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.next();

                    if (input.equals("exit")) {

                        synchronized (ModelRobot.getInstance().getMechanicLock()){
                            while (ModelRobot.getInstance().getRobotRepairing() || ModelRobot.getInstance().getRequestMechanic()){
                                try{
                                    System.out.println("I'm waiting mechanic, wait!");
                                    ModelRobot.getInstance().getMechanicLock().wait();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }

                        try{
                            mqttPublisher.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        for (Robot r : robotArrayList) {
                            if (robotArrayList.size() >= 1 && !r.getID().equals(robotId)) {
                                RemoveRobot removeRobot = new RemoveRobot(robot, r);
                                System.out.println("Calling " + r.getID() + "at address: " + r.getIP() + ":" + r.getPort());
                                removeRobot.start();
                            }
                        }

                        try {
                            if (server != null) {
                                server.shutdownNow();
                                System.out.println("GRPC Server stopped.");
                            } else {
                                System.out.println("Server is already null.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error GRPC stop: " + e);
                        }

                        String robotId = "";

                        //Delete robot from general list of robots and decrement district
                        if (ModelRobot.getInstance().getCurrentRobot().getID() != null)
                            robotId = ModelRobot.getInstance().getCurrentRobot().getID();
                        else System.out.println("null RobotID");
                        try {
                            Client client = Client.create();

                            WebResource webResource = client.resource("http://localhost:1993/Robot/delete/" + robotId);

                            ClientResponse result = webResource.type("application/json").delete(ClientResponse.class);

                            if (result.getStatus() == 200) {
                                System.out.println("Delete from network: OK!");
                                System.exit(0);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (input.equals("bal")) {

                        System.out.println("DIS (AFTER CALL): ");
                        for (int n: ModelRobot.getInstance().getDistrictMap().values()
                        ) {
                            System.out.println(n);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void checkRobotAlive() {
        Thread grpcRequestThread = new Thread(() -> {
            while (true) {

                ArrayList<Robot> robotArrayList = ModelRobot.getInstance().getRobotArrayList();

                for (Robot r : robotArrayList) {
                    if (!r.getID().equals(robot.getID())) {
                        try {

                            RobotAlive robotAlive = new RobotAlive(r, 3000);
                            robotAlive.start();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Thread.sleep(7000); // Wait 10 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        grpcRequestThread.setDaemon(true);

        grpcRequestThread.start();
    }

    private static void requestMechanic(){

        Thread mechanicThread = new Thread(() -> {
            try {
                while (true) {

                    ArrayList<Robot> iterList = new ArrayList<>(ModelRobot.getInstance().getRobotArrayList());

                    if (Math.random() <= 0.4) { //10% of chance

                        My_CountdownLatch latch;
                        ModelRobot.getInstance().setRequestMechanic(true);
                        latch = new My_CountdownLatch(iterList.size() -1);

                        //LAMPORT - SEND MESSAGE
                        ModelRobot.getInstance().getCurrentRobot().incrementLamportTimestamp();
                        System.out.println("\nMaking MECHANIC Request...");
                        System.out.println("TIME: " + ModelRobot.getInstance().getCurrentRobot().getLamportTimestamp());
                        for (Robot r : iterList) {
                            if (!r.getID().equals(ModelRobot.getInstance().getCurrentRobot().getID())) {
                                try {

                                    final ManagedChannel channel = ManagedChannelBuilder.forTarget(r.getIP() + ":" + r.getPort())
                                            .usePlaintext()
                                            .build();

                                    RobotServiceGrpc.RobotServiceBlockingStub stub = RobotServiceGrpc.newBlockingStub(channel);
                                    Grpc.RequestMechanicRequest request = Grpc.RequestMechanicRequest.newBuilder()
                                            .setRobotId(ModelRobot.getInstance().getCurrentRobot().getID())
                                            .setTimestamp(ModelRobot.getInstance().getCurrentRobot().getLamportTimestamp())
                                            .build();

                                    Grpc.RequestMechanicResponse response;

                                    try {

                                        response = stub.requestMechanic(request);
                                        if(response.getReply().equals("OK"))
                                            latch.countDown();
                                        System.out.println("Response: " + r.getID() + ": " + response.getReply());

                                    } catch (StatusRuntimeException e) {
                                        e.printStackTrace();
                                    }

                                    channel.shutdownNow();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        latch.await();

                        ModelRobot.getInstance().setRequestMechanic(false);

                        System.out.println("\n--> RECEIVING ASSISTANCE FROM MECHANIC...");
                        ModelRobot.getInstance().setRobotRepairing(true);

                        Thread.sleep(20000);

                        ModelRobot.getInstance().setRobotRepairing(false);
                        System.out.println("...ASSISTANCE FINISHED! <--\n");

                        ModelRobot.getInstance().getCurrentRobot().updateLamportTimestamp(ModelRobot.getInstance().getCurrentRobot().getLamportTimestamp());

                        synchronized (ModelRobot.getInstance().getMechanicLock()){
                            ModelRobot.getInstance().getMechanicLock().notifyAll();
                        }
                    }
                    Thread.sleep(10000); //Every 10 seconds could be the chance
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mechanicThread.start();
    }

}


