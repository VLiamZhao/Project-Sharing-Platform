package com.psp.repository;

import com.psp.model.RecommendationLetter;
import com.psp.model.User;

import java.util.List;

public interface RecommendationLetterDao {
    RecommendationLetter save (RecommendationLetter recommendationLetter);
    boolean deleteRecommendationLetterById(long id);
    RecommendationLetter getRecommendationLetterById(long id);
    List<RecommendationLetter> getAllRecommendationLetters();
    RecommendationLetter updateRecommendationLetter(RecommendationLetter recommendationLetter);
    RecommendationLetter getLetterByUserId(User company, long stuId);
}
