package com.psp.service;

import com.psp.model.Project;
import com.psp.repository.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    ProjectDao projectDao;

    public Project saveProject(Project project){
        return projectDao.saveProject(project);
    }
    public boolean deleteProjectById(long id){
        return projectDao.deleteProjectById(id);
    }
    public List<Project> getProjectList(){
        return projectDao.getProjectList();
    }
    public Project updateProject(Project project){
        return projectDao.updateProject(project);
    }
    public Project getProjectById(long id){
        return projectDao.getProjectById(id);
    }
    public List<Project> getProjectsAndInfo(){return projectDao.getProjectsAndInfo();}
    public List<Project> getProjectsAndInfoById(long id){return projectDao.getProjectsAndInfoById(id);}
}
