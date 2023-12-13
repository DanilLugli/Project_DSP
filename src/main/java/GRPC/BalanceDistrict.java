package GRPC;

import beans.Robot;
import greenfield.ModelRobot;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.Grpc;
import proto.RobotServiceGrpc;

public class BalanceDistrict extends Thread {

    Robot s = new Robot();

    Robot r = new Robot();

    int newDistrict;

    public BalanceDistrict(){};

    public BalanceDistrict(Robot s, Robot r, int newDistrict){
        this.s = s;
        this.r = r;
        this.newDistrict = newDistrict;
    }

    public void run(){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(r.getIP()+ ":" + r.getPort())
                .usePlaintext()
                .build();

        RobotServiceGrpc.RobotServiceBlockingStub stub = RobotServiceGrpc.newBlockingStub(channel);


        int oldDistrict = s.getDistrict();
        Grpc.RobotBalanceRequest request = Grpc.RobotBalanceRequest.newBuilder()
                .setOldDistrict(oldDistrict)
                .setNewDistrict(newDistrict)
                .setStatus(s.getID())
                .build();

        //System.out.println(ModelRobot.getInstance().getCurrentRobot().getDistrict());
        ModelRobot.getInstance().getCurrentRobot().setDistrict(newDistrict);
        //System.out.println(ModelRobot.getInstance().getCurrentRobot().getDistrict());
        ModelRobot.getInstance().decrementValue(ModelRobot.getInstance().getDistrictMap(), oldDistrict);
        ModelRobot.getInstance().incrementValue(ModelRobot.getInstance().getDistrictMap(), newDistrict);

        try {

            Grpc.RobotBalanceResponse response = stub.balanceDistrict(request);
            System.out.println(response);
            //System.out.println(response.getReply());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }


    }


}
