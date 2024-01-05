package GRPC;

import beans.Robot;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.Grpc;
import proto.RobotServiceGrpc;

public class BalanceDistrict extends Thread {

    Robot s = new Robot();

    Robot r = new Robot();

    int newDistrict;
    int oldDistrict;

    public BalanceDistrict(){};

    public BalanceDistrict(Robot s, Robot r, int newDistrict, int oldDistrict){
        this.s = s;
        this.r = r;
        this.newDistrict = newDistrict;
        this.oldDistrict = oldDistrict;
    }

    public void run(){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(r.getIP()+ ":" + r.getPort())
                .usePlaintext()
                .build();

        RobotServiceGrpc.RobotServiceBlockingStub stub = RobotServiceGrpc.newBlockingStub(channel);

        Grpc.RobotBalanceRequest request = Grpc.RobotBalanceRequest.newBuilder()
                .setOldDistrict(oldDistrict)
                .setNewDistrict(newDistrict)
                .setStatus(s.getID())
                .build();


        try {

            Grpc.RobotBalanceResponse response = stub.balanceDistrict(request);
            //System.out.println("Response Balance: "+response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }


    }


}
