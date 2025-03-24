package Redundant;

import common.model.QuestionModel;
import java.util.*;

/**
 * Represents the database of questions
 */
public class QuestionBankModel {
    // use synchronized list for thread
    private List<QuestionModel> questions;

    public QuestionBankModel() {
        this.questions = Collections.synchronizedList(XMLStorageController.loadQuestionsFromXML("data/questions.xml"));
    }

    /**
     * Returns a list of questions
     */
    public List<QuestionModel> getQuestions() {
        // sync access to the questions list
        synchronized (questions) {
            return new ArrayList<>(questions);
        }
    }
}