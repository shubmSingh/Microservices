package com.shubham.quizservice.feign;

//import com.shubham.quizservice.Fallback.QuizInterfaceFallback;

import com.shubham.quizservice.model.QuestionWrapper;
import com.shubham.quizservice.model.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(value = "QUESTION-SERVICE", fallbackFactory = QuizInterfaceFallback.class)
@FeignClient(value = "QUESTION-SERVICE")
public interface QuizInterface {
    @GetMapping("/question-service/question/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category, @RequestParam int limit);

    @PostMapping("/question-service/question/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> ids);

    @PostMapping("/question-service/question/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses);
}
