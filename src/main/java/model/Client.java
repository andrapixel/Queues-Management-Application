package model;

public class Client implements Comparable<Client> {
    // Attributes region
    private int clientId;
    private int arrivalTime;
    private int serviceTime;
    // end region

    // Constructors region
    public Client(int clientId, int arrivalTime, int serviceTime) {
        this.clientId = clientId;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
    // end region

    // Getters & Setters region
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    // end region

    @Override
    public String toString() {
        return "(" + clientId + ", " + arrivalTime + ", " + serviceTime + "); ";
    }

    // method that compares two clients best on their arrival times
    // used for sorting the clients by arrival time later on
    @Override
    public int compareTo(Client c) {
        return (Integer.compare(this.getArrivalTime(), c.getArrivalTime()));
    }
}
