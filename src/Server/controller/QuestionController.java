package Server.controller;

import common.model.QuestionModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for getting questions based on category
 */
public class QuestionController {
    /**
     * Returns a list of questions for a certain category
     */
    public List<QuestionModel> getQuestionsByCategory(String category) {
        List<QuestionModel> allQuestions = JSONStorageController.loadQuestionsFromJSON();

        return allQuestions.stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
    /**
     * Returns a list that contains all the questions in the database
     */
    public List<QuestionModel> getQuestionsList() {
        return JSONStorageController.loadQuestionsFromJSON();
    }
}
