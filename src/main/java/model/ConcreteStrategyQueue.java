package model;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    private int minQueueSize;     // the minimum size (smallest number of clients) of a queue

    @Override
    public void addClient(List<ClientsQueue> queues, Client client) {
        if (this.minQueueSize != Integer.MAX_VALUE) {
            getShortestQueue(queues).addClientToQueue(client);
        }
    }

    // method that gets the shortest queue from the queue-list
    private ClientsQueue getShortestQueue(List<ClientsQueue> queueList) {
        ClientsQueue bestQueue = null;   // the shortest queue
        this.minQueueSize = Integer.MAX_VALUE;

        for (ClientsQueue nextQueue : queueList) {
            if (nextQueue.getClients().length < this.minQueueSize) {
                this.minQueueSize = nextQueue.getClients().length;
                bestQueue = nextQueue;
            }
        }

        return bestQueue;
    }
}
