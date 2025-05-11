//package com.shubham.quizservice.Fallback;
//
//import com.shubham.quizservice.feign.QuizInterface;
//import com.shubham.quizservice.model.QuestionWrapper;
//import com.shubham.quizservice.model.Response;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class QuizInterfaceFallback implements QuizInterface {
//
//    @Override
//    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, int limit) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> ids) {
//        System.out.println("Fallback: Returning default questions as the Question Service is down.");
//
//        // Return default questions
//        List<QuestionWrapper> defaultQuestions = Arrays.asList(
//                new QuestionWrapper(9991, "What is the capital of France?", "Berlin", "Madrid", "Paris", "Rome"),
//                new QuestionWrapper(9992, "Which programming language is mainly used in Android development?", "Swift", "Kotlin", "JavaScript", "C#")
//        );
//
//        return ResponseEntity.ok(defaultQuestions);
//    }
//
//    @Override
//    public ResponseEntity<Integer> getScore(List<Response> responses) {
//        return null;
//    }
//}
//
