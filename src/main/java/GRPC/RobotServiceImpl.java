package GRPC;

import beans.Position;
import beans.Robot;
import greenfield.ModelRobot;
import io.grpc.stub.StreamObserver;
import proto.Grpc;
import proto.RobotServiceGrpc;

import java.util.ArrayList;

public class RobotServiceImpl extends RobotServiceGrpc.RobotServiceImplBase {


    @Override
    public void notifyNewRobot(Grpc.RobotInfo request, StreamObserver<Grpc.RobotResponse> responseObserver) {

        Robot newRobot = new Robot();

        newRobot.setIp(request.getRobotIp());
        newRobot.setId(request.getRobotId());
        newRobot.setPort(request.getRobotPort());
        Position p = new Position(request.getX(), request.getY());
        newRobot.setPos(p);
        newRobot.setDistrict(request.getDistrict());

        if (ModelRobot.getInstance().getRobotArrayList() != null) {
            ArrayList<Robot> robotArrayList = ModelRobot.getInstance().getRobotArrayList();
            if (robotArrayList != null) {
                ModelRobot.getInstance().incrementValue(ModelRobot.getInstance().getDistrictMap(), request.getDistrict());
                robotArrayList.add(newRobot);
                System.out.println("\n--> NEW Robot in greenfield: " + newRobot.getID() + ", added in my list.");

            } else {
                System.out.println("Robot ArrayList is null.");
            }
        } else {
            System.out.println("ModelRobot is null.");
        }

        Grpc.RobotResponse response = Grpc.RobotResponse
                .newBuilder()
                .setDistrict(ModelRobot.getInstance().getCurrentRobot().getDistrict())
                .build();


        /*System.out.println("SITUA DIS: ");
        for (int n: ModelRobot.getInstance().getDistrictMap().values()
        ) {
            System.out.println(n);
        }*/

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void removeRobot(Grpc.RemoveRobotRequest request, StreamObserver<Grpc.RemoveRobotResponse> responseObserver) {

        String robotId = request.getRobotId();

        if (ModelRobot.getInstance() != null) {

            ModelRobot.getInstance().removeRobotById(robotId);
            ModelRobot.getInstance().decrementValue(ModelRobot.getInstance().getDistrictMap(), request.getDistrict());

            System.out.println("I've just deleted: " + robotId);
            System.out.println("\nNow there are: ");

            for (Robot robot : ModelRobot.getInstance().getRobotArrayList()
            ) {
                System.out.println(robot.getID());
            }

            System.out.println("SITUA DIS (AFTER REMOVE): ");
            for (int n: ModelRobot.getInstance().getDistrictMap().values()
            ) {
                System.out.println(n);
            }


        } else {
            System.out.println("ModelRobot instance null");
        }

        Grpc.RemoveRobotResponse response = Grpc.RemoveRobotResponse
                .newBuilder()
                .setReply("OK")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void robotAlive(Grpc.RobotAliveRequest request, StreamObserver<Grpc.RobotAliveResponse> responseObserver) {

        //String ack = request.getAck();
        Grpc.RobotAliveResponse response = Grpc.RobotAliveResponse.newBuilder()
                .setMsg("OK!")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void balanceDistrict(Grpc.RobotBalanceRequest request, StreamObserver<Grpc.RobotBalanceResponse> responseObserver) {

        ModelRobot.getInstance().decrementValue(ModelRobot.getInstance().getDistrictMap(), request.getOldDistrict());
        ModelRobot.getInstance().incrementValue(ModelRobot.getInstance().getDistrictMap(), request.getNewDistrict());

        System.out.println("I've updated district !\n");
        Grpc.RobotBalanceResponse response = Grpc.RobotBalanceResponse
                .newBuilder()
                .setReply("OK")
                .build();

        /*System.out.println("SITUA DIS (AFTER BALANCE E CRASH): ");
        for (int n: ModelRobot.getInstance().getDistrictMap().values()
        ) {
            System.out.println(n);
        }*/

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void requestMechanic(Grpc.RequestMechanicRequest request, StreamObserver<Grpc.RequestMechanicResponse> responseObserver) {
        /*
        System.out.println("CURRENT: " + ModelRobot.getInstance().getCurrentRobot().getID());
        System.out.println("REQUEST: " + request.getRobotId());
        System.out.println("INTERESTED: " + ModelRobot.getInstance().getRequestMechanic());
         */


        long requestTimestamp = request.getTimestamp();


        Grpc.RequestMechanicResponse response = Grpc.RequestMechanicResponse.newBuilder().setReply("OK").build();

        if (!ModelRobot.getInstance().getRobotRepairing() && !ModelRobot.getInstance().getRequestMechanic()) {

            response = Grpc.RequestMechanicResponse.newBuilder().setReply("OK").build();

        } else if (ModelRobot.getInstance().getRequestMechanic() && requestTimestamp > ModelRobot.getInstance().getCurrentRobot().getLamportTimestamp()){

            try {
                synchronized (ModelRobot.getInstance().getMechanicLock()){
                    System.out.println("Attendi "+ request.getRobotId() + ", sei in wait()!");
                    ModelRobot.getInstance().getMechanicLock().wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            response = Grpc.RequestMechanicResponse.newBuilder().setReply("OK").build();

        } else if (ModelRobot.getInstance().getRobotRepairing()) {

            try {
                synchronized (ModelRobot.getInstance().getMechanicLock()){
                    System.out.println("Attendi "+ request.getRobotId() +", Io " + ModelRobot.getInstance().getCurrentRobot().getID() + " la sto usando!");
                    ModelRobot.getInstance().getMechanicLock().wait();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            response = Grpc.RequestMechanicResponse.newBuilder().setReply("OK").build();

        } else {

            response = Grpc.RequestMechanicResponse.newBuilder().setReply("OK").build();

        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}


