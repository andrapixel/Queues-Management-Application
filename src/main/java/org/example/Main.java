package org.example;

import controller.SimulationController;

public class Main
{
    public static void main( String[] args ) throws InterruptedException {

        SimulationController simulation = new SimulationController();
        Thread thread = new Thread(simulation);
        thread.start();
    }
}
