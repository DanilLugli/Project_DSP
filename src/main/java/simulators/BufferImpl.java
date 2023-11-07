package simulators;

import java.util.ArrayList;
import java.util.List;

public class BufferImpl implements Buffer{

    public BufferImpl(){} // costruttore
    public ArrayList<Measurement> buffer = new ArrayList<>();

    @Override
    public synchronized void addMeasurement(Measurement m) {
        //fill the data structure
        buffer.add(m);
        if (buffer.size() == 8){
            this.notify();
        }
    }

    @Override
    public synchronized List<Measurement> readAllAndClean() {
        //obtain the measurements stored in the data structure

        ArrayList<Measurement> bufferNew = new ArrayList<>();

        while(buffer.size() < 8) {
            try {
                this.wait();
            } catch (InterruptedException e) { e.printStackTrace();  }
        }
        if(buffer.size() == 8){

            bufferNew = new ArrayList<>(buffer);
            for (int i = 0; i < 4; i++){
                //50% -> 8/2 = 4
                buffer.remove(i);
            }

        }
        return bufferNew;
    }
}
