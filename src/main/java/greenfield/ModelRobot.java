package greenfield;
import beans.Robot;
import beans.RobotModels;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;


@XmlRootElement
public class ModelRobot {

    private Robot robot;
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

    public Robot getRobot() {
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

    public synchronized void requestMechanic() throws InterruptedException {
        while (mechanic) {
            wait();
        }
        mechanic = true;
    }

    public synchronized void releaseMechanic() {
        mechanic = false;
        notifyAll();
    }

}
