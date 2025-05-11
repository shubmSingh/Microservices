package com.shubham.questionservice.dao;

import com.shubham.questionservice.model.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionDao extends JpaRepository<Questions, Integer> {

    List<Questions> findByCategory(String category);

    @Query(value = "SELECT q.id FROM question AS q Where q.category=:category ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(String category, int limit);
}
