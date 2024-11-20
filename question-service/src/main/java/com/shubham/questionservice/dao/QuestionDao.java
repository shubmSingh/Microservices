package com.shubham.questionservice.dao;

import com.shubham.questionservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionDao extends JpaRepository<Question, Integer> {

    List<Question> findByCategory(String category);

    @Query(value = "SELECT q.id FROM question AS q Where q.category=:category ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(String category, int limit);
}
