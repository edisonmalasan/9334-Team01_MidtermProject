package Server.view;

import javax.swing.*;
import java.awt.*;

public class ServerView extends JFrame {
    private JButton startButton, stopButton, restartButton, clearLogButton;
    private JTextArea logArea;
    private JScrollPane scrollPane;

    public ServerView() {
        setupFrame();
        setupTopPanel();
        setupLogArea();
        setupBottomPanel();
    }

    private void setupFrame() {
        setTitle("SERVER");
        setSize(900, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void setupTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        startButton = new JButton("START");
        startButton.setBackground(Color.GREEN);
        stopButton = new JButton("STOP");
        stopButton.setBackground(Color.RED);
        restartButton = new JButton("RESTART");
        restartButton.setBackground(Color.BLUE);
        restartButton.setForeground(Color.WHITE);

        topPanel.add(startButton);
        topPanel.add(stopButton);
        topPanel.add(restartButton);

        add(topPanel, BorderLayout.NORTH);
    }

    private void setupLogArea() {
        logArea = new JTextArea();
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.WHITE);
        logArea.setEditable(false);
        logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        logPanel.add(logArea, BorderLayout.CENTER);

        scrollPane = new JScrollPane(logPanel);

        add(scrollPane, BorderLayout.CENTER);
    }


    private void setupBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        clearLogButton = new JButton("CLEAR LOG");
        clearLogButton.setBackground(Color.BLUE);
        clearLogButton.setForeground(Color.WHITE);
        bottomPanel.add(clearLogButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JButton getRestartButton() {
        return restartButton;
    }

    public JButton getClearLogButton() {
        return clearLogButton;
    }

    // Log handling methods
    public void appendToLog(String message) {
        logArea.append(message);
    }

    public void clearLog() {
        logArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerView serverView = new ServerView();
            serverView.setVisible(true);
        });
    }
}
