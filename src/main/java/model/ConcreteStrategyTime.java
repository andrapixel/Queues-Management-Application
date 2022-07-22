package model;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    private int minWaitingTime;     // the minimum waiting time of the current queue

    @Override
    public void addClient(List<ClientsQueue> queues, Client client) {
        // updating the total waiting time of the queues at a given time
        QueueScheduler.totalWaitingTime += this.minWaitingTime + client.getServiceTime();

        if (this.minWaitingTime != Integer.MAX_VALUE) {
            getShortestWaitingTimeQueue(queues).addClientToQueue(client);
        }
    }

    // method that gets the queue with the shortest waiting time from the queue-list
    private ClientsQueue getShortestWaitingTimeQueue(List<ClientsQueue> queueList) {
        ClientsQueue bestQueue = null;   // the queue with the shortest waiting time out of all the queues
        this.minWaitingTime = Integer.MAX_VALUE;

        for (ClientsQueue nextQueue : queueList) {
            if (nextQueue.getWaitingPeriod().get() < this.minWaitingTime) {
                this.minWaitingTime = nextQueue.getWaitingPeriod().get();
                bestQueue = nextQueue;
            }
        }

        return bestQueue;
    }
}
