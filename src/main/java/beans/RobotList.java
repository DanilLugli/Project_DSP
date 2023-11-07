package beans;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class RobotList {
    private List<Robot> robots;

    public RobotList(List<Robot> robots) {
        this.robots = robots;
    }

    public RobotList() {}

    public List<Robot> getRobots() {
        return robots;
    }

    public void setRobots(List<Robot> robots) {
        this.robots = robots;
    }
}
