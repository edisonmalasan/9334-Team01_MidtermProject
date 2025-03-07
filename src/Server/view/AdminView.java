package Server.view;
/**
 * GUI for admin view
 */

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.*;

public class AdminView extends JFrame {
    private JTextArea textArea;
    private File xmlFile;
    private DefaultListModel<String> playerListModel;
    private JList<String> playerList;
    private JTextField nameField, scoreField;
    private JButton saveButton, deleteButton, refreshButton;  // Add refresh button

    public AdminView(String xmlFilePath, String title) {
        super(title);
        this.xmlFile = new File(xmlFilePath);

        // Initialize GUI components
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Player List Model for JList
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(playerList);
        add(listScrollPane, BorderLayout.WEST);

        // Text area to display XML content
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panel to add/edit player
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(4, 2)); // Add an extra row for refresh button
        editPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        editPanel.add(nameField);

        editPanel.add(new JLabel("Score:"));
        scoreField = new JTextField();
        editPanel.add(scoreField);

        saveButton = new JButton("Save Changes");
        editPanel.add(saveButton);

        deleteButton = new JButton("Delete Player");
        editPanel.add(deleteButton);

        refreshButton = new JButton("Refresh");
        editPanel.add(refreshButton); // Add refresh button to panel

        add(editPanel, BorderLayout.SOUTH);

        // Listen to selection changes in player list
        playerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedPlayer = playerList.getSelectedValue();
                if (selectedPlayer != null) {
                    String name = selectedPlayer.split(" \\| ")[0].replace("Player: ", "");
                    nameField.setText(name);
                    String score = selectedPlayer.split(" \\| ")[1].replace("Score: ", "");
                    scoreField.setText(score);
                }
            }
        });

        setVisible(true);
    }

    // Set the data for the player list and text area
    public void updatePlayerList(String playerData) {
        textArea.setText(playerData);
    }

    public void updateListModel(DefaultListModel<String> model) {
        playerListModel = model;
        playerList.setModel(playerListModel);
    }

    public String getSelectedPlayer() {
        return playerList.getSelectedValue();
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getScoreField() {
        return scoreField;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;  // Return the refresh button
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public DefaultListModel<String> getPlayerListModel() {
        return playerListModel;
    }
}

