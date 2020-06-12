package com.psp.repository;

import com.psp.model.Project;
import com.psp.model.StudentProject;
import com.psp.model.User;
import com.psp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class StudentProjectDaoImpl implements StudentProjectDao {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<StudentProject> getStudentProjectListByStudentId(long id) {
        String hql = "FROM StudentProject as sp left join fetch sp.project left join fetch sp.studentUser as u where u.id = :targetId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(hql);
            query.setParameter("targetId", id);
            User user = query.uniqueResult();
            List<StudentProject> studentProjects = new ArrayList<>(user.getStudentProjects());
            if (user.getUserType().equals("student")){
                return studentProjects;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<StudentProject> getStudentProjectListByStudentUserName(String username) {
        String hql = "FROM User as u left join fetch u.studentProjects where u.username = :targetName";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(hql);
            query.setParameter("targetName", username);
            User user = query.uniqueResult();
            List<StudentProject> studentProjects = new ArrayList<>(user.getStudentProjects());
            if (user.getUserType().equals("student")){
                return studentProjects;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<StudentProject> getStudentProjectListByProjectId(long id) {
        String hql = "FROM Project as p left join fetch p.studentProjects where p.id = :targetId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Project> query = session.createQuery(hql);
            query.setParameter("targetId", id);
            Project project = query.uniqueResult();
            List<StudentProject> studentProjects = new ArrayList<>(project.getStudentProjects());
            return studentProjects;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public StudentProject getStudentProjectById(long id) {
        String hql = "FROM StudentProject where id = :targetId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<StudentProject> query = session.createQuery(hql);
            query.setParameter("targetId", id);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public StudentProject save(StudentProject studentProject) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(studentProject);
            transaction.commit();
            return studentProject;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteStudentProjectById(long id) {
        String hql = "DELETE StudentProject as s where s.id = :uId";
        int deletedCount = 0;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Query<StudentProject> query = session.createQuery(hql);
            query.setParameter("uId", id);
            deletedCount = query.executeUpdate();
            transaction.commit();
        }
        catch (Exception e){
            if (transaction != null) transaction.rollback();
            logger.error(e.getMessage());
        }
        logger.debug(String.format("The StudentProject %s was deleted", String.valueOf(id)));
        return deletedCount >= 1;
    }

    @Override
    public List<StudentProject> getStudentProjectList() {
        String hql = "From StudentProject";
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<StudentProject> query = session.createQuery(hql);
            return query.list();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public StudentProject updateStudentProject(StudentProject studentProject) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(studentProject);
            transaction.commit();
            return studentProject;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            if (transaction != null) transaction.rollback();
        }
        return null;
    }

    @Override
    public StudentProject getStudentProjectByStudentIdAndProjectId(long stuId, long proId) {
        String hql = "FROM StudentProject as p where p.studentUser.id = :studentId and p.project.id = :projectId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<StudentProject> query = session.createQuery(hql);
            query.setParameter("studentId", stuId);
            query.setParameter("projectId", proId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
