package com.psp.repository;
import com.psp.model.Project;
import com.psp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProjectDaoImpl implements ProjectDao{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Project saveProject(Project project) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(project);
            transaction.commit();
            return project;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();

            }
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteProjectById(long projectId) {
        //TODO: How to solve the read line of RateStar
        // not influence the running
        String hql = "DELETE  Project as p where p.id = :pId";
        int deletedCount = 0;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Query<ProjectDao> query = session.createQuery(hql);
            query.setParameter("pId", projectId);
            deletedCount = query.executeUpdate();
            transaction.commit();
        }
        catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(String.format("Failure to delete project record", projectId), e.getMessage(), e);
        }
        //%d = rateId
        logger.debug(String.format("The RateStar %d was deleted", projectId));
        return deletedCount >= 1;
    }

    @Override
    public List<Project> getProjectList() {
        String hql = "From Project";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Project> query = session.createQuery(hql);
            return query.list();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Project updateProject(Project project) {
        String msg = String.format("The rate %s was updated.", project.toString());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(project);
            transaction.commit();
            return project;
        }
        catch (Exception e) {
            msg = e.getMessage();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            logger.debug(msg);
        }
        return null;
    }

    @Override
    public Project getProjectById(long projectId) {
        String hql = "FROM Project where id = :pId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Project> query = session.createQuery(hql);
            query.setParameter("pId", projectId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Project> getProjectsAndInfo(){
        String hql = "From Project as p left join fetch p.studentProjects";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Project> query = session.createQuery(hql);
            return query.list();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Project> getProjectsAndInfoById(long id) {
        String hql = "From Project as p left join fetch p.studentProjects where student_id =: uid";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Project> query = session.createQuery(hql);
            query.setParameter("uid", id);
            return query.list();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }

    }

}
