package com.psp.service;

import com.psp.model.RecommendationLetter;
import com.psp.model.User;
import com.psp.repository.RecommendationLetterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RecommendationLetterService {
    @Autowired
    private RecommendationLetterDao recommendationLetterDao;

    public RecommendationLetter save (RecommendationLetter recommendationLetter){
        return recommendationLetterDao.save(recommendationLetter);
    }
    public boolean deleteRecommendationLetterById(long id){
        return recommendationLetterDao.deleteRecommendationLetterById(id);
    }
    public RecommendationLetter getRecommendationLetterById(long id){
        return recommendationLetterDao.getRecommendationLetterById(id);
    }
    public List<RecommendationLetter> getAllRecommendationLetters(){
        return recommendationLetterDao.getAllRecommendationLetters();
    }
    public RecommendationLetter updateRecommendationLetter(RecommendationLetter recommendationLetter){
        return recommendationLetterDao.updateRecommendationLetter(recommendationLetter);
    }

    public RecommendationLetter getLetterByUserId(User user, long stuId) {
        return recommendationLetterDao.getLetterByUserId(user, stuId);
    }
}