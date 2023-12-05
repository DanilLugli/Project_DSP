package GRPC;

import beans.Robot;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import greenfield.ModelRobot;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import proto.Grpc;
import proto.RobotServiceGrpc;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class RobotAlive extends Thread {

    public boolean check = false;
    private final Robot r;
    private final int timeoutMs;
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private volatile boolean responseReceived = false;

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
            Grpc.RobotAliveResponse response = stub.withDeadlineAfter(timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS)
                    .robotAlive(request);
        } catch (StatusRuntimeException e) {

            handleTimeout();
            check = true;

            //TODO: CALL HERE BALANCE

        } finally {
            channel.shutdown();
        }
    }

    private void handleTimeout() {
        if (!responseReceived) {

            System.out.println("ATTENTION --> Timeout: No answer from " + r.getID() + " within " + timeoutMs + " ms");

            removeRobot(r.getID(), r.getDistrict());

            if(!checkBalance()){
                while(!checkBalance()){
                    if (ModelRobot.getInstance().getDistrictMap().get(getMaxDistrict()) > 0) {
                        synchronized (ModelRobot.getInstance().getDistrictMap()) {
                            if(SE QUESTO E' IL MIO DISTRETTO MI SPOSTO IO) else (SPOSTO UN ALTRO)
                            ModelRobot.getInstance().getDistrictMap().put(getMaxDistrict(), ModelRobot.getInstance().getDistrictMap().get(getMaxDistrict()) - 1);
                            ModelRobot.getInstance().getDistrictMap().put(getMinDistrict(), ModelRobot.getInstance().getDistrictMap().get(getMinDistrict()) + 1);
                        }
                        System.out.println("Robot spostato dal Distretto " + fromDistrict + " al Distretto " + toDistrict);
                    }

                }

            }

            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:1993/Robot/delete/" + r.getID());
            ClientResponse result = webResource.type("application/json").delete(ClientResponse.class);

            if (result.getStatus() == 200) {
                System.out.println("Delete from network after Crash by: " + r.getID() + " OK");
            }
        }
    }

    private void removeRobot(String robotId, int district) {

        ModelRobot.getInstance().removeRobotById(robotId);
        ModelRobot.getInstance().decrementValue(ModelRobot.getInstance().districtMap, district);

    }

    private boolean checkBalance(){

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

    // Funzione per ottenere il distretto con il minimo numero di robot
    private int getMinDistrict() {
        return ModelRobot.getInstance().getDistrictMap().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }


}
