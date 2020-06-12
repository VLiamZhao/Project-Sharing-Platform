package com.psp.repository;

import com.psp.model.Resume;
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
public class ResumeDaoImpl implements ResumeDao{
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public Resume save(Resume resume) {
        return null;
    }

    @Override
    public boolean deleteResumeInfoById(long id) {
        String hql = "DELETE Resume as u where u.id = :uId";
        int deletedCount = 0;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Query<Resume> query = session.createQuery(hql);
            query.setParameter("uId", id);
            deletedCount = query.executeUpdate();
            transaction.commit();
        }
        catch (Exception e){
            if (transaction != null) transaction.rollback();
            logger.error(String.format("Failure to delete record", id), e.getMessage(), e);
        }
        logger.debug(String.format("The Resume %s was deleted", String.valueOf(id)));
        return deletedCount >= 1;
    }

    @Override
    public List<Resume> getAllResume() {
        String hql = "From Resume";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Resume> query = session.createQuery(hql);
            return query.list();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Resume updateResume(Resume resume) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(resume);
            transaction.commit();
            return resume;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            if (transaction != null) transaction.rollback();
        }
        return null;
    }

    @Override
    public Resume getResumeByUserId(long id) {
        String hql = "FROM Resume where id = :targetId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Resume> query = session.createQuery(hql);
            query.setParameter("targetId", id);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
