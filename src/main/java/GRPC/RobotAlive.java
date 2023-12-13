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


public class RobotAlive extends Thread {

    public boolean check = false;
    private final Robot r;
    private final int timeoutMs;
    private final boolean responseReceived = false;

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

        } finally {
            channel.shutdown();
        }
    }

    private void handleTimeout() {
        if (!responseReceived) {

            System.out.println("ATTENTION --> Timeout: No answer from " + r.getID() + " within " + timeoutMs + " ms");

            removeRobot(r.getID(), r.getDistrict());

            if (!checkBalance()){

                    synchronized (ModelRobot.getInstance().getDistrictMap()) {
                        if(ModelRobot.getInstance().getCurrentRobot().getDistrict() == getMaxDistrict()){
                            System.out.println("My District..");

                            for (Robot r : ModelRobot.getInstance().getRobotArrayList()) {

                                if (!r.getID().equals(ModelRobot.getInstance().getCurrentRobot().getID())) {
                                    BalanceDistrict balanceDistrict = new BalanceDistrict(ModelRobot.getInstance().getCurrentRobot(), r, getMinDistrict() );
                                    balanceDistrict.start();

                                }
                            }
                            System.out.println("Me " + ModelRobot.getInstance().getCurrentRobot().getID() + " CHANGE district to balance Greenfield!");
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
        ModelRobot.getInstance().decrementValue(ModelRobot.getInstance().getDistrictMap(), district);

        /*System.out.println("SITUA DIS (AFTER CRASH): ");
        for (int n: ModelRobot.getInstance().getDistrictMap().values()
        ) {
            System.out.println(n);
        }*/
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
}
