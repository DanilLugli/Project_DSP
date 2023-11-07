package GRPC;

import beans.Robot;
import beans.RobotModels;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import greenfield.ModelRobot;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import proto.Grpc;
import proto.RobotServiceGrpc;

import java.util.Arrays;
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
                .setAck("Alive")
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

            System.out.println("ATTENTION: Timeout: No answer from " + r.getID() + " within " + timeoutMs + " ms");

            removeRobot(r.getID());
            checkBalance();

            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:1993/Robot/delete/" + r.getID());
            ClientResponse result = webResource.type("application/json").delete(ClientResponse.class);

            if (result.getStatus() == 200) {
                System.out.println("Delete from network after Crash by: " + r.getID() + " OK");
            }
        }
    }

    private void removeRobot(String robotId) {
        ModelRobot.getInstance().removeRobotById(robotId);
    }

    private void checkBalance(){

    }


}
