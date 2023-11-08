import beans.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.lang.reflect.Type;
import java.util.*;

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

        System.out.print("Enter the ID of the robot you need: ");
        String robotId = sc.next();

        Client client = Client.create();

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

    /*private static void getLastNAvg() {
        Scanner sc = new Scanner(System.in);

        WebResource webResource = client.resource(restAddressStatistic + "/get");
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

        if (response.getStatus() == 200) {
            try {
                Gson gson = new Gson();
                Type mapType = new TypeToken<HashMap<String, ArrayList<Pollution>>>() {}.getType();
                HashMap<String, ArrayList<Pollution>> pollutionMap = gson.fromJson(response.getEntity(String.class), mapType);

                System.out.print("Enter the ID of the robot you need: ");
                String robotId = sc.next();

                ArrayList<Pollution> pollutionData = pollutionMap.get(robotId);

                if (pollutionData != null) {
                    System.out.print("Enter the number of pollution data you want: ");
                    int n = sc.nextInt();

                    if (pollutionData.size() >= n) {
                        ArrayList<Pollution> lastNData = new ArrayList<>(pollutionData.subList(pollutionData.size() - n, pollutionData.size()));

                        double avg = lastNData.stream().mapToDouble(Pollution::getPm_10).average().orElse(0.0);
                        System.out.println("The average of the last " + n + " pm_10 of " + robotId + " is: " + avg);
                    } else {
                        System.out.println("Not enough data available for " + robotId);
                    }
                } else {
                    System.out.println("No data available for " + robotId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error getting pollution data. Status code: " + response.getStatus());
        }
    }*/



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
