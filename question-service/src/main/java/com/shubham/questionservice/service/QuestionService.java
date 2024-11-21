package com.shubham.questionservice.service;

import com.shubham.questionservice.dao.QuestionDao;
import com.shubham.questionservice.model.Questions;
import com.shubham.questionservice.model.QuestionWrapper;
import com.shubham.questionservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Questions>> getAllQuestions() {
       try {
           List<Questions> questions = questionDao.findAll();
           if (questions.isEmpty()) {
               return new ResponseEntity<>(HttpStatus.NO_CONTENT);
           }
           return new ResponseEntity<>(questions, HttpStatus.OK);
       } catch (Exception e) {
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    public ResponseEntity<List<Questions>> getQuestionsByCategory(String category) {
        try{
            List<Questions> questions = questionDao.findByCategory(category);
            if (questions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> addQuestion(Questions questions) {
        try {
            questionDao.save(questions);
            return new ResponseEntity<>("Question added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Question could not be added", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Integer>> getQuestionsFromQuiz(String category, Integer limit) {
        try {
            List<Integer> questions = questionDao.findRandomQuestionsByCategory(category, limit);
            if (questions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> ids) {
       List<QuestionWrapper> wrapperList = new ArrayList<>();
       List <Questions> questions = new ArrayList<>();

       for (Integer id : ids) {
           questions.add(questionDao.findById(id).get());
       }

       for (Questions question : questions) {
           QuestionWrapper wrapper = new QuestionWrapper();
              wrapper.setId(question.getId());
              wrapper.setQuestionTitle(question.getQuestionTitle());
              wrapper.setOption1(question.getOption1());
              wrapper.setOption2(question.getOption2());
              wrapper.setOption3(question.getOption3());
              wrapper.setOption4(question.getOption4());
              wrapperList.add(wrapper);
       }
         return new ResponseEntity<>(wrapperList, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int score = 0;
        for (Response response : responses) {
            Questions questions = questionDao.findById(Integer.parseInt(response.getId())).get();
            if (questions.getCorrectAnswer().equals(response.getResponse())) {
                score++;
            }
        }
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
