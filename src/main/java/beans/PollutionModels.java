package beans;

import com.google.gson.Gson;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import java.util.Scanner;

@XmlRootElement
@XmlAccessorType
public class PollutionModels {

    Map<String, ArrayList<Pollution>> dictionaryPollution;

    private static PollutionModels instance;

    private PollutionModels(){
        dictionaryPollution = new HashMap<String, ArrayList<Pollution>>();
    }


    public synchronized static PollutionModels getInstance(){
        if(instance == null){
            instance = new PollutionModels();
        }
        return instance;
    }

    public void printDictionary() {
        for (Map.Entry<String, ArrayList<Pollution>> entry : dictionaryPollution.entrySet()) {
            String key = entry.getKey();
            ArrayList<Pollution> values = entry.getValue();

            System.out.println("Chiave: " + key);
            System.out.println("Valori: ");
            for (Pollution pollution : values) {
                System.out.println("  " + pollution.toString()); // Assumendo che Pollution abbia un metodo toString appropriato
            }
        }
    }

    public synchronized Map<String, ArrayList<Pollution>> getListPollution(){
        return dictionaryPollution;
    }

    public synchronized void addPollution(String robotId, Pollution pollution) {
        synchronized (dictionaryPollution) {

            ArrayList<Pollution> pollutionList = dictionaryPollution.get(robotId);

            if (pollutionList == null) {
                pollutionList = new ArrayList<>();
            }

            pollutionList.add(pollution);
            dictionaryPollution.put(robotId, pollutionList);

            //Print Data List for each Robot
            for (Map.Entry<String, ArrayList<Pollution>> entry : dictionaryPollution.entrySet()) {
                String key = entry.getKey();
                ArrayList<Pollution> values = entry.getValue();

                System.out.println("Robot: " + key);

                for (Pollution pollution2 : values) {
                    System.out.println("  - " + pollution2.getPm_10());
                }
            }


        }
    }


    public synchronized double getLastNPollution(String robotId, int n) {

        ArrayList<Pollution> lastN = PollutionModels.getInstance().dictionaryPollution.get(robotId);
        if (lastN != null) {
            lastN.stream().mapToDouble(Pollution::getPm_10).forEach(value -> System.out.println(value));
            return lastN.stream().mapToDouble(Pollution::getPm_10).sum() / n;
        } else {
            System.out.println("Robot does not exist. The problem is here!!");
            return 0;
        }
    }


    public synchronized double getAvgPollutionTimestamp(long t1, long t2){
        List<Double> pollutionValues = new ArrayList<>();

        int count = 0;

        for (List<Pollution> pollutionList : dictionaryPollution.values()) {
            for (Pollution pollution : pollutionList) {
                if (pollution.getTimestamp() >= t1 && pollution.getTimestamp() <= t2) {
                    pollutionValues.add(pollution.getPm_10());
                    count++;
                }
            }
        }

        if (count > 0) {
            double sum = pollutionValues.stream().mapToDouble(Double::doubleValue).sum();
            return sum / count;
        }

        return 0.0;
    }


}
