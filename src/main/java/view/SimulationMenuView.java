package view;

import controller.SimulationController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SimulationMenuView extends JFrame {
    private JPanel simulationMenuPanel;
    private JTextField noClientsTxt;
    private JTextField noQueuesTxt;
    private JTextField simTimeTxt;
    private JTextField minArrivalTxt;
    private JTextField maxArrivalTxt;
    private JTextField minServiceTxt;
    private JTextField maxServiceTxt;
    private JButton startSimulationBtn;
    private SimulationController simulationManager;
    private SimulationView simulationView;
    public static File inputFileGeneratedByGUI;
    public static boolean startSimulationButtonPressed = false;

    public SimulationMenuView(final SimulationController simulationManager) {
        super("Queue Simulator Menu");
        setContentPane(simulationMenuPanel);
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        this.simulationManager = simulationManager;
        startSimulationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputFileGeneratedByGUI = new File("./Tests/simulation.in");
                if (inputFileGeneratedByGUI.exists()) {     // if there already exists a file at the given path
                    inputFileGeneratedByGUI.delete();                // delete the existing file
                    inputFileGeneratedByGUI = new File("./Tests/simulation.in");  // and create a new one
                }
                createInputFile(inputFileGeneratedByGUI);

                System.out.println("Simulation input file has been created");
                startSimulationButtonPressed = true;
                //simulationView = new SimulationView();
                Thread thread = new Thread(simulationManager);
                thread.start();
            }
        });
    }

    private int getNoClients() {
        int noClientsVal = 0;

        try {
            noClientsVal = Integer.parseInt(this.noClientsTxt.getText());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid string");
            e.printStackTrace();
        }

        return noClientsVal;
    }

    private int getNoQueues() {
        int noQueuesVal = 0;

        try {
            noQueuesVal = Integer.parseInt(this.noQueuesTxt.getText());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid string");
            e.printStackTrace();
        }

        return noQueuesVal;
    }

    private int getSimulationTime() {
        int simTimeVal = 0;

        try {
            simTimeVal = Integer.parseInt(this.simTimeTxt.getText());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid string");
            e.printStackTrace();
        }

        return simTimeVal;
    }

    private int getMinArrivalTime() {
        int minArrivalTimeVal = 0;

        try {
            minArrivalTimeVal = Integer.parseInt(this.minArrivalTxt.getText());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid string");
            e.printStackTrace();
        }

        return minArrivalTimeVal;
    }

    private int getMaxArrivalTime() {
        int maxArrivalTimeVal = 0;

        try {
            maxArrivalTimeVal = Integer.parseInt(this.maxArrivalTxt.getText());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid string");
            e.printStackTrace();
        }

        return maxArrivalTimeVal;
    }

    private int getMinServiceTime() {
        int minServiceTimeVal = 0;

        try {
            minServiceTimeVal = Integer.parseInt(this.minServiceTxt.getText());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid string");
            e.printStackTrace();
        }

        return minServiceTimeVal;
    }

    private int getMaxServiceTime() {
        int maxServiceTimeVal = 0;

        try {
            maxServiceTimeVal = Integer.parseInt(this.maxServiceTxt.getText());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid string");
            e.printStackTrace();
        }

        return maxServiceTimeVal;
    }

    private void createInputFile(File inputFileGUI) {

        try {
            FileWriter writingBuffer = new FileWriter(inputFileGUI, true);    // instantiate the buffer used for writing contents in a file

            writingBuffer.write(this.noClientsTxt.getText());
            writingBuffer.write("\n");
            writingBuffer.write(this.noQueuesTxt.getText());
            writingBuffer.write("\n");
            writingBuffer.write(this.simTimeTxt.getText());
            writingBuffer.write("\n");
            writingBuffer.write(this.minArrivalTxt.getText());
            writingBuffer.write(" ");
            writingBuffer.write(this.maxArrivalTxt.getText());
            writingBuffer.write("\n");
            writingBuffer.write(this.minServiceTxt.getText());
            writingBuffer.write(" ");
            writingBuffer.write(this.maxServiceTxt.getText());

            writingBuffer.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
