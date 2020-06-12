package com.psp.controller;

import com.psp.exception.ApiRequestException;
import com.psp.model.Project;
import com.psp.model.StudentProject;
import com.psp.model.User;
import com.psp.service.ProjectService;
import com.psp.service.StudentProjectService;
import com.psp.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
@RequestMapping(value = {"/projects"})
public class ProjectController {
    @Autowired
    UserService userService;
    @Autowired
    StudentProjectService studentProjectService;
    @Autowired
    ProjectService projectService;


    /**
     * GET {prefix}/projects/all
     * @param
     * @return List<Project>
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Project>> getAllProjects(){
        List<Project> proList = projectService.getProjectList();
        return ResponseEntity.ok().body(proList);
    }
    /**
     * GET {prefix}/projects
     * @param id
     * @return Project
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getProjectById(@RequestParam("id") long id){
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User user = project.getCompanyUser();
        Map<String, Object> res = new HashMap<>();

        res.put("project", project);
        res.put("companyName", user.getUsername());
        res.put("companyID", user.getId());
        res.put("companyRate", user.getReceiveStars());
        return ResponseEntity.ok().body(res);
    }
}