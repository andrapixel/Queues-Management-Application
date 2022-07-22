package model;

import java.util.ArrayList;
import java.util.List;

public class QueueScheduler {
    // Attributes region
    private List<ClientsQueue> queues;  // a list of all the queues
    private Strategy strategy;
    public static float totalWaitingTime = 0.0f;    // the total waiting time summed from all the queues
    // end region

    // Constructor region
    public QueueScheduler(int maxNoQueues, int maxClientsPerQueue) {
        queues = new ArrayList<>();

        for (int queue = 0; queue < maxNoQueues; ++queue) {
            // create a new queue and add it to the list of queues
            ClientsQueue newQueue = new ClientsQueue(queue + 1, maxClientsPerQueue);
            queues.add(newQueue);

            // create the corresponding thread for the new queue and start it
            Thread thread = new Thread(newQueue);
            thread.start();
        }
    }
    // end region

    // Getters region
    public List<ClientsQueue> getQueues() {
        return queues;
    }
    // end region

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();

        if (policy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ConcreteStrategyTime();
    }

    public void dispatchClient(Client client) {
            strategy.addClient(queues, client);
    }
}
