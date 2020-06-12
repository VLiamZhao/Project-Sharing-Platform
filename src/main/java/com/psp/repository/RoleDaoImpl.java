package com.psp.repository;

import com.psp.model.Role;
import com.psp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public Role getRoleByName(String name) {
        String hql = "FROM Role as r where r.name = :targetName";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Role> query = session.createQuery(hql);
            query.setParameter("targetName", name);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Role getRoleById(long id) {
        String hql = "FROM Role as r where r.id = :targetId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Role> query = session.createQuery(hql);
            query.setParameter("targetId", id);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Role save(Role role) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(role);
            transaction.commit();
            return role;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteRoleById(long id) {
        String hql = "DELETE Role as r where r.id = :rId";
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
        logger.debug(String.format("The Role %s was deleted", id));
        return deletedCount >= 1;
    }

    @Override
    public List<Role> getRoles() {
        String hql = "From Role";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Role> query = session.createQuery(hql);
            return query.list();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Role updateRole(Role role) {
        String msg = String.format("The role %s was updated.", role.getName());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(role);
            transaction.commit();
            return role;
        }
        catch (Exception e) {
            msg = e.getMessage();
            if (transaction != null) transaction.rollback();
        }
        logger.debug(msg);
        return null;
    }


}
