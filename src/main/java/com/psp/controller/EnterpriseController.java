package com.psp.controller;

import com.psp.exception.ApiRequestException;
import com.psp.model.*;
import com.psp.repository.RecommendationLetterDaoImpl;
import com.psp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
@RequestMapping(value = {"/enterprises"})
public class EnterpriseController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private StudentProjectService studentProjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AzureService azureService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private RateStarService rateStarService;

    @Autowired
    private RecommendationLetterService recommendationLetterService;


    /**
     * PUT {prefix}/enterprises/projects/approval
     * @param projectId, studentId, request
     * @return ResponseEntity<Project>
     */
    @RequestMapping(value = "/projects/approval", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, String>> approveApplication(@RequestParam("projectId") long projectId, @RequestParam("studentId") long studentId, HttpServletRequest request){
        // get current userId
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        User comUser = userService.getUserById(comId);
        // get project of the application to be approved
        Project project = projectService.getProjectById(projectId);
        // check if the user owns the project
        if (!(comUser.getUsername()).equals(project.getCompanyUser().getUsername())){
            throw new ApiRequestException("You are not authorized for this project!");
        }
        // get the data corresponding with the student and the project
        StudentProject sp = studentProjectService.getStudentProjectByStudentIdAndProjectId(studentId, projectId);
        if(sp.getStatus().equals("approved")){
            throw new ApiRequestException("You have already permitted the application!");
        }
        sp.setStatus("approved");
        // update the data
        studentProjectService.updateStudentProject(sp);
        Map<String, String> map = new HashMap<>();
        map.put("Status", "Succeed!");
        return ResponseEntity.ok().body(map);
    }

    /**
     * PUT {prefix}/enterprises/projects/rejection
     * @param projectId, studentId, request
     * @return ResponseEntity<Project>
     */
    @RequestMapping(value = "/projects/rejection", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, String>> rejectApplication(@RequestParam("projectId") long projectId, @RequestParam("studentId") long studentId, HttpServletRequest request){
        // get current userId
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        User comUser = userService.getUserById(comId);
        // get project of the application to be approved
        Project project = projectService.getProjectById(projectId);
        // check if the user owns the project
        if (!(comUser.getUsername()).equals(project.getCompanyUser().getUsername())){
            throw new ApiRequestException("You are not authorized for this project!");
        }
        // get the data corresponding with the student and the project
        StudentProject sp = studentProjectService.getStudentProjectByStudentIdAndProjectId(studentId, projectId);
        if(sp.getStatus().equals("rejected")){
            throw new ApiRequestException("You have already rejected the application!");
        }
        sp.setStatus("rejected");
        // update the data
        studentProjectService.updateStudentProject(sp);
        Map<String, String> map = new HashMap<>();
        map.put("Status", "Rejection Succeed!");
        return ResponseEntity.ok().body(map);
    }

    /**
     * POST {prefix}/enterprises/projects/upload
     * @param file
     * @return ResponseEntity<Project>
     */
    @RequestMapping(value = "/projects/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Project> uploadProject(@RequestParam("file") MultipartFile file, @RequestParam("projectName") String projectName, @RequestParam("description") String description, HttpServletRequest request){
        if (file == null){
            throw new ApiRequestException("Invalid File! please upload again!");
        }
        URI url = azureService.upload(file);
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        User comUser = userService.getUserById(comId);
        Project project = new Project(url.toString(), file.getOriginalFilename(), ZonedDateTime.now());
        project.setProjectName(projectName);
        project.setDescription(description);
        project.setCompanyUser(comUser);
        project.setStatus("valid");
        projectService.saveProject(project);
        return ResponseEntity.ok().body(project);
    }

    /**
     * PATCH {prefix}/enterprises/projects/deletion
     * @param id
     * @return ResponseEntity<Project>
     */
    @RequestMapping(value = "/projects/deletion", method = RequestMethod.PATCH, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Project> deleteProject(@RequestParam("id") long id){
        if(id <= 0){
            throw new ApiRequestException("Invalid Id! please input again!");
        }
        Project p = projectService.getProjectById(id);
        p.setStatus("deleted");
        projectService.updateProject(p);
        return ResponseEntity.ok().body(p);
    }

    /**
     * PUT {prefix}/enterprises/projects/update
     * @param projectId, project
     * @return ResponseEntity<Project>
     */
    @RequestMapping(value = "/projects/update", method = RequestMethod.PATCH, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Project> updateProject(@RequestParam("projectId") long projectId, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("projectName") String projectName, @RequestParam("description") String description, HttpServletRequest request){

        Project p = projectService.getProjectById(projectId);
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        User comUser = userService.getUserById(comId);
        if (!comUser.getUsername().equals(p.getCompanyUser().getUsername())){
            throw new ApiRequestException("You are not authorized to proceed!");
        }

        p.setProjectName(projectName);
        p.setDescription(description);
        if (file != null) {
            URI url = azureService.upload(file);
            p.setProjectUrl(url.toString());
        }
        projectService.updateProject(p);
        return ResponseEntity.ok().body(p);
    }

    /**
     * POST {prefix}/enterprises/info/icon
     * @param icon
     * @return ResponseEntity<String>
     */
    @RequestMapping(value = "/info/icon", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCompanyIcon(@RequestParam("icon") MultipartFile icon, HttpServletRequest request){
        if (icon == null){
            throw new ApiRequestException("Invalid File! please upload again!");
        }
        URI url = azureService.upload(icon);
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        UserInfo userInfo = userInfoService.getUserInfoByUserId(comId);
        userInfo.setIconUrl(url.toString());
        userInfoService.updateUserInfo(userInfo);
        return ResponseEntity.ok().body(url.toString());
    }

    /**
     * GET {prefix}/enterprises/info/icon
     * @param
     * @return ResponseEntity<String>
     */
    @RequestMapping(value = "/info/icon", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getCompanyIcon(HttpServletRequest request){
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        UserInfo userInfo = userInfoService.getUserInfoByUserId(comId);
        String url = userInfo.getIconUrl();
        return ResponseEntity.ok().body(url);
    }

    /**
     * GET {prefix}/enterprises/ratestar
     * @param
     * @return ResponseEntity<String>
     */
    @RequestMapping(value = "/ratestar", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> getCompanyRateStar(@RequestParam("id") long id, HttpServletRequest request){
//        HttpSession session = request.getSession();
//        long comId = (long)session.getAttribute("curUser");
        User companyUser = userService.getUserById(id);
        Set<RateStar> ratestars = companyUser.getReceiveStars();
        int stars = 0;
        for (RateStar rs : ratestars){
            stars += rs.getStar();
        }
        Map<String, String> map = new HashMap<>();
        if (ratestars.size() == 0) {
            map.put("RateStar", String.valueOf(0));
            return ResponseEntity.ok().body(map);
        }
        stars /= ratestars.size();
        map.put("RateStar", String.valueOf(stars));
        return ResponseEntity.ok().body(map);
    }

    /**
     * GET {prefix}/enterprises/projects/{projectId}
     * @param projectId, request
     * @return List<User>
     */
    @RequestMapping(value = "/projects/students", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Map<String, Object>>> getAppliedStudentsOfProject(@RequestParam("projectId") long projectId, HttpServletRequest request){
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        User comUser = userService.getUserById(comId);
        // get project of the application to be approved
        Project project = projectService.getProjectById(projectId);
        // check if the user owns the project
        if (!(comUser.getUsername()).equals(project.getCompanyUser().getUsername())){
            throw new ApiRequestException("You are not authorized for this project!");
        }
        List<StudentProject> spList = studentProjectService.getStudentProjectListByProjectId(projectId);
        List<Map<String, Object>> students = new ArrayList<>();
        for (StudentProject sp : spList){
            Map<String, Object> map = new HashMap<>();
            RecommendationLetter rl = recommendationLetterService.getLetterByUserId(comUser, sp.getStudentUser().getId());
            map.put("student", sp.getStudentUser().getUserInfo());
            map.put("status", sp.getStatus());
            map.put("student_id", sp.getStudentUser().getId());
            map.put("student_phone", sp.getStudentUser().getPhoneNum());
            if (sp.getStudentUser().getResume() != null) {
                map.put("resume", sp.getStudentUser().getResume().getResumeUrl());
            } else {
                map.put("resume", "");
            }
            if (rl != null) {
                map.put("letter", rl.getLetterUrl());
                map.put("letterId", rl.getId());
            } else {
                map.put("letter", "");
            }
            map.put("email", sp.getStudentUser().getEmail());

            students.add(map);

        }
        return ResponseEntity.ok().body(students);
    }

    /**
     * GET {prefix}/enterprises/projects/showlist
     * @param request
     * @return List<Project>
     */
    @RequestMapping(value = "/projects", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Project>> getCurrentCompanyProjects(HttpServletRequest request){
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        User comUser = userService.getUserById(comId);
        List<Project> pl = new ArrayList<>(comUser.getPostedProjects());
        List<Project> res = new ArrayList<>();
        for (Project project: pl) {
            if (!project.getStatus().equals("deleted")) {
                res.add(project);
            }
        }
        return ResponseEntity.ok().body(res);
    }

    /**
     * GET {prefix}/enterprises/info
     * @param
     * @return UserInfo
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> getCompanyInfo(HttpServletRequest request){
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = userInfoService.getUserInfoByUserId(comId);
        map.put("companyInfo", userInfo);
        map.put("phone", userInfo.getUser().getPhoneNum());
        map.put("email", userInfo.getUser().getEmail());
        return ResponseEntity.ok().body(map);
    }

    /**
     * GET {prefix}/enterprises/projects/{id}
     * @param
     * @return UserInfo
     */
    @RequestMapping(value = "/projects/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Project> getProjectById(@PathVariable Long id, HttpServletRequest request){
        Project project = projectService.getProjectById(id);
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        if (project == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User user = project.getCompanyUser();
        if (comId != user.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(project);
    }

    /**
     * GET {prefix}/enterprises/info
     * @param
     * @return UserInfo
     */
    @RequestMapping(value = "/info", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserInfo> updateStudentInfo(@RequestBody Map<String, String> map, HttpServletRequest request){
        System.out.println(map.toString());
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        User user = userService.getUserById(stuId);
        if (map.get("phone") != null) {
            user.setPhoneNum(map.get("phone"));
        }
        UserInfo uI = userInfoService.getUserInfoByUserId(stuId);
        if (map.get("firstName") != null) {
            uI.setFirstName(map.get("firstName"));
        }
        if (map.get("institution") != null) {
            uI.setInstitution(map.get("institution"));
        }

        if (map.get("address") != null) {
            uI.setInstitution(map.get("institution"));
        }

        uI.setId(uI.getId());
        uI.setUser(user);
        uI.setRegisterTime(ZonedDateTime.now());
        userService.updateUser(user);
        userInfoService.updateUserInfo(uI);
        return ResponseEntity.ok().body(uI);
    }

    /**
     * POST {prefix}/enterprises/recommendationLetter/upload
     * @param file, id
     * @return ResponseEntity<RecommendationLetter>
     */
    @RequestMapping(value = "/uploadRecommendationLetter/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecommendationLetter> uploadRecommendationLetter(@RequestParam("file") MultipartFile file, @RequestParam("studentId") long studentId, HttpServletRequest request){
        if (file == null){
            throw new ApiRequestException("Invalid File! please upload again!");
        }
        URI url = azureService.upload(file);
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        User comUser = userService.getUserById(comId);
        RecommendationLetter rl = recommendationLetterService.getLetterByUserId(comUser, studentId);
        if (rl != null) {
            rl.setStatus("hide");
            recommendationLetterService.updateRecommendationLetter(rl);
        }


        RecommendationLetter recommendationLetter = new RecommendationLetter(comUser, studentId, url.toString(), "valid");
        recommendationLetter.setIssueInstitution(comUser.getUsername());
        recommendationLetter.setCreateTime(ZonedDateTime.now());
        recommendationLetterService.save(recommendationLetter);
        return ResponseEntity.ok().body(recommendationLetter);
    }


    /**
     * PATCH {prefix}/enterprises/recommendationletter/deletion
     * @param recommendationLetterId, request
     * @return ResponseEntity<RecommendationLetter>
     */
    @RequestMapping(value = "/uploadRecommendationLetter/deletion", method = RequestMethod.PATCH, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RecommendationLetter> deleteProject(@RequestParam("recommendationLetterId") long recommendationLetterId, HttpServletRequest request){
        if(recommendationLetterId <= 0){
            throw new ApiRequestException("Invalid Id! please input again!");
        }
        RecommendationLetter r = recommendationLetterService.getRecommendationLetterById(recommendationLetterId);
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        User comUser = userService.getUserById(comId);
        if (!comUser.getUsername().equals(r.getCompanyUser().getUsername())){
            throw new ApiRequestException("You are not authorized to proceed!");
        }
        r.setStatus("deleted");
        recommendationLetterService.updateRecommendationLetter(r);
        return ResponseEntity.ok().body(r);
    }

    /**
     * GET {prefix}/enterprises/projects/{id}
     * @param
     * @return UserInfo
     */
    @RequestMapping(value = "/projects/finish", method = RequestMethod.PATCH, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Project> finishProject(@RequestParam("proId") Long id, HttpServletRequest request){
        Project project = projectService.getProjectById(id);
        HttpSession session = request.getSession();
        long comId = (long)session.getAttribute("curUser");
        if (project == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User user = project.getCompanyUser();
        if (comId != user.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        project.setStatus("finished");
        projectService.updateProject(project);
        List<StudentProject> studentProjectList = studentProjectService.getStudentProjectListByProjectId(project.getId());
        for (StudentProject studentProject: studentProjectList) {
            if (studentProject.getStatus().equals("approved")) {
                studentProject.setStatus("finished");
                studentProjectService.updateStudentProject(studentProject);
            }
        }
        return ResponseEntity.ok().body(project);
    }

}
