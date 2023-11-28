package GRPC;

import beans.Robot;
import greenfield.ModelRobot;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import proto.Grpc;
import proto.RobotServiceGrpc;

import java.util.concurrent.CountDownLatch;

public class RequestMechanic extends Thread {
    private Robot s;
    private Robot r;

    private static CountDownLatch mechanicLatch;  // Unica istanza condivisa tra tutti i thread

    public RequestMechanic(Robot s, Robot r, CountDownLatch mechanicLatch) {
        this.s = s;
        this.r = r;
        RequestMechanic.mechanicLatch = mechanicLatch;  // Imposta l'istanza condivisa
    }

    public void run() {
        String myRobotId = ModelRobot.getInstance().getCurrentRobot().getID();

        if (myRobotId != null) {
            try {
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(r.getIP() + ":" + r.getPort())
                        .usePlaintext()
                        .build();

                RobotServiceGrpc.RobotServiceBlockingStub stub = RobotServiceGrpc.newBlockingStub(channel);
                Grpc.RequestMechanicRequest request = Grpc.RequestMechanicRequest.newBuilder()
                        .setRobotId(myRobotId)
                        .build();

                try {
                    Grpc.RequestMechanicResponse response = stub.requestMechanic(request);
                    System.out.println("Received response from " + r.getID() + ": " + response.getReply());
                } catch (StatusRuntimeException e) {
                    System.out.println("Error communicating with " + r.getID() + ": " + e.getStatus());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e);
                }

                channel.shutdownNow();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println("ERROR: myRobotId is null.");
        }
    }
}
