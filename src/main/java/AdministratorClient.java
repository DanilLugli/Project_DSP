import beans.Robot;
import beans.RobotList;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.List;
import java.util.Scanner;

public class AdministratorClient {

    public static String restAddressRobots = "http://localhost:1993/Robot";
    public static String restAddressStatistic = "http://localhost:1993/Pollution";
    public static Client client = Client.create();
    public static Scanner sc = new Scanner(System.in);


    static String ListRequestClient = "What you need? Select the number of method: \n" +
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
        Client client = Client.create();

        System.out.print("Enter the ID of the robot you need: ");
        String robotId = sc.next();

        System.out.println("Enter the number of PM you need: ");
        int n = sc.nextInt();

        WebResource webResource = client.resource(restAddressStatistic + "/get/"+ robotId + "/" + n);
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

        if (response.getStatus() == 200) {

            Gson gson = new Gson();
            String responseBody = response.getEntity(String.class);
            double lastNPollution = gson.fromJson(responseBody, Double.class);

            System.out.println("Last N Pollution for Robot " + robotId + " with n=" + n + ": " + lastNPollution);
        } else {
            System.out.println("Error: " + response.getStatus());
        }
    }

    private static void getPollutionTimestamp() {

        System.out.print("\nEnter the start of timestamp that you need: ");
        long t1 = sc.nextLong();
        System.out.print("\nEnter the end of timestamp that you need: ");
        long t2 = sc.nextLong();

        WebResource webResource = client.resource(restAddressStatistic + "/get/timeP/" + t1 + "-" + t2);
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

        if(response.getStatus() == 200){
            try {
                Gson gson = new Gson();
                String responseBody = response.getEntity(String.class);
                double lastTPollution = gson.fromJson(responseBody, Double.class);
                System.out.println("\nThe average between\nt1: " + t1 + "\nt2: " + t2 + "\n= " + lastTPollution);
            } catch (Exception e) {
                System.out.println(e);

            }
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
