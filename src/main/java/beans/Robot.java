package beans;
import com.google.gson.annotations.SerializedName;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Robot {


    @SerializedName("robotId")
    @XmlElement(name = "robotId")
    private String id;
    @SerializedName("robotIp")
    @XmlElement(name = "robotIp")
    private String ip;

    private int port;

    private Position pos;

    private int district;

    public Robot(){}

    public Robot(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }


    public String getID() {return id;}

    public String getIP() {return ip;}

    public int getPort() {return port;}

    public void setPort(int port) {
        this.port = port;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public void setDistrict(int district) {
        this.district = district;
    }

    public int getDistrict() {
        return district;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Robot:" +
                "\n\t-id: " + id +
                "\n\t-ip: " + ip +
                "\n\t-port: " + port + "\n";
    }
}



