package GRPC;

import beans.Robot;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.Grpc;
import proto.RobotServiceGrpc;

public class BalanceDistrict extends Thread {

    Robot r = new Robot();

    public BalanceDistrict(){};

    public BalanceDistrict(Robot r){
        this.r = r;
    }

    public void run(){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(r.getIP()+ ":" + r.getPort())
                .usePlaintext()
                .build();

        RobotServiceGrpc.RobotServiceBlockingStub stub = RobotServiceGrpc.newBlockingStub(channel);

        Grpc.RobotInfo request = Grpc.RobotInfo.newBuilder()
                .build();
        try {

            Grpc.RobotResponse response = stub.notifyNewRobot(request);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }


    }


}
