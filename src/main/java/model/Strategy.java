package model;

import java.util.List;

public interface Strategy {

    void addClient(List<ClientsQueue> queues, Client client);
}
