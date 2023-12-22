package greenfield;

import beans.Robot;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@XmlRootElement
public class ModelRobot {

    private Robot robot;
    private ArrayList<Robot> robotArrayList;
    public Map<Integer, Integer> districtMap = new HashMap<Integer, Integer>(){{
        put(0, 0);
        put(1, 0);
        put(2, 0);
        put(3, 0);
    }};

    public static ModelRobot getInstance() {
        if (instance == null) {
            instance = new ModelRobot();
        }
        return instance;
    }

    // ...
    private boolean robotRepairing;

    private boolean requestMechanic;

    public boolean getRequestMechanic() {
        return requestMechanic;
    }

    public void setRequestMechanic(boolean requestMechanic) {
        this.requestMechanic = requestMechanic;
    }

    public boolean getRobotRepairing() {
        return robotRepairing;
    }

    public void setRobotRepairing(boolean robotRepairing) {
        this.robotRepairing = robotRepairing;
    }

    public synchronized void incrementValue(Map<Integer, Integer> map, int key) {

        Integer value = map.get(key);

        if (value != null) {
            synchronized (this.districtMap){
                map.put(key, value + 1);
            }
        } else {
            synchronized (this.districtMap){
                map.put(key, 1);
            }
        }
    }

    public synchronized void decrementValue(Map<Integer, Integer> map, int key) {
        Integer value = map.get(key);

        if (value != null) {
            synchronized (map) {
                map.put(key, value - 1);
            }
        } else {
            synchronized (map) {
                map.put(key, -1);
            }
        }
    }

    private Object mechanicLock = new Object();

    public Object getExitLock() {
        return exitLock;
    }

    private Object exitLock = new Object();

    public Object getBalanceLock() {
        return balanceLock;
    }

    private Object balanceLock = new Object();

    public Object getMechanicLock() {
        return mechanicLock;
    }

    public void setMechanicLock(Object mechanicLock) {
        this.mechanicLock = mechanicLock;
    }

    private static ModelRobot instance;

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

    public synchronized void removeRobotById(String robotId) {
        synchronized (this.robotArrayList) {
            boolean removed = robotArrayList.removeIf(r -> r.getID().equals(robotId));
            System.out.println(removed ? "OK DELETED" : "ERROR: Robot not found!");
        }
    }

    public Map<Integer, Integer> getDistrictMap() {
        return districtMap;
    }

    public void setDistrictMap(Map<Integer, Integer> districtMap) {
        this.districtMap = districtMap;
    }

    public synchronized boolean containsRobotWithId(String robotId) {
        synchronized (robotArrayList) {
            synchronized (this.robotArrayList){
                return robotArrayList.stream().anyMatch(robot -> robot.getID().equals(robotId));
            }

        }
    }

}
