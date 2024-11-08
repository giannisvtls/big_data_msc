import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashClient {
    private static final Logger logger = Logger.getLogger(DiseaseHandler.class.getName());
    // Helper function that sets default values to env vars that are not configured
    private static int getEnvVarOrDefault() {
        String value = System.getenv("SERVER_PORT");
        if (value == null || value.isEmpty()) {
            logger.log(Level.WARNING,"Environment variable \"" + "SERVER_PORT" + "\" not set, using default value: " + 9003);
            return 9003;
        }
        return Integer.parseInt(value);
    }

    private static final int port = getEnvVarOrDefault();
    private final String serverAddress;

    public HashClient(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void start() {
        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server at " + serverAddress + ":" + port);
            System.out.println("       ");
            System.out.println("Available commands:");
            System.out.println("1,key,value - Insert");
            System.out.println("2,key - Delete");
            System.out.println("3,key - Search");
            System.out.println("0,0 - Exit");
            System.out.println("       ");

            while (true) {
                System.out.print("Enter command: ");
                String userInput = scanner.nextLine();
                
                out.println(userInput);
                
                String[] parts = userInput.split(",");
                int operation = Integer.parseInt(parts[0]);
                
                if (operation == 0) {
                    System.out.println("Exiting...");
                    break;
                }

                String response = in.readLine();
                switch (operation) {
                    case 1:
                        System.out.println(response.equals("1") ? "Insertion successful" : "Insertion failed");
                        break;
                    case 2:
                        System.out.println(response.equals("1") ? "Deletion successful" : "Deletion failed");
                        break;
                    case 3:
                        System.out.println("Value: " + response);
                        break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverAddress);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to:" + serverAddress);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Default to localhost
        new HashClient(serverAddress).start();
    }
}
