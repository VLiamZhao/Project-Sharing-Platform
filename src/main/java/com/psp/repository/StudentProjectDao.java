package com.psp.repository;

import com.psp.model.Project;
import com.psp.model.Role;
import com.psp.model.StudentProject;
import com.psp.model.User;

import java.util.List;

public interface StudentProjectDao {
    List<StudentProject> getStudentProjectListByStudentId(long id);
    List<StudentProject> getStudentProjectListByStudentUserName(String username);
    List<StudentProject> getStudentProjectListByProjectId(long id);
    StudentProject getStudentProjectById(long id);
    StudentProject save(StudentProject studentProject);
    boolean deleteStudentProjectById(long id);
    List<StudentProject> getStudentProjectList();
    StudentProject updateStudentProject(StudentProject studentProject);
    StudentProject getStudentProjectByStudentIdAndProjectId(long stuId, long proId);
}
