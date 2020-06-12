package com.psp.repository;

import com.psp.model.RateStar;
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
public class RateStarDaoImpl implements RateStarDao{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public RateStar saveRate(RateStar rate) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(rate);
            transaction.commit();
            return rate;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteRateById(long rateId) {
        //TODO: How to solve the read line of RateStar
        String hql = "DELETE  RateStar as r where r.id = :rId";
        int deletedCount = 0;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Query<RateStar> query = session.createQuery(hql);
            query.setParameter("rId", rateId);
            // 多少记录被改变
            deletedCount = query.executeUpdate();
            transaction.commit();
        }
        catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(String.format("Failure to delete rate record", rateId), e.getMessage(), e);
        }
        //%d = rateId
        logger.debug(String.format("The RateStar %d was deleted", rateId));
        return deletedCount >= 1;
    }

    @Override
    public List<RateStar> getRateList() {
        String hql = "From RateStar";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RateStar> query = session.createQuery(hql);
            return query.list();
        }
        catch (Exception e){
            //打印error message
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public RateStar updateRate(RateStar rate) {
        String msg = String.format("The rate %s was updated.", rate.toString());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(rate);
            transaction.commit();
            return rate;
        }
        catch (Exception e) {
            msg = e.getMessage();
            if (transaction != null) {
                transaction.rollback();
            }
            //TODO:suggest to add finally instead of write debug out of catch exception
        } finally {
            logger.debug(msg);
        }
        return null;
    }

    @Override
    public RateStar getRateById(long rateId) {
        String hql = "FROM RateStar where id = :rateId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<RateStar> query = session.createQuery(hql);
            query.setParameter("rateId", rateId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
