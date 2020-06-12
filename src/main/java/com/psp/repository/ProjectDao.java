package com.psp.repository;

import com.psp.model.Project;
import com.psp.model.User;

import java.util.List;

public interface ProjectDao {
    Project saveProject(Project project);
    boolean deleteProjectById(long id);
    List<Project> getProjectList();
    Project updateProject(Project project);
    Project getProjectById(long id);
    List<Project> getProjectsAndInfo();
    List<Project> getProjectsAndInfoById(long id);
}
