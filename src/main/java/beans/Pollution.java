package beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Pollution {

    private double pm_10;
    private long timestamp;  //milliseconds


    public Pollution(){}

    public Pollution(double pm_10, long timestamp){
        this.pm_10 = pm_10;
        this.timestamp = timestamp;
    }

    public double getPm_10(){return pm_10;}

    public long getTimestamp(){return timestamp;}

    public void setPm_10(double pm_10){
        this.pm_10 = pm_10;
    }

    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }


}
