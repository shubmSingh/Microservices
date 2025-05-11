package com.shubham.quizservice.service;

import com.shubham.quizservice.dao.QuizDao;
import com.shubham.quizservice.feign.QuizInterface;
import com.shubham.quizservice.model.QuestionWrapper;
import com.shubham.quizservice.model.Quiz;
import com.shubham.quizservice.model.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int limit, String title) {
        List<Integer> ids = quizInterface.getQuestionsForQuiz(category, limit).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(ids);
        quizDao.save(quiz);

        return new ResponseEntity<>("Quiz created successfully", HttpStatus.OK);
    }

    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "getQuizQuestionsFallback")
    @Retry(name = "quizRetry")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> ids = quiz.getQuestionIds();
        log.info("ids is:{}", ids);
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(ids);
        return questions;
    }

    // ✅ Fallback Method (Must match parameters + Throwable)
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestionsFallback(Integer quizId, Throwable t) {
        System.out.println("Fallback: Returning default questions as the Question Service is down.");

        // Return default questions
        List<QuestionWrapper> defaultQuestions = Arrays.asList(
                new QuestionWrapper(9991, "Fallback Triggered?", "Berlin", "Madrid", "Paris", "Rome"),
                new QuestionWrapper(9992, "Which programming language is mainly used in Android development?", "Swift", "Kotlin", "JavaScript", "C#")
        );

        return ResponseEntity.ok(defaultQuestions);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }
}
