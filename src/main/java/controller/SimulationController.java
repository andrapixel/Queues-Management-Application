package controller;

import model.*;
import view.SimulationMenuView;
import view.SimulationView;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.exit;

public class SimulationController implements Runnable {
    // Attributes region
    public int noClients;   // total number of clients
    public int noQueues;    // total number of queues
    public int maxSimulationTime;
    // arrival and service time limits for the clients
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minServiceTime;
    public int maxServiceTime;

    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    private QueueScheduler queueScheduler;   // entity responsible with queue management and client distribution
    private List<Client> generatedClientsList;   // list of clients shopping in the store

    public float totalServiceTime;
    public static int peakHour;
    public static float averageWaitingTime;
    public static float averageServiceTime;
    private int maxNoOfWaitingClientsInQueues = 0;
    private int noClientsPerQueue = 0;

    // attributes used for logging
    private String inputFile, outputFile;
    private static File logFile;

    private SimulationMenuView simulationMenuView;
    private SimulationView simulationView;
    public static int option;
    // end region

    // Constructors region
    public SimulationController() {
        showMenu();     // show menu of options the user can choose from

        // select the input file to run tests on and read its contents
        Scanner scanner = new Scanner(System.in);
        option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                inputFile = "./Tests/test1.in";
                outputFile = "./Tests/test1.out";
                break;
            case 2:
                inputFile = "./Tests/test2.in";
                outputFile = "./Tests/test2.out";
                break;
            case 3:
                inputFile = "./Tests/test3.in";
                outputFile = "./Tests/test3.out";
                break;
            default:
                System.out.println("The chosen option is invalid.");
                break;
        }

        readInputFileContents(inputFile);

        // configuring output file path
        logFile = new File(outputFile);
        if (logFile.exists()) {     // if there already exists a file at the given path
            logFile.delete();                // delete the existing file
            logFile = new File(outputFile);  // and create a new one
        }

        // initialize the scheduler
        queueScheduler = new QueueScheduler(noQueues, noClients);
        queueScheduler.changeStrategy(selectionPolicy);

        generateNRandomClients();   // generate N random clients
        for (Client client : generatedClientsList)
            this.totalServiceTime += (float)client.getServiceTime();

        if (option == 1 || option == 2)
            simulationView = new SimulationView(this);
    }
    // end region

    public List<Client> getGeneratedClientsList() {
        return generatedClientsList;
    }

    // method that returns a random number from a given range
    private static int getRandomValueFromRange(int minLimit, int maxLimit) {
        return ThreadLocalRandom
                .current()
                .nextInt(minLimit, maxLimit + 1);
    }

    // here we generate the N random client tuples
    private void generateNRandomClients() {
        generatedClientsList = new ArrayList<>(); // instantiate list of generated clients

        for (int clientIndex = 0; clientIndex < noClients; ++clientIndex) {
            int randomArrivalTime = getRandomValueFromRange(minArrivalTime, maxArrivalTime);
            int randomServiceTime = getRandomValueFromRange(minServiceTime, maxServiceTime);
            generatedClientsList.add(new Client(clientIndex + 1, randomArrivalTime, randomServiceTime));
        }

        Collections.sort(generatedClientsList);  // sort client list with respect to their arrivalTime values
    }

    // method that iterates through the generatedClientsList and picks the clients that have the arrivalTime = currentTime
    private List<Client> getFilteredClients(int currentTime) {
        List<Client> filteredClients = new ArrayList<>();

        for (Client client : generatedClientsList) {
            if (client.getArrivalTime() == currentTime)
                filteredClients.add(client);    // add the client to a queue
        }

        return filteredClients;
    }

    public void run() {
        int currentTime = 0;
        System.out.println("Please wait " + maxSimulationTime + " seconds.");

        while (currentTime <= maxSimulationTime - 1) {
            // retrieve the list of clients that have the arrivalTime = currentTime
            List<Client> quickClients = getFilteredClients(currentTime);

            try {
                // send the retrieved clients to their respective queues, while following the chosen policy
                for (Client client : quickClients) {
                        queueScheduler.dispatchClient(client);  // send client to queue
                        generatedClientsList.remove(client);    // delete client from list
                }

                System.out.println(currentTime + 1);

                // write results into the output logs
                this.noClientsPerQueue = 0;
                writeOutputFileLogs(logFile, currentTime, 1);
                writeOutputFileLogs(logFile, currentTime, 2);

                // update simulation view
                if (option == 1 || option == 2)
                    simulationView.updateSimulationViewFrame(Integer.toString(currentTime), getGeneratedClientsList(), queueScheduler.getQueues());

                currentTime++;  // increment the current time
                Thread.sleep(1000);  // wait an interval of 1 second

                // when the current time surpases the assigned maximum simulation time, exit the loop
                if (currentTime > maxSimulationTime - 1)
                    break;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (SimulationException simE) {
                System.out.println(simE.getMessage());
            }
        }

        // log the supplementary results (average waiting time, average service time, peak hour)
        writeOutputFileLogs(logFile, currentTime, 3);
        System.out.println("\nOutput file was generated successfully.");

        if (option == 3)
            exit(0);
    }

    // method that displays the option menu in the console when the application starts
    private static void showMenu() {
        System.out.println("\nPlease select one of the following options:");
        System.out.println("\t1 - Run the first input test.");
        System.out.println("\t2 - Run the second input test.");
        System.out.println("\t3 - Run the third input test.");
        System.out.println();   // leaves an empty line to separate the menu from the user's input
    }

    // methods that work with file reading & writing operations
    private void readInputFileContents(String inputFilePath) {
        ArrayList<Integer> inputData = new ArrayList<>();
        String currentLine;
        String[] dataOnLine;

        try {
            File inputFile = new File(inputFilePath);
            Scanner scanner = new Scanner(inputFile);

            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                dataOnLine = currentLine.split(" ");

                for (String inputStr : dataOnLine) {
                    inputData.add(Integer.parseInt(inputStr));
                }
            }

            this.noClients = inputData.get(0);
            this.noQueues = inputData.get(1);
            this.maxSimulationTime = inputData.get(2);
            this.minArrivalTime = inputData.get(3);
            this.maxArrivalTime = inputData.get(4);
            this.minServiceTime = inputData.get(5);
            this.maxServiceTime = inputData.get(6);
            scanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error! File not found.");
            e.printStackTrace();
        }
        catch (NullPointerException eNull) {
            eNull.printStackTrace();
        }
    }

    private void writeOutputFileLogs(File outputFile, int realTime, int logCode) {
        try {
            FileWriter writingBuffer = new FileWriter(outputFile, true);    // instantiate the buffer used for writing contents in a file

            if (logCode == 1) {
                writingBuffer.write("Time " + realTime + "\n"); // log the current time

                // log the clients waiting to be assigned to a queue
                writingBuffer.write("Waiting clients: ");
                if (!generatedClientsList.isEmpty()) {
                    for (Client client : generatedClientsList) {
                        writingBuffer.write(client.toString());
                    }
                }
                else
                    writingBuffer.write("No clients in line");
                writingBuffer.write("\n");

                // log the queues and their corresponding clients
                List<ClientsQueue> queueList = queueScheduler.getQueues();
                for (int q = 0; q < queueList.size(); ++q) {
                    this.noClientsPerQueue += queueList.get(q).getClients().length;
                    // computing the peak hour
                    if (noClientsPerQueue >= this.maxNoOfWaitingClientsInQueues) {
                        this.maxNoOfWaitingClientsInQueues = noClientsPerQueue;
                        this.peakHour = realTime;
                    }
                    // logging clients waiting at the current queue
                    writingBuffer.write(queueList.get(q).toString());
                    writingBuffer.write("\n");
                }
            }

            if (logCode == 2) {     // logging the number of clients that are waiting in queues at the current time
                writingBuffer.write("Total number of clients that are waiting in queues at this time: " + this.noClientsPerQueue + " clients");
                writingBuffer.write("\n\n");
            }

            if (logCode == 3) {  // log simulation results
                logSimulationResults(writingBuffer);
            }

            writingBuffer.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void logSimulationResults(FileWriter writingBuffer) throws IOException {
            writingBuffer.write("Average waiting time: " + getAverageWaitingTime() + "\n");
            writingBuffer.write("Average service time: " + getAverageServiceTime() + "\n");
            writingBuffer.write("Peak hour: " + this.peakHour + "\n");
    }

    private Float getAverageWaitingTime() {
        averageWaitingTime = QueueScheduler.totalWaitingTime / noClients;
        return (QueueScheduler.totalWaitingTime / noClients);
    }

    private Float getAverageServiceTime() {
        averageServiceTime = this.totalServiceTime / noClients;
        return (this.totalServiceTime / noClients);
    }
}
