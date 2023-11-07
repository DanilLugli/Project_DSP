package GRPC;

import beans.Robot;
import greenfield.ModelRobot;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.Grpc;
import proto.RobotServiceGrpc;

public class NotifyNewRobot extends Thread{

    Robot s = new Robot();
    Robot r = new Robot();


    public NotifyNewRobot(Robot s, Robot r){
        this.s = s;
        this.r = r;
    }

    public NotifyNewRobot(){};


    public void run(){

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(r.getIP()+ ":" + r.getPort())
                .usePlaintext()
                .build();

        RobotServiceGrpc.RobotServiceBlockingStub stub = RobotServiceGrpc.newBlockingStub(channel);

        Grpc.RobotInfo request = Grpc.RobotInfo.newBuilder()
                .setRobotId(s.getID())
                .setRobotIp(s.getIP())
                .setRobotPort(s.getPort())
                .setDistrict(s.getDistrict())
                .build();
        try {
            Grpc.Empty response = stub.notifyNewRobot(request);
            //for (Robot robot : ModelRobot.getInstance().getRobotArrayList()) {
                //if (!s.getID().equals(robot.getID())) {
                    //System.out.println("Robot " + s.getID() + " has contacted " + robot.getID());
                //}
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }

}

}
