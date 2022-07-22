package model;

import controller.SimulationController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientsQueue implements Runnable {
    // Attributes region
    private int queueId;
    private int maxNoClientsPerQueue;   // maximum number of clients assigned per queue
    private BlockingQueue<Client> clientsList;
    private AtomicInteger waitingPeriod;    // the waiting period of the queue
    // end region

    // Constructors region
    public ClientsQueue(int queueId, int maxNoClientsPerQueue) {
        this.queueId = queueId;
        this.maxNoClientsPerQueue = maxNoClientsPerQueue;
        this.clientsList = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
    }
    // end region

    // Getters & Setters region
    public BlockingQueue<Client> getClientsList() {
        return clientsList;
    }

    public void setClientsList(BlockingQueue<Client> clientsList) {
        this.clientsList = clientsList;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }
    // end region

    // method that adds a new client to the queue
    public void addClientToQueue(Client newClient) {
        // if the queue already has the maximum allocated nr of clients, print error message
        if (clientsList.size() == maxNoClientsPerQueue)
        {
            SimulationException simException = new SimulationException("Error! The maximum queue capacity has been reached." +
                    "No more clients can be added to this queue.");
            throw simException;
        }

        clientsList.add(newClient);
        // update the waiting period of the queue
        waitingPeriod.set(this.waitingPeriod.intValue() + newClient.getServiceTime());
    }

    // Overriding the run() method of the Runnable Interface
    @Override
    public void run() {
        while (true) {  // while the simulation is running
            try {
                if (!clientsList.isEmpty()) {   // if the list of clients corresponding to the queue is not empty
                    Client client = this.clientsList.peek();    // get the client at the head of the list

                    // and while the client is served, let the thread sleep for 1000 * client_service_time seconds
                    while (client.getServiceTime() != 0) {
                        Thread.sleep(1000);
                        waitingPeriod.getAndDecrement();    // decrement the waiting period by 1
                        client.setServiceTime(client.getServiceTime() - 1);  // decrement the service time of the client by 1
                    }

                    // update the waiting period of the queue
                    waitingPeriod.set(this.waitingPeriod.intValue() - client.getServiceTime());
                    clientsList.poll();     // remove the first client from the list
                }
            } catch (InterruptedException exception) {
                throw new SimulationException(exception.getMessage());
            }
        }
    }

    // method that returns an array of the clients currently waiting in a queue
    public Client[] getClients() {
        Client[] clientsInQueueArray = new Client[clientsList.size()];
        clientsList.toArray(clientsInQueueArray);

        return clientsInQueueArray;
    }

    @Override
    public String toString() {
        // retrieving the clients array correspoding to the queue
        Client[] clientsArray = getClients();
        String queueStr = "Queue " + queueId + ": ";

        if (clientsArray.length != 0) {     // if there still are clients in the queue
            for (int i = 0; i < getClients().length; ++i)
                queueStr += clientsArray[i].toString();     // append the string format of each client to queueStr
        }
        else
            queueStr += "closed";   // otherwise, it means the queue is closed

        return queueStr;
    }
}
