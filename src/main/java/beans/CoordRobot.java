package beans;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;


@XmlRootElement
public class CoordRobot {

    private ArrayList<Robot> robotsList;

    private Position pos;
    private int district;

    public CoordRobot(){}

    public CoordRobot(ArrayList<Robot> robotsList, Position pos, int district){
        this.robotsList = robotsList;
        this.pos = pos;
        this.district = district;
    }

    public ArrayList<Robot> getRobotsList(){return this.robotsList; }

    public void setRobotsList(ArrayList<Robot> robotsList) {
        this.robotsList = robotsList;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
