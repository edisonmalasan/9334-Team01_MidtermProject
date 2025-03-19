package Server.controller;
/**
 * Manages xml files
 */

import common.AnsiFormatter;
import common.model.QuestionModel;
import utility.LeaderboardEntryModel;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Manipulates XML files
 */
public class XMLStorageController {
private static final Logger logger = Logger.getLogger(XMLStorageController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    /**
     * Returns the list of questions
     */
    public static List<QuestionModel> loadQuestionsFromXML(String filename) {
        List<QuestionModel> questions = new ArrayList<>();
        try {
            File file = new File(filename);
            if (!file.exists()) return questions;

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("question");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String category = element.getAttribute("category");
                String questionText = element.getElementsByTagName("text").item(0).getTextContent();
                String correctAnswer = element.getElementsByTagName("answer").item(0).getTextContent();
                int score = Integer.parseInt(element.getElementsByTagName("score").item(0).getTextContent());
                List<String> choices = new ArrayList<>();

                NodeList choiceNodes = element.getElementsByTagName("choice");
                for (int j = 0; j < choiceNodes.getLength(); j++) {
                    choices.add(choiceNodes.item(j).getTextContent());
                }

                questions.add(new QuestionModel(category, questionText, choices, correctAnswer, score));
            }
            logger.info("XMLStorageModel: Questions loaded successfully from " + filename);
        } catch (Exception e) {
            logger.severe("XMLStorageModel: Error loading questions from XML: " + e.getMessage());
            e.printStackTrace();
        }
        return questions;
    }

    public static void saveQuestionsToXML(String filename, List<QuestionModel> questions) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("questions");
            doc.appendChild(rootElement);

            for (QuestionModel question : questions) {
                List<String> choices = question.getChoices();

                Element questionElement = doc.createElement("question");
                questionElement.setAttribute("category", question.getCategory());

                Element questionText = doc.createElement("text");
                questionText.appendChild(doc.createTextNode(question.getQuestionText()));
                questionElement.appendChild(questionText);

                for (String choice : choices) {
                    Element choiceElement = doc.createElement("choice");
                    choiceElement.appendChild(doc.createTextNode(choice));
                    questionElement.appendChild(choiceElement);
                }

                Element correctAnswer = doc.createElement("answer");
                correctAnswer.appendChild(doc.createTextNode(question.getCorrectAnswer()));
                questionElement.appendChild(correctAnswer);

                Element score = doc.createElement("score");
                score.appendChild(doc.createTextNode(String.valueOf(question.getScore())));
                questionElement.appendChild(score);

                rootElement.appendChild(questionElement);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

            logger.info("XMLStorageModel: Questions successfully saved to " + filename);
        } catch (Exception e) {
            logger.severe("XMLStorageModel: Error saving Questions to XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns a list containing leaderboard entries
     */
    public static List<LeaderboardEntryModel> loadLeaderboardFromXML(String filename) {
        List<LeaderboardEntryModel> leaderboard = new ArrayList<>();
        try {
            File file = new File(filename);
            File parentDir = file.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (!file.exists()) {
                logger.info("XMLStorageModel: Leaderboard file not found. Creating a new one.");
                file.createNewFile();
                return leaderboard;
            }

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("entry");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String playerName = element.getElementsByTagName("player").item(0).getTextContent();
                int score = Integer.parseInt(element.getElementsByTagName("score").item(0).getTextContent());
                leaderboard.add(new LeaderboardEntryModel(playerName, score));
            }
            logger.info("XMLStorageModel: Leaderboard loaded successfully from " + filename);
        } catch (Exception e) {
            logger.severe("XMLStorageModel: Error loading leaderboard from XML: " + e.getMessage());
            e.printStackTrace();
        }
        return leaderboard;
    }

    /**
     * Saves the list of leaderboard entries to the xml file
     */
    public static void saveLeaderboardToXML(String filename, List<LeaderboardEntryModel> leaderboard) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("leaderboard");
            doc.appendChild(rootElement);

            for (LeaderboardEntryModel entry : leaderboard) {
                Element entryElement = doc.createElement("entry");

                Element player = doc.createElement("player");
                player.appendChild(doc.createTextNode(entry.getPlayerName()));
                entryElement.appendChild(player);

                Element score = doc.createElement("score");
                score.appendChild(doc.createTextNode(String.valueOf(entry.getScore())));
                entryElement.appendChild(score);

                rootElement.appendChild(entryElement);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

            logger.info("XMLStorageModel: Leaderboard successfully saved to " + filename);
        } catch (Exception e) {
            logger.severe("XMLStorageModel: Error saving leaderboard to XML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
