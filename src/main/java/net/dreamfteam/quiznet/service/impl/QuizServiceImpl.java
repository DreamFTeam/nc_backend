package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.Quiz;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.QuizService;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private QuizDao quizDao;

    @Autowired
    public QuizServiceImpl(QuizDao quizDao) {
        this.quizDao = quizDao;
    }

    @Override
    public Quiz saveQuiz(DtoQuiz newQuiz) throws ValidationException {
        checkQuizUniqueness(newQuiz);
        Quiz quiz = Quiz.builder()
                .title(newQuiz.getTitle())
                .creationDate(Calendar.getInstance().getTime())
                .creatorId(newQuiz.getCreatorId())
                .language(newQuiz.getLanguage())
                .description(newQuiz.getDescription())
                .imageRef(newQuiz.getImageRef())
                .validated(false)
                .activated(false)
                .isFavourite(false)
                .tagList(newQuiz.getTagList())
                .categoryList(newQuiz.getCategoryList())
                .build();

        quiz = quizDao.saveQuiz(quiz);

        System.out.println("Saved in db:" + quiz.toString());
        return quiz;
    }

    @Override
    public Quiz updateQuiz(DtoQuiz quiz) {
        checkQuizUniqueness(quiz);
        return quizDao.updateQuiz(quiz);
    }

    @Override
    public Quiz getQuiz(DtoQuiz quiz) {
        return quizDao.getQuiz(quiz);
    }

    @Override
    public void markAsFavourite(DtoQuiz quiz) {
        quizDao.markAsFavourite(quiz);
    }

    @Override
    public Question saveQuestion(Question newQuestion) {
        newQuestion.setId(quizDao.saveQuestion(newQuestion));
        switch(newQuestion.getTypeId()) {
            case (1): // One of four options question, can be multiple right answers
                quizDao.saveFirstTypeAns(newQuestion);
                return(newQuestion);
            case (2): // True-false question
            case (3):
                quizDao.saveSecondThirdTypeAns(newQuestion);
                return(newQuestion);
            case (4):
                quizDao.saveFourthTypeAns(newQuestion);
                return(newQuestion);
            default:
                throw new ValidationException("Question type should be in range of [1 - 4]");
        }
    }

    @Override
    public Question updateQuestion(Question newQuestion) {
        quizDao.deleteQuestion(newQuestion);
        newQuestion.setId(quizDao.saveQuestion(newQuestion));
        return newQuestion;
    }

    @Override
    public void deleteQuestion(Question question) {
        quizDao.deleteQuestion(question);
    }

    @Override
    public List<Question> getQuestionList(Question question) {
        return quizDao.getQuestionList(question);
    }


    private void checkQuizUniqueness(DtoQuiz quiz) {
        if(quizDao.getUserQuizByTitle(quiz.getTitle(), quiz.getCreatorId()) != null) {
            throw new ValidationException("Quiz with current name already exist for this user");
        }
    }
}
