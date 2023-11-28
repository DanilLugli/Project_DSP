package greenfield;

import beans.Robot;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@XmlRootElement
public class ModelRobot {

    public BlockingQueue<String> getMechanicQueue() {
        return mechanicQueue;
    }

    private final BlockingQueue<String> mechanicQueue = new LinkedBlockingQueue<>();

    private Robot robot;

    public boolean getMechanic() {
        return mechanic;
    }

    public void setMechanic(boolean mechanic) {
        this.mechanic = mechanic;
    }

    private boolean mechanic = false;


    private ArrayList<Robot> robotArrayList;
    private static ModelRobot instance;

    public static ModelRobot getInstance() {
        if (instance == null) {
            instance = new ModelRobot();
        }
        return instance;
    }


    public ModelRobot() {
        this.robot = null;
        this.robotArrayList = new ArrayList<>();
    }

    public ModelRobot(Robot robot, ArrayList<Robot> robotArrayList) {
        this.robot = robot;
        this.robotArrayList = new ArrayList<>();
    }

    public Robot getCurrentRobot() {
        return this.robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public synchronized ArrayList<Robot> getRobotArrayList() {
        return this.robotArrayList;
    }

    public void setRobotArrayList(ArrayList<Robot> robotArrayList) {
        this.robotArrayList = robotArrayList;
    }

    public void removeRobotById(String robotId) {
        synchronized (this.robotArrayList) {
            boolean removed = robotArrayList.removeIf(r -> r.getID().equals(robotId));
            System.out.println(removed ? "OK DELETED" : "ERROR: Robot not found!");
        }
    }



    public synchronized void requestMechanic(String robotID){
        this.mechanic = true;
        //System.out.println("Mechanic is TRUE: " + robotID );
    }

    public synchronized void releaseMechanic(String robotID) {
        this.mechanic = false;
        //System.out.println("Mechanic is FALSE: " + robotID);
    }

}
