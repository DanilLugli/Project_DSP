package GRPC;

import beans.Robot;

public class RequestMechanic extends Thread {
    private Robot s;
    private Robot r;

    long timestamp;

    public RequestMechanic(Robot s, Robot r, long timestamp) {
        this.s = s;
        this.r = r;
        this.timestamp = timestamp ;
    }

    public void run() {


    }
}
