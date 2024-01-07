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

            /*System.out.println("\nNow there are: ");
            for (Robot robot : ModelRobot.getInstance().getRobotArrayList()
            ) {
                System.out.println(robot.getID());
            }*/

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

        synchronized (ModelRobot.getInstance().getDistrictMap()){
            ModelRobot.getInstance().decrementValue(ModelRobot.getInstance().getDistrictMap(), request.getOldDistrict());
            ModelRobot.getInstance().incrementValue(ModelRobot.getInstance().getDistrictMap(), request.getNewDistrict());
        }

        Grpc.RobotBalanceResponse response = Grpc.RobotBalanceResponse
                .newBuilder()
                .setReply("OK")
                .build();

        System.out.println("IMPORTANT: " + request.getStatus() + " has changed his district, NEW DISTRICT: " + request.getNewDistrict());


        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void requestMechanic(Grpc.RequestMechanicRequest request, StreamObserver<Grpc.RequestMechanicResponse> responseObserver) {

        long requestTimestamp = request.getTimestamp();
        System.out.println("\nCURRENT: " + ModelRobot.getInstance().getCurrentRobot().getID());
        System.out.println("REQUEST ROBOT: " + request.getRobotId());
        System.out.println("REQUEST TIME: " + requestTimestamp + "\n");


        Grpc.RequestMechanicResponse response;

        if (!ModelRobot.getInstance().getRobotRepairing() && !ModelRobot.getInstance().getRequestMechanic()) {

            response = Grpc.RequestMechanicResponse.newBuilder().setReply("OK").build();

        } else if (ModelRobot.getInstance().getRequestMechanic() && requestTimestamp >= ModelRobot.getInstance().getCurrentRobot().getLamportTimestamp()){

            try {

                synchronized (ModelRobot.getInstance().getMechanicLock()){
                    System.out.println("WAIT "+ request.getRobotId() + ", you have to wait!");
                    ModelRobot.getInstance().getMechanicLock().wait();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            response = Grpc.RequestMechanicResponse.newBuilder().setReply("OK").build();

        } else if (ModelRobot.getInstance().getRobotRepairing()) {

            try {

                synchronized (ModelRobot.getInstance().getMechanicLock()){
                    System.out.println("WAIT "+ request.getRobotId() +", " + ModelRobot.getInstance().getCurrentRobot().getID() + " is to mechanic!");
                    ModelRobot.getInstance().getMechanicLock().wait();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            response = Grpc.RequestMechanicResponse.newBuilder().setReply("OK").build();

        } else {

            response = Grpc.RequestMechanicResponse.newBuilder().setReply("OK").build();

        }


        ModelRobot.getInstance().getCurrentRobot().updateLamportTimestamp(requestTimestamp);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkDelete(Grpc.RequestCheckDelete request, StreamObserver<Grpc.ResponseCheckDelete> responseObserver) {

        boolean robotExist = ModelRobot.getInstance().containsRobotWithId(request.getRobotId());
        Grpc.ResponseCheckDelete response = Grpc.ResponseCheckDelete
                .newBuilder()
                .setAck("OKK")
                .build();

        try
        {
            Thread.sleep(1500);
            if(!robotExist){
                response = Grpc.ResponseCheckDelete
                        .newBuilder()
                        .setAck("OKK")
                        .build();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}


