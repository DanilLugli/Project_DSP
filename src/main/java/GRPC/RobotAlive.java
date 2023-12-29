package GRPC;

import beans.Robot;
import beans.RobotModels;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import greenfield.ModelRobot;
import greenfield.RobotProcess;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import proto.Grpc;
import proto.RobotServiceGrpc;

import java.util.ArrayList;
import java.util.Map;


public class RobotAlive extends Thread {

    public boolean check = false;
    private final Robot r;
    private final int timeoutMs;
    private final boolean responseReceived = false;
    private int oldDistrict;

    private int c_count = 0;


    public RobotAlive(Robot r, int timeoutMs) {
        this.r = r;
        this.timeoutMs = timeoutMs;
    }

    public void run() {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(r.getIP() + ":" + r.getPort())
                .usePlaintext()
                .build();

        RobotServiceGrpc.RobotServiceBlockingStub stub = RobotServiceGrpc.newBlockingStub(channel);

        Grpc.RobotAliveRequest request = Grpc.RobotAliveRequest.newBuilder()
                .setAck("A")
                .build();

        try {
            Grpc.RobotAliveResponse response =
                    stub
                            .withDeadlineAfter(timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS)
                            .robotAlive(request);
        } catch (StatusRuntimeException e) {

            handleTimeout();
            check = true;

        } finally {
            channel.shutdown();
        }
    }

    private void handleTimeout() {
        if (!responseReceived) {

            System.out.println("ATTENTION I'm "
                    + ModelRobot.getInstance().getCurrentRobot().getID()
                    +" --> TIMEOUT: No answer from "
                    + r.getID()
                    + " within "
                    + timeoutMs
                    + " ms");


            System.out.println("\nSIZE PRE: " + ModelRobot.getInstance().getRobotArrayList().size());

            removeRobot(r.getID(), r.getDistrict());

            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:1993/Robot/delete/" + r.getID());
            ClientResponse result = webResource.type("application/json").delete(ClientResponse.class);

            if (result.getStatus() == 200) {
                System.out.println("Delete from network after Crash by: " + r.getID() + " OK");
            }

            System.out.println("SIZE POST: " + ModelRobot.getInstance().getRobotArrayList().size() + "\n");

            ArrayList<Robot> iterList = new ArrayList<>(ModelRobot.getInstance().getRobotArrayList());
            for (Robot r: iterList
                 ) {
                if (!r.getID().equals(ModelRobot.getInstance().getCurrentRobot().getID())) {
                    try {

                        final ManagedChannel channel = ManagedChannelBuilder.forTarget(r.getIP() + ":" + r.getPort())
                                .usePlaintext()
                                .build();

                        RobotServiceGrpc.RobotServiceBlockingStub stub = RobotServiceGrpc.newBlockingStub(channel);
                        Grpc.RequestCheckDelete request = Grpc.RequestCheckDelete.newBuilder()
                                .setRobotId(ModelRobot.getInstance().getCurrentRobot().getID())
                                .build();

                        Grpc.ResponseCheckDelete response;

                        try {

                            response = stub.checkDelete(request);
                            if(response.getAck().equals("OKK"))
                                c_count ++;

                        } catch (StatusRuntimeException e) {
                            e.printStackTrace();
                        }

                        channel.shutdownNow();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if(c_count == iterList.size() -1){
                System.out.println("<-- START BALANCE DISTRICT PROCESS -->");

                while (!checkBalance()){
                    synchronized (ModelRobot.getInstance().getBalanceLock()) {
                        ArrayList<Robot> rList = new ArrayList<>(ModelRobot.getInstance().getRobotArrayList());
                        if(ModelRobot.getInstance().getCurrentRobot().getDistrict() == getMaxDistrict()){  //if MY district
                            if(highID(ModelRobot.getInstance().getCurrentRobot())){      //if I'M highest

                                System.out.println(ModelRobot.getInstance().getCurrentRobot().getDistrict() + " OLD.");

                                synchronized (ModelRobot.getInstance().getDistrictMap()){

                                    ModelRobot.getInstance().decrementValue(ModelRobot.getInstance().getDistrictMap(), ModelRobot.getInstance().getCurrentRobot().getDistrict());
                                    oldDistrict = ModelRobot.getInstance().getCurrentRobot().getDistrict();
                                    ModelRobot.getInstance().getCurrentRobot().setDistrict(getMinDistrict());
                                    System.out.println(ModelRobot.getInstance().getCurrentRobot().getID() + " is in NEW DISTRICT: " + getMinDistrict() );
                                    ModelRobot.getInstance().incrementValue(ModelRobot.getInstance().getDistrictMap(), getMinDistrict());
                                    ModelRobot.getInstance().getCurrentRobot().setPos(RobotModels.updatePos(getMinDistrict()));

                                }

                                System.out.println(ModelRobot.getInstance().getCurrentRobot().getDistrict() + " NEW.\n");

                                for (Robot r : rList) {
                                    if (!r.getID().equals(ModelRobot.getInstance().getCurrentRobot().getID())) {

                                        BalanceDistrict balanceDistrict = new BalanceDistrict(ModelRobot.getInstance().getCurrentRobot(), r, ModelRobot.getInstance().getCurrentRobot().getDistrict(), oldDistrict);
                                        balanceDistrict.start();

                                    }
                                }

                                System.out.println("Me " + ModelRobot.getInstance().getCurrentRobot().getID() + " CHANGE district to balance Greenfield!");

                                RobotProcess.stopSensorSend();
                                ModelRobot.setRunning(true);
                                RobotProcess.sensorSend();

                                Client client2 = Client.create();
                                WebResource webResource2 = client2.resource("http://localhost:1993/Robot/update/" + ModelRobot.getInstance().getCurrentRobot().getID() + "/" + ModelRobot.getInstance().getCurrentRobot().getDistrict());
                                ClientResponse result2 = webResource2.type("application/json").put(ClientResponse.class);

                                System.out.println("AAA " + result2);

                                synchronized (ModelRobot.getInstance().getBalanceLock()) {
                                    try{
                                        Thread.sleep(1000);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    ModelRobot.getInstance().getBalanceLock().notifyAll();
                                }

                            }else{
                                try {
                                    ModelRobot.getInstance().getBalanceLock().wait();
                                    System.out.println("UNLOCKED - OK !");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();

                                }
                            }
                        }else{

                            try {
                                ModelRobot.getInstance().getBalanceLock().wait();
                                System.out.println("UNLOCKED - OK !");
                            } catch (InterruptedException e) {
                                e.printStackTrace();

                            }
                        }


                    }
                }

            }
            System.out.println("<- GREENFIELD ALREADY BALANCE ->");
        }
    }

    private void removeRobot(String robotId, int district) {

        ModelRobot.getInstance().removeRobotById(robotId);
        synchronized (ModelRobot.getInstance().getDistrictMap()){
            ModelRobot.getInstance().decrementValue(ModelRobot.getInstance().getDistrictMap(), district);
        }
    }

    private synchronized boolean checkBalance(){

        int maxRobots = getMaxRobots();
        int minRobots = getMinRobots();

        return (maxRobots - minRobots) <= 1;
    }

    private int getMaxRobots() {
        return ModelRobot.getInstance().getDistrictMap().values().stream().max(Integer::compare).orElse(0);
    }

    private int getMinRobots() {
        return ModelRobot.getInstance().getDistrictMap().values().stream().min(Integer::compare).orElse(0);
    }

    private int getMaxDistrict() {
        return ModelRobot.getInstance().getDistrictMap().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    private int getMinDistrict() {
        return ModelRobot.getInstance().getDistrictMap().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    private boolean highID(Robot robot) {
        for (Robot r : ModelRobot.getInstance().getRobotArrayList()) {
            if(r.getDistrict() == robot.getDistrict()){
                if (r.getID().compareTo(robot.getID()) > 0){
                    return false;
                }
            }
        }
        return true;
    }
}
