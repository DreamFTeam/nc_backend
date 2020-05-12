package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoEditQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuizFilter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface QuizService {

    Quiz saveQuiz(DtoQuiz newQuiz, String currentUserId, MultipartFile image) throws ValidationException;

    Quiz updateQuiz(DtoEditQuiz quiz, MultipartFile image, boolean newImage);

    Quiz getQuiz(String quizId);

    Quiz getQuiz(String quizId, String userId);

    void markAsFavourite(DtoQuiz quiz);

    void markAsPublished(DtoQuiz quiz);

    void validateQuiz(DtoQuiz quiz);

    void deleteQuizById(DtoQuiz dtoQuiz);

    void deactivateQuiz(DtoQuiz dtoQuiz);

    Question saveQuestion(Question newQuestion, MultipartFile image);

    Question updateQuestion(Question newQuestion, MultipartFile image);

    void deleteQuestion(Question question);

    List<Question> getQuestionList(String quizId);

    List<Question> getQuestionsInPage(int startIndex, int amount , String quizId);

    List<Map<String, String>> getTagList();

    List<List<Object>> getCategoryList();

    List<Quiz> getUserQuizList(String userId, String thisUserId);

    List<QuizView> getQuizzes(int startIndex, int amount);

    List<QuizValid> getInvalidQuizzes(int startIndex, int amount, String adminId);

    List<QuizValid> getValidQuizzes(int startIndex, int amount, String adminId);

    List<QuizFiltered> findQuizzesByFilter(DtoQuizFilter quizFilter, int startIndex, int amount);

    List<QuizFiltered> shortListOfQuizzes();

    List<QuizView> getSuggestionsQuizList(String userId, int amount);

    int getQuestionsAmountInQuiz(String quizId);

    int getQuizzesTotalSize();

    int getInvalidQuizzesTotalSize();

    int getValidQuizzesTotalSize(String adminId);

    Quiz setValidator(String quizId, String adminId);

}
