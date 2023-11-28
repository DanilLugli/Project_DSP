package GRPC;

import beans.Position;
import beans.Robot;
import greenfield.ModelRobot;
import io.grpc.stub.StreamObserver;
import proto.Grpc;
import proto.RobotServiceGrpc;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class RobotServiceImpl extends RobotServiceGrpc.RobotServiceImplBase {

    private final CountDownLatch mechanicLatch = new CountDownLatch(1);

    @Override
    public void notifyNewRobot(Grpc.RobotInfo request, StreamObserver<Grpc.Empty> responseObserver) {

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

                robotArrayList.add(newRobot);
                System.out.println("\n--> NEW Robot in greenfield: " + newRobot.getID() + ", added in my list.");
                //System.out.println("Now there are these robot active in Greenfield: ");
            } else {
                System.out.println("Robot ArrayList is null.");
            }
        } else {
            System.out.println("ModelRobot is null.");
        }
        responseObserver.onNext(Grpc.Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void removeRobot(Grpc.RemoveRobotRequest request, StreamObserver<Grpc.RemoveRobotResponse> responseObserver) {
        String robotId = request.getRobotId();
        if (ModelRobot.getInstance() != null) {
            ModelRobot.getInstance().removeRobotById(robotId);
            System.out.println("I've just deleted: " + robotId);
            System.out.println("\nNow there are: ");
            for (Robot robot : ModelRobot.getInstance().getRobotArrayList()
            ) {
                System.out.println(robot.getID());
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
    public void balanceDistrict(Grpc.RobotBalanceRequest request, StreamObserver<Grpc.Empty> responseObserver) {
    }

    @Override
    public void requestMechanic(Grpc.RequestMechanicRequest request, StreamObserver<Grpc.RequestMechanicResponse> responseObserver) {
        try {
            String robotIDRequest = request.getRobotId();

            // Aggiungi il thread corrente alla coda
            synchronized (this) {
                ModelRobot.getInstance().getMechanicQueue().offer(robotIDRequest);
            }

            String currentId;
            synchronized (this) {
                currentId = ModelRobot.getInstance().getMechanicQueue().poll();
            }

            if (currentId.equals(robotIDRequest) && !ModelRobot.getInstance().getMechanic()) {

                ModelRobot.getInstance().requestMechanic(currentId.toString());
                System.out.println("\nRobot " + currentId + " sta ricevendo assistenza meccanica...");
                Thread.sleep(250000);
                System.out.println("Robot " + currentId + " ha ricevuto assistenza meccanica.");
                ModelRobot.getInstance().releaseMechanic(currentId.toString());

                mechanicLatch.countDown();

                Grpc.RequestMechanicResponse response = Grpc.RequestMechanicResponse.newBuilder()
                        .setReply("OK")
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                System.out.println("\nRobot " + robotIDRequest + " è in attesa del suo turno per l'assistenza meccanica...");
                responseObserver.onNext(Grpc.RequestMechanicResponse.newBuilder().setReply("In attesa del tuo turno...").build());
                responseObserver.onCompleted();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

