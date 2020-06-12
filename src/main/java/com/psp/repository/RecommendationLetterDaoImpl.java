package com.psp.repository;

import com.psp.model.RecommendationLetter;
import com.psp.model.Role;
import com.psp.model.User;
import com.psp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecommendationLetterDaoImpl implements RecommendationLetterDao {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public RecommendationLetter save (RecommendationLetter recommendationLetter) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(recommendationLetter);
            transaction.commit();
            session.close();
            return recommendationLetter;
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (transaction!=null) transaction.rollback();
        }
        return null;
    }

    @Override
    public boolean deleteRecommendationLetterById(long id) {
        String hql = "DELETE RecommendationLetter as r where r.id = :rId";
        int deletedCount = 0;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Query<Role> query = session.createQuery(hql);
            query.setParameter("rId", id);
            deletedCount = query.executeUpdate();
            transaction.commit();
        }
        catch (Exception e){
            if (transaction != null) transaction.rollback();
            logger.error(String.format("Failure to delete record", id), e.getMessage(), e);
        }
        logger.debug(String.format("The Recommendation %s was deleted", String.valueOf(id)));
        return deletedCount >= 1;
    }

    @Override
    public RecommendationLetter getRecommendationLetterById(long id) {
        String hql = "FROM RecommendationLetter as r where r.id = :targetId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RecommendationLetter> query = session.createQuery(hql);
            query.setParameter("targetId", id);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<RecommendationLetter> getAllRecommendationLetters() {
        String hql = "From RecommendationLetter";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RecommendationLetter> query = session.createQuery(hql);
            return query.list();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public RecommendationLetter updateRecommendationLetter(RecommendationLetter recommendationLetter) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(recommendationLetter);
            transaction.commit();
            return recommendationLetter;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            if (transaction != null) transaction.rollback();
        }
        return null;
    }

    @Override
    public RecommendationLetter getLetterByUserId(User company, long stuId) {
        String hql = "From RecommendationLetter as r where r.studentUserId = :sId and r.companyUser = :company and r.status = 'valid' ";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RecommendationLetter> query = session.createQuery(hql);
            query.setParameter("sId", stuId);
            query.setParameter("company", company);
            System.out.println(query.toString());
            return query.uniqueResult();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

}
