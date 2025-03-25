package Server.view;

import common.Log.LogManager;
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

        LogManager.getInstance().setLogArea(logArea);

        clearLogButton.addActionListener(e -> clearLog());
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
        logArea.setFont(new Font("Monospaced", Font.BOLD, 14));

        scrollPane = new JScrollPane(logArea);
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

    public JTextArea getLogArea() {
        return logArea;
    }

    public void clearLog() {
        SwingUtilities.invokeLater(() -> logArea.setText(""));
    }
}
