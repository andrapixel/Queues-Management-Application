package view;

import controller.SimulationController;
import model.Client;
import model.ClientsQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationView extends JFrame {
    private SimulationController simulationController;
    private JPanel simulationPanel;
    private JLabel timeLbl;
    private JButton exitBtn;
    private JLabel queue1Lbl;
    private JLabel queue2Lbl;
    private JLabel queue3Lbl;
    private JLabel queue4Lbl;
    private JLabel queue5Lbl;
    private JLabel queue3TitleLbl;
    private JLabel queue4TitleLbl;
    private JLabel queue5TitleLbl;
    private JScrollPane scrollPane3;
    private JScrollPane scrollPane4;
    private JScrollPane scrollPane5;
    private JTextPane waitingClientsTxtPane1;
    private JButton seeLogsBtn;
    private JButton openLogFileBtn;
    private final int WINDOW_WIDTH = 850, WINDOW_HEIGHT = 600;
    private File file;
    private String path = "";

    public SimulationView(final SimulationController simulationController) {
        super("Queue Simulator");
        setContentPane(simulationPanel);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        if (simulationController.option == 1) {
            this.path = "./Tests/test1.out";
            queue3TitleLbl.setVisible(false);
            queue3Lbl.setVisible(false);
            scrollPane3.setVisible(false);
            queue4TitleLbl.setVisible(false);
            queue4Lbl.setVisible(false);
            scrollPane4.setVisible(false);
            queue5TitleLbl.setVisible(false);
            queue5Lbl.setVisible(false);
            scrollPane5.setVisible(false);
        }
        else if (simulationController.option == 2)
            this.path = "./Tests/test2.out";

        exitBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        seeLogsBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("\n\n");
                stringBuilder.append("Average Waiting Time = " + simulationController.averageWaitingTime).append("\n");
                stringBuilder.append("Average Service Time = " + simulationController.averageServiceTime).append("\n");
                stringBuilder.append("Peak Hour = " + simulationController.peakHour).append("\n\n");
                JOptionPane.showMessageDialog(new JFrame(), stringBuilder.toString(), "Log Dialog", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        openLogFileBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    file = new File(path);
                    if (!Desktop.isDesktopSupported()) {
                        System.out.println("not supported");
                        return;
                    }

                    Desktop desktop = Desktop.getDesktop();
                    if (file.exists())
                        desktop.open(file);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public String clientsToString(List<Client> waitingClients) {
        String clientsListString = "";

        for (Client client : waitingClients)
            clientsListString += client.toString();

        return clientsListString;
    }

    public void updateSimulationViewFrame(String currentTimeString, List<Client> waitingClients, List<ClientsQueue> queues) {
        this.timeLbl.setText(currentTimeString);
        this.waitingClientsTxtPane1.setText(clientsToString(waitingClients));

        ArrayList<String> queueStrings = new ArrayList<>();
        for (int q = 0; q < queues.size(); ++q) {
            if (queues.get(q).getClients().length != 0) {
                queueStrings.add(q, clientsToString(Arrays.asList(queues.get(q).getClients())));
            }
            else
                queueStrings.add(q, "closed");
        }

        if (queues.size() == 2) {
            queue1Lbl.setText(queueStrings.get(0));
            queue2Lbl.setText(queueStrings.get(1));
        }
        else if (queues.size() == 5) {
            queue1Lbl.setText(queueStrings.get(0));
            queue2Lbl.setText(queueStrings.get(1));
            queue3Lbl.setText(queueStrings.get(2));
            queue4Lbl.setText(queueStrings.get(3));
            queue5Lbl.setText(queueStrings.get(4));
        }
    }
}
