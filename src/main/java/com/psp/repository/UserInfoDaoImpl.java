package com.psp.repository;

import com.psp.model.User;
import com.psp.model.UserInfo;
import com.psp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class UserInfoDaoImpl implements UserInfoDao{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserInfo save(UserInfo userInfo) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(userInfo);
            transaction.commit();
            return userInfo;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteUserInfoById(long id) {
        String hql = "DELETE UserInfo as ui where ui.id = :uId";
        int deletedCount = 0;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Query<UserInfo> query = session.createQuery(hql);
            query.setParameter("uId", id);
            deletedCount = query.executeUpdate();
            transaction.commit();
        }
        catch (Exception e){
            if (transaction != null) transaction.rollback();
            logger.error(e.getMessage());
        }
        logger.debug(String.format("The UserInfo with Id %s was deleted", String.valueOf(id)));
        return deletedCount >= 1;
    }


    @Override
    public List<UserInfo> getAllUserInfo() {
        String hql = "From UserInfo";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserInfo> query = session.createQuery(hql);
            return query.list();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
    String msg = String.format("The userInfo of user %s was updated.", userInfo.getFirstName() +
            " " + userInfo.getLastName());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(userInfo);
            transaction.commit();
            return userInfo;
        }
        catch (Exception e) {
            msg = e.getMessage();
            if (transaction != null) transaction.rollback();
        }
        logger.debug(msg);
        return null;
    }

    @Override
    public UserInfo getUserInfoByUserId(long id) {
        String hql = "FROM User as u left join fetch u.userInfo where u.id = :targetId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(hql);
            query.setParameter("targetId", id);
            return query.uniqueResult().getUserInfo();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String getUserIconUrlById(long id) {
        String hql = "FROM User as u left join fetch u.userInfo where u.id = :targetId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(hql);
            query.setParameter("targetId", id);
            return query.uniqueResult().getUserInfo().getIconUrl();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

}
