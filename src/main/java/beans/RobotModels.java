package beans;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//Classe per gestire la lista dei robot
@XmlRootElement
@XmlAccessorType
public class RobotModels {
    private static ArrayList<Robot> listRobot;

    private final int[] arrayDistrict = new int[4];

    private static RobotModels instance;

    private RobotModels() {
        listRobot = new ArrayList<>();
    }

    public synchronized static RobotModels getInstance() {

        if (instance == null) {
            instance = new RobotModels();
        }
        return instance;
    }

    public int[] getArrayDistrict() {
        return arrayDistrict;
    }

    public ArrayList<Robot> getListRobot() {
        synchronized (this.listRobot) {
            return new ArrayList<Robot>(this.listRobot);
        }
    }

    public Robot getById(String id){
        for (Robot rob : this.getListRobot()){
            if(rob.getID().equals(id))
                return rob;
        }
        return null;
    }

    public static int randomCoord(int min, int max) {
        int pos = ThreadLocalRandom.current().nextInt(min, max + 1);
        return pos;
    }

    public int getMinDistrict(){
        //check empty robotList
        if(listRobot.isEmpty()){
            Random rd = new Random();
            int d = rd.nextInt(4);
            return d;
        }
        else{
            int min = 0;
            for(int i=0; i<=3; i++){
                if(arrayDistrict[i]< arrayDistrict[min]){
                    min = i;
                }
            }
            return min;
        }

    }

    public Position settingDistrict(Robot robot){
        Position p = new Position();
        int min = getMinDistrict();
        if(min == 0){
            p.setX(randomCoord(0,5));
            p.setY(randomCoord(0,5));
            robot.setDistrict(0);
            arrayDistrict[0] ++;
            return p;
        } else if (min == 1) {
            p.setX(randomCoord(6,10));
            p.setY(randomCoord(0,5));
            robot.setDistrict(1);
            arrayDistrict[1] ++;
            return p;
        } else if (min == 2) {
            p.setX(randomCoord(0,5));
            p.setY(randomCoord(6,10));
            robot.setDistrict(2);
            arrayDistrict[2] ++;
            return p;
        } else{
            p.setX(randomCoord(6,10));
            p.setY(randomCoord(6,10));
            robot.setDistrict(3);
            arrayDistrict[3] ++;
            return p;
        }
    }

    public CoordRobot addRobot(Robot robot) {
        for (Robot rob : this.getListRobot()) {
            if (rob.getID().equals(robot.getID()))
                return null;
        }

        Position p = new Position() ;
        robot.setPos(settingDistrict(robot));
        int district = robot.getDistrict();

        synchronized (listRobot) {
            this.listRobot.add(robot);
        }

        return new CoordRobot(listRobot, p , district);
    }

    public boolean deleteRobot(String id) {
        synchronized (listRobot) {
            for (Robot rob : listRobot) {
                if (rob.getID().equals(id)) {
                    listRobot.remove(rob);
                    arrayDistrict[rob.getDistrict()]--;
                    return true;
                }
            }
        }
        System.out.println("It's not able to delete this robot. ");
        return false;
    }

    public static Position updatePos(int district) {
        Position p = new Position();
        switch (district) {
            case 0:
                p.setX(randomCoord(0, 5));
                p.setY(randomCoord(0, 5));
                break;
            case 1:
                p.setX(randomCoord(6, 10));
                p.setY(randomCoord(0, 5));
                break;
            case 2:
                p.setX(randomCoord(0, 5));
                p.setY(randomCoord(6, 10));
                break;
            case 3:
                p.setX(randomCoord(6, 10));
                p.setY(randomCoord(6, 10));
                break;
            default:
                throw new IllegalArgumentException("Distretto non valido: " + district);
        }
        return p;
    }


}
