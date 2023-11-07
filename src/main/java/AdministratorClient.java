import beans.*;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdministratorClient {

    public static String restAddressRobots = "http://localhost:1993/Robot";
    public static String restAddressStatistic = "http://localhost:1993/Pollution";
    public static Client client = Client.create();
    public static Scanner sc = new Scanner(System.in);


    static String ListRequestClient = "What you need? Select with the number of method: \n" +
            "\t(1) Get list of robots in Greenfield\n" +
            "\t(2) Get average of the last n air pollution levels sent to the server by a given robot\n" +
            "\t(3) Get average of the air pollution levels sent by all the robots to the server and occurred from timestamps t1 and t2\n" +
            "\t(4) Quit\n\n" +
            "Insert a command between 1 and 4: ";

    private static void getListRobots() {

        WebResource webResource = client.resource(restAddressRobots + "/getList");
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
        if (response != null && response.getStatus() == 200) {
            try {

                Gson gson = new Gson();
                List<Robot> r = gson.fromJson((response.getEntity(String.class)), RobotList.class).getRobots();

                if (r != null && r.size() >= 1) {
                    System.out.println("List of " + r.size() + " Robots in Greenfield: ");
                    for (Robot robot : r
                    ) {
                        System.out.println(robot.toString());
                    }
                } else {
                    System.out.println("Greenfield has no robots now!");
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }


    private static void getLastNAvg() {
        Scanner sc = new Scanner(System.in);

        getListRobots();
        System.out.println("____");

        System.out.print("Enter the ID of the robot you need: ");
        String robotId = sc.next();

        WebResource webResource = client.resource(restAddressRobots + "/getList");
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
        Gson gson = new Gson();
        List<Robot> r = gson.fromJson((response.getEntity(String.class)), RobotList.class).getRobots();

        for (Robot r1 : r
             ) {
            if(r1.getID().equals(robotId)){

                System.out.print("Enter the number of pollution data you want: ");
                int n = sc.nextInt();
                String nJson = gson.toJson(n);

                WebResource webResource2 = client.resource(restAddressStatistic + "/get" + nJson);
                System.out.println(webResource2);
                ClientResponse response2 = webResource2.type("application/json").get(ClientResponse.class);
                System.out.println(response2);   // --> NOT FOUND 404
                /*
                * BISOGNA CAPIRE DOVE AGGIUNGO LE MEDIE, E CAPIRE SE LE AGGIUNGE DAVVERO
                *
                * */

                try {
                    double avg = PollutionModels.getInstance().getLastNPollution(robotId, n);
                    System.out.println("\nThe average of the last " + n + " pm_10 of " + robotId + " is: " + avg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void getPollutionTimestamp() {

        System.out.print("\nEnter the start of timestamp that you need: ");
        long t1 = sc.nextLong();
        System.out.print("\nEnter the end of timestamp that you need: ");
        long t2 = sc.nextLong();

        WebResource webResource = client.resource(restAddressStatistic + "/getTimestampPollution/" + t1 + "-" + t2);
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

        try {
            double avg = PollutionModels.getInstance().getAvgPollutionTimestamp(t1, t2);
            System.out.println("\nThe average between: " + t1 + "-" + t2 + "is: " + avg);
        } catch (Exception e) {
            System.out.println(e);

        }

    }

    public static void main(String[] args) {
        System.out.println("--> GREENFIELD ADMIN CLIENT <--\n");
        int choice = 0;
        boolean exit = false;
        while (!exit) {
            System.out.print(ListRequestClient);
            try {
                choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        getListRobots();
                        break;
                    case 2:
                        getLastNAvg();
                        break;
                    case 3:
                        getPollutionTimestamp();
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        System.out.println("Please enter a valid command.");
                }
            } catch (Exception e) {
                choice = 0;
                System.out.println("Enter a valid number for your choice. ");
            }
        }
        sc.close();
        System.exit(0);
    }
}
