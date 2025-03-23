package Client.Admin.controller;

import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.io.*;

// TODO: ensure admin client communicates with the server independently
// FIXME: update lgoic

/**
 * Manipulates the admin GUI
 */
public class PlayerControllerAdmin {
//    private AdminView view;
//
//    public PlayerControllerAdmin(AdminView view) {
//        this.view = view;
//
//        // Set up event listeners
//        view.getSaveButton().addActionListener(e -> saveChanges());
//        view.getDeleteButton().addActionListener(e -> deleteSelectedPlayer());
//        view.getRefreshButton().addActionListener(e -> refreshLeaderboard());  // Add listener for refresh
//    }
//
//    /**
//     * Loads XML and updates the view with the current leaderboard
//     */
//    public void loadXML() {
//        try {
//            File xmlFile = view.getXmlFile();
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document document = builder.parse(xmlFile);
//            document.getDocumentElement().normalize();
//
//            NodeList nodeList = document.getElementsByTagName("entry");
//            DefaultListModel<String> listModel = new DefaultListModel<>();
//            StringBuilder xmlContent = new StringBuilder();
//
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                Node node = nodeList.item(i);
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    Element element = (Element) node;
//                    String name = element.getElementsByTagName("player").item(0).getTextContent();
//                    String score = element.getElementsByTagName("score").item(0).getTextContent();
//                    xmlContent.append("Player: ").append(name).append(" | Score: ").append(score).append("\n");
//
//                    listModel.addElement("Player: " + name + " | Score: " + score);
//                }
//            }
//            // Update view with the XML data
//            view.updatePlayerList(xmlContent.toString());
//            view.updateListModel(listModel);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Save changes to the XML
//     */
//    private void saveChanges() {
//        String name = view.getNameField().getText();
//        String score = view.getScoreField().getText();
//
//        try {
//            File xmlFile = view.getXmlFile();
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document document = builder.parse(xmlFile);
//            document.getDocumentElement().normalize();
//
//            // Update existing player entry
//            NodeList nodeList = document.getElementsByTagName("entry");
//            String selectedPlayer = view.getSelectedPlayer();
//            String nameToEdit = selectedPlayer.split(" \\| ")[0].replace("Player: ", "");
//
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                Node node = nodeList.item(i);
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    Element element = (Element) node;
//                    String existingName = element.getElementsByTagName("player").item(0).getTextContent();
//
//                    if (existingName.equals(nameToEdit)) {
//                        // Update player details
//                        element.getElementsByTagName("player").item(0).setTextContent(name);
//                        element.getElementsByTagName("score").item(0).setTextContent(score);
//                        break;
//                    }
//                }
//            }
//
//            // Save the updated document back to the file
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            DOMSource source = new DOMSource(document);
//            StreamResult result = new StreamResult(xmlFile);
//            transformer.transform(source, result);
//
//            // Reload the view with updated XML data
//            loadXML();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Deletes the selected player from the XML
//     */
//    private void deleteSelectedPlayer() {
//        String selectedPlayer = view.getSelectedPlayer();
//        if (selectedPlayer != null) {
//            String nameToDelete = selectedPlayer.split(" \\| ")[0].replace("Player: ", "");
//
//            try {
//                File xmlFile = view.getXmlFile();
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//                Document document = builder.parse(xmlFile);
//                document.getDocumentElement().normalize();
//
//                NodeList nodeList = document.getElementsByTagName("entry");
//                for (int i = 0; i < nodeList.getLength(); i++) {
//                    Node node = nodeList.item(i);
//                    if (node.getNodeType() == Node.ELEMENT_NODE) {
//                        Element element = (Element) node;
//                        String name = element.getElementsByTagName("player").item(0).getTextContent();
//
//                        if (name.equals(nameToDelete)) {
//                            // Remove player node from the document
//                            document.getDocumentElement().removeChild(node);
//                            break;
//                        }
//                    }
//                }
//
//                TransformerFactory transformerFactory = TransformerFactory.newInstance();
//                Transformer transformer = transformerFactory.newTransformer();
//                DOMSource source = new DOMSource(document);
//                StreamResult result = new StreamResult(xmlFile);
//                transformer.transform(source, result);
//
//                loadXML();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            JOptionPane.showMessageDialog(view, "Please select a player to delete.");
//        }
//    }
//
//    /**
//     * Refreshes the leaderboard file
//     */
//    private void refreshLeaderboard() {
//        loadXML();
//    }
}


