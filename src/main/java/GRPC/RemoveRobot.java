package GRPC;

import beans.Robot;
import greenfield.ModelRobot;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.Grpc;
import proto.RobotServiceGrpc;

public class RemoveRobot extends Thread {
    private Robot s;
    private Robot r;

    public RemoveRobot(Robot s, Robot r) {
        this.s = s;
        this.r = r;
    }

    public void run() {

        String myRobotId = ModelRobot.getInstance().getCurrentRobot().getID();

        if (myRobotId != null) {
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(r.getIP() + ":" + r.getPort())
                    .usePlaintext()
                    .build();

            RobotServiceGrpc.RobotServiceBlockingStub stub = RobotServiceGrpc.newBlockingStub(channel);

            Grpc.RemoveRobotRequest request = Grpc.RemoveRobotRequest.newBuilder().setRobotId(myRobotId).build();
            Grpc.RemoveRobotResponse response;

            try {
                response = stub.removeRobot(request);
                System.out.println("I've just send to :" + r.getID() + " my elimination.");
            } catch (Exception e) {
                System.out.println("There's no communication with: " + r.getID());
            }

            channel.shutdownNow();


        } else {
            System.out.println("ERROR: myRobotId è null.");
        }
    }
}
