package Server.handler;
/**
 * Handles requests from the client
 */

import Server.controller.JSONStorageController;
import common.Log.AnsiFormatter;
import Client.Player.model.PlayerModel;
import Server.controller.LeaderboardControllerServer;
import Server.controller.QuestionController;
import utility.LeaderboardEntryModel;
import Redundant.XMLStorageController;
import common.Response;
import common.model.QuestionModel;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private String fileName;
    private Socket clientSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private LeaderboardControllerServer leaderboardControllerServer;
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    // Constructor initializes client socket and input/output streams
    public ClientHandler(Socket clientSocket, LeaderboardControllerServer leaderboardControllerServer) {
        this.clientSocket = clientSocket;
        this.leaderboardControllerServer = leaderboardControllerServer;

        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            logger.severe("Error initializing streams: " + e.getMessage());
        }
    }

    // Main run method for handling incoming client requests
    @Override
    public void run() {
        try {
            logger.info("New client connected: " + clientSocket.getInetAddress());

            // Handle client requests continuously
            while (!clientSocket.isClosed()) {
                Object request = objectInputStream.readObject();

                if (request instanceof String) {
                    String reqString = (String) request;
                    if (reqString.startsWith("GET_QUESTION:")) {
                        String category = reqString.split(":")[1].trim();
                        Response response = handleQuestionRequest(category);
                        sendResponse(response);
                    } else if (reqString.equals("GET_LEADERBOARD_CLASSIC")) {
                        List<LeaderboardEntryModel> classicLeaderboard = LeaderboardControllerServer.getClassicLeaderboard();
                        Response response = handleLeaderboardUpdate(classicLeaderboard,"classic");
                        sendResponse(response);
                    } else if (reqString.equals("GET_LEADERBOARD_ENDLESS")) {
                        List<LeaderboardEntryModel> endlessLeaderboard = LeaderboardControllerServer.getEndlessLeaderboard();
                        Response response = handleLeaderboardUpdate(endlessLeaderboard,"endless");
                        sendResponse(response);
                    } else if (reqString.equals("GET_QUESTIONS_LIST")) {
                        Response response = handleQuestionListRequest();
                        sendResponse(response);
                    }
                } else if (request instanceof PlayerModel) {
                    logger.info("Player score update request received.");
                    Response response = handlePlayerScoreUpdate((PlayerModel) request);
                    sendResponse(response);
                } else if (request instanceof LeaderboardEntryModel) {
                    logger.info("Leaderboard update request received.");
                    Response response = handleLeaderboardUpdateRequest((LeaderboardEntryModel) request);
                    sendResponse(response);
                } else if (request instanceof QuestionModel) {
                    logger.info("Questions update request received.");
                    Response response = handleQuestionUpdate((QuestionModel) request);
                    sendResponse(response);
                }
            }
        } catch (EOFException e) {
            logger.info("Client disconnected.");
        } catch (SocketException e) {
            logger.warning("Client forcibly closed the connection.");
        } catch (Exception e) {
            logger.severe("SERVER ERROR: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    // Sends a response back to the client
    private void sendResponse(Response response) throws IOException {
        logger.info("Sending response to client: " + response);

        try {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        } catch (IOException e) {
            logger.severe("Failed to send response: " + e.getMessage());
            throw e;
        }
    }

    // Handles leaderboard updates based on the game mode (classic/endless)
    private Response handleLeaderboardUpdate(List<?> list, String xmlFile) {
        try {
            if (list == null) {
                logger.severe("Received null player data.");
                return new Response(false, "Received null player data.", null);
            }

            if (xmlFile.equals("classic")) {
                fileName = "data/classic_leaderboard.xml";
            } else {
                fileName = "data/endless_leaderboard.xml";
            }
            List<LeaderboardEntryModel> leaderboard = XMLStorageController.loadLeaderboardFromXML(fileName);
            logger.info("Returning leaderboard data.");
            return new Response(true, "Leaderboard displayed successfully.", leaderboard);
        } catch (Exception e) {
            logger.severe("Error retrieving leaderboard: " + e.getMessage());
            return new Response(false, "Error retrieving leaderboard: " + e.getMessage(), null);
        }
    }

    // Handles player score updates and modifies the leaderboard accordingly
    private Response handlePlayerScoreUpdate(PlayerModel player) {
        try {
            if (player == null) {
                logger.severe("Received null player data.");
                return new Response(false, "Received null player data.", null);
            }

            String usernameLower = player.getName().toLowerCase();
            int newScore = player.getScore();
            logger.info("Updating player score: " + usernameLower + " with score: " + newScore);

            fileName = "data/classic_leaderboard.xml";
            List<LeaderboardEntryModel> leaderboard = XMLStorageController.loadLeaderboardFromXML(fileName);

            if (player.getName().endsWith("  ")) {
                player.getName().trim();
                player.setName(player.getName());
                fileName = "data/endless_leaderboard.xml";
                leaderboard = XMLStorageController.loadLeaderboardFromXML(fileName);
            }

            boolean found = false;
            for (LeaderboardEntryModel entry : leaderboard) {
                if (entry.getPlayerName().equalsIgnoreCase(usernameLower)) {
                    // compare the new score with the existing score
                    if (newScore > entry.getScore()) {
                        entry.setScore(newScore); // update the score only if the new score is higher
                        logger.info("Updated score for player: " + usernameLower + " to: " + newScore);
                    } else {
                        logger.info("New score is not higher. Keeping the existing score: " + entry.getScore());
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                leaderboard.add(new LeaderboardEntryModel(usernameLower, newScore));
                logger.info("Added new player to leaderboard: " + usernameLower + " with score: " + newScore);
            }

            XMLStorageController.saveLeaderboardToXML(fileName, leaderboard);

            logger.info("Player score updated successfully.");
            return new Response(true, "Player score updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error updating player score: " + e.getMessage());
            return new Response(false, "Error updating player score: " + e.getMessage(), null);
        }
    }

    // Handles a request for a question based on the selected category
    private Response handleQuestionRequest(String category) {
        logger.info("Server received question request for category: " + category);

        QuestionController questionController = new QuestionController();
        List<QuestionModel> questions = questionController.getQuestionsByCategory(category);

        if (questions.isEmpty()) {
            logger.warning("No questions found for category: " + category);
            return new Response(false, "No questions found for category: " + category, null);
        }

        logger.info("Question retrieved successfully for category: " + category);
        return new Response(true, "Question retrieved successfully.", questions);
    }

    // Handle a request for a list that contains all the questions in the database
    private Response handleQuestionListRequest() {
        logger.info("Server received request for question list");

        QuestionController questionController = new QuestionController();
        List<QuestionModel> allQuestions = questionController.getQuestionsList();

        if (allQuestions.isEmpty()) {
            logger.warning("No question list found.");
            return new Response(false, "No question list found", null);
        }

        logger.info("Question list retrieved successfully");
        return new Response(true, "Question list retrieved successfully.", allQuestions);
    }

    private Response handleQuestionUpdate(QuestionModel question) {
        try {
            logger.info("Server received request for removing question in database");
            if (question == null) {
                logger.severe("Received null question data.");
                return new Response(false, "Received null question data.", null);
            }

            fileName = "data/questions.json";
            List<QuestionModel> questionList = JSONStorageController.loadQuestionsFromJSON();

            boolean found = false;
            for (QuestionModel questionModel : questionList) {
                if (question.equals(questionModel)) {
                    logger.info("Removed question from database: " + questionModel.getCategory() + " with text: " + questionModel.getQuestionText());
                    questionList.remove(questionModel);
                    found = true;
                    break;
                }
            }

            if (!found) {
                questionList.add(question);
                logger.info("Question added to database: " + question.getCategory() + " with text: " + question.getQuestionText());
            }

            XMLStorageController.saveQuestionsToXML(fileName, questionList);
            logger.info("Question database updated successfully.");
            return new Response(true, "Question database updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error updating: " + e.getMessage());
            return new Response(false, "Error updating question database: " + e.getMessage(), null);
        }
    }

    private Response handleLeaderboardUpdateRequest(LeaderboardEntryModel leaderboardEntry) {
        try {
            logger.info("Server received request for removal of entry in leaderboard");
            if (leaderboardEntry == null) {
                logger.severe("Received null leaderboard entry data.");
                return new Response(false, "Received null leaderboard entry data.", null);
            }

            fileName = "data/classic_leaderboard.xml";
            List<LeaderboardEntryModel> leaderboard = XMLStorageController.loadLeaderboardFromXML(fileName);

            if (leaderboardEntry.getPlayerName().endsWith("  ")) {
                fileName = "data/endless_leaderboard.xml";
                leaderboard = XMLStorageController.loadLeaderboardFromXML(fileName);
            }
            for (LeaderboardEntryModel entry : leaderboard) {
                if (leaderboardEntry.equals(entry)) {
                    logger.info("Removed entry from leaderboard: " + entry.getPlayerName() + " with score: " + entry.getScore());
                    leaderboard.remove(entry);
                    break;
                }
            }
            XMLStorageController.saveLeaderboardToXML(fileName, leaderboard);
            logger.info("Leaderboard updated successfully.");
            return new Response(true, "Leaderboard updated successfully.", null);
        } catch (Exception e) {
            logger.severe("Error removing entry from leaderboard: " + e.getMessage());
            return new Response(false, "Error removing entry from leaderboard: " + e.getMessage(), null);
        }
    }

    // Closes the connection to the client (streams and socket)
    private void closeConnection() {
        try {
            if (objectInputStream != null) objectInputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            logger.info("Connection closed.");
        } catch (IOException e) {
            logger.severe("Error closing connection: " + e.getMessage());
        }
    }
}
