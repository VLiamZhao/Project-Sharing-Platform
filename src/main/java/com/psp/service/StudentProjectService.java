package com.psp.service;

import com.psp.model.StudentProject;
import com.psp.repository.StudentProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StudentProjectService {
    @Autowired
    StudentProjectDao studentProjectDao;

    public List<StudentProject> getStudentProjectListByStudentId(long id){
        return studentProjectDao.getStudentProjectListByStudentId(id);
    }

    public List<StudentProject> getStudentProjectListByStudentUserName(String username){
        return studentProjectDao.getStudentProjectListByStudentUserName(username);
    }

    public List<StudentProject> getStudentProjectListByProjectId(long id) {
        return studentProjectDao.getStudentProjectListByProjectId(id);
    }

    public StudentProject getStudentProjectById(long id){
        return studentProjectDao.getStudentProjectById(id);
    }
    public StudentProject save(StudentProject studentProject){
        return studentProjectDao.save(studentProject);
    }
    public boolean deleteStudentProjectById(long id){
        return studentProjectDao.deleteStudentProjectById(id);
    }
    public List<StudentProject> getStudentProjectList(){
        return studentProjectDao.getStudentProjectList();
    }
    public StudentProject updateStudentProject(StudentProject studentProject){
        return studentProjectDao.updateStudentProject(studentProject);
    }

    public StudentProject getStudentProjectByStudentIdAndProjectId(long stuId, long proId){
        return studentProjectDao.getStudentProjectByStudentIdAndProjectId(stuId, proId);
    }
}
