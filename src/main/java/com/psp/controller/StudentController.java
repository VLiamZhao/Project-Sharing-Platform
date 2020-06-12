package com.psp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.psp.exception.ApiRequestException;
import com.psp.model.*;
import com.psp.model.view.JsView;
import com.psp.service.*;
import org.checkerframework.checker.units.qual.Acceleration;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = {"/students"})
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
public class StudentController {
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
     * GET {prefix}/students/projects/application
     * @param project, request
     * @return
     */
    @RequestMapping(value = "/projects/application", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> StudentApplyForProject(@RequestBody Project project, HttpServletRequest request){
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        User user = userService.getUserById(stuId);

        Project tempProject = projectService.getProjectById(project.getId());
        System.out.println("project " + tempProject);
        if(user.getUserType().equals("company")){
            throw new ApiRequestException("Project application service is only for student users!");
        }
        List<StudentProject> studentProjects = studentProjectService.getStudentProjectListByStudentId(stuId);
        if (studentProjectService.getStudentProjectByStudentIdAndProjectId(stuId, project.getId()) == null){
            StudentProject sp = new StudentProject(user, tempProject, "pending");
            studentProjectService.save(sp);
            Map<String, String> map = new HashMap<>();
            map.put("Application", "Succeed!");
            return ResponseEntity.ok().body(map);
        } else {
            throw new ApiRequestException("Project application cannot be submitted again!");
        }
    }
    /**
     * GET {prefix}/students
     * @param
     * @return List<Student>
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<User>> getStudents(){
        List<User> students = studentService.getStudents();
        return ResponseEntity.ok().body(students);
    }

    /**
     * GET {prefix}/students/info
     * @param
     * @return List<Project>
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> getStudentInfo(HttpServletRequest request){
        Map<String, Object> res = new HashMap<>();
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        User user = userService.getUserById(stuId);
        UserInfo userInfo = userInfoService.getUserInfoByUserId(stuId);
        res.put("userinfo", userInfo);
        res.put("phone", user.getPhoneNum());
        res.put("email", user.getEmail());
        return ResponseEntity.ok().body(res);
    }

    /**
     * GET {prefix}/students/info
     * @param
     * @return Map<String, String>
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
        if (map.get("lastName") != null) {
            uI.setLastName(map.get("lastName"));
        }
        if (map.get("institution") != null) {
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
     * POST {prefix}/students/info/icon
     * @param icon
     * @return ResponseEntity<String>
     */
    @RequestMapping(value = "/info/icon", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadStudentIcon(@RequestParam("icon") MultipartFile icon, HttpServletRequest request){
        if (icon == null){
            throw new ApiRequestException("Invalid File! please upload again!");
        }
        URI url = azureService.upload(icon);
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        UserInfo userInfo = userInfoService.getUserInfoByUserId(stuId);
        userInfo.setIconUrl(url.toString());
        userInfoService.updateUserInfo(userInfo);
        return ResponseEntity.ok().body(url.toString());
    }

    /**
     * GET {prefix}/students/info/icon
     * @param
     * @return ResponseEntity<String>
     */
    @RequestMapping(value = "/info/icon", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getUserIcon(HttpServletRequest request){
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        UserInfo userInfo = userInfoService.getUserInfoByUserId(stuId);
        String url = userInfo.getIconUrl();
        return ResponseEntity.ok().body(url);
    }

    //TODO to be Tested
    /**
     * POST {prefix}/students/resumes/upload
     * @param resume
     * @return ResponseEntity<Project>
     */
    @RequestMapping(value = "/resumes", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resume> uploadResume(@RequestParam("file") MultipartFile resume, HttpServletRequest request){
        if (resume == null){
            throw new ApiRequestException("Invalid File! please upload again!");
        }
        URI url = azureService.upload(resume);
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        User studentUser = userService.getUserById(stuId);
        Resume res = studentUser.getResume();
        if (res == null) {
            res = new Resume(url.toString(), resume.getOriginalFilename(), ZonedDateTime.now());
            res.setStatus("cur");
            res.setStudentUser(studentUser);
            resumeService.updateResume(res);
        } else {
            res.setResumeUrl(url.toString());
            res.setStatus("cur");
            res.setCreateTime(ZonedDateTime.now());
            res.setResumeName(resume.getOriginalFilename());
            resumeService.updateResume(res);
        }
        return ResponseEntity.ok().body(res);
    }

//    /**
//     * PUT {prefix}/students/resumes
//     * @param resume, request
//     * @return ResponseEntity<Resume>
//     */
//    @RequestMapping(value = "/resumes", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Resume> updateResume(@RequestParam("file") MultipartFile resume, HttpServletRequest request){
//        HttpSession session = request.getSession();
//        long stuId = (long)session.getAttribute("curUser");
//        User studentUser = userService.getUserById(stuId);
//        if (resume == null){
//            throw new ApiRequestException("Invalid File! please upload again!");
//        }
//        URI url = azureService.upload(resume);
//        Resume res = studentUser.getResume();
//        if(res == null){
//            res = new Resume(url.toString(), resume.getOriginalFilename(), ZonedDateTime.now());
//            res.setStatus("cur");
//            res.setStudentUser(studentUser);
//            resumeService.updateResume(res);
//        }else {
//            res.setResumeUrl(url.toString());
//            resumeService.updateResume(res);
//        }
//        return ResponseEntity.ok().body(res);
//    }

    /**
     * PATCH {prefix}/students/resume
     * @param
     * @return ResponseEntity<Resume>
     */
    @RequestMapping(value = "/resumes/delete", method = RequestMethod.PATCH, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> deleteResume(HttpServletRequest request){
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        User studentUser = userService.getUserById(stuId);
        Resume resume = studentUser.getResume();
        if (resume == null){
            return ResponseEntity.ok().body("You have no resume to delete!");
        }else {
            resume.setStatus("deleted");
            resumeService.updateResume(resume);
            return ResponseEntity.ok().body("Your resume has been successfully deleted!");
        }
    }

    /**
     * GET {prefix}/students/resumes/download
     * @param request
     * @return ResponseEntity<Resume>
     */
    @RequestMapping(value = "/resumes/download", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> downloadResume(HttpServletRequest request){
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        User studentUser = userService.getUserById(stuId);
        Resume resume = studentUser.getResume();
        if (resume == null || resume.getStatus().equals("deleted")) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.ok().body(resume.getResumeUrl());
    }

    /**
     * GET {prefix}/students/projects/applied
     * @param
     * @return ResponseEntity<List<Project>>
     */

    @RequestMapping(value = "/projects/applied", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Map<String, Object>>> getAppliedProjectList(HttpServletRequest request){
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
//        User studentUser = userService.getUserById(stuId);
        List<Map<String, Object>> res = new ArrayList<>();
        List<Project> ps = projectService.getProjectsAndInfoById(stuId);
        for (Project project: ps) {
            Map<String, Object> map = new HashMap<>();
            User companyUser = project.getCompanyUser();
            project.setStudentProjects(null);
            map.put("project", project);
            map.put("companyName", companyUser.getUsername());
            StudentProject studentProject = studentProjectService.getStudentProjectByStudentIdAndProjectId(stuId, project.getId());
            Set<RateStar> ratestars = companyUser.getReceiveStars();
            if (ratestars == null || ratestars.size() == 0) {
                map.put("star", "0");
            } else {
                int stars = 0;
                for (RateStar rs : ratestars) {
                    stars += rs.getStar();
                }
                stars /= ratestars.size();
                map.put("star", stars);
            }
            map.put("status", studentProject.getStatus());
            res.add(map);

        }
//        List<Project> resPs = new ArrayList<>();
//        for(Project p : ps){
//            for(StudentProject stupro : p.getStudentProjects()){
//                if (stuId == stupro.getStudentUser().getId()){
//                    resPs.add(p);
//                    break;
//                }
//            }
//        }
        return ResponseEntity.ok().body(res);
    }

    /**
     * GET {prefix}/students/projects/finished
     * @param
     * @return ResponseEntity<List<Project>>
     */
    @RequestMapping(value = "/projects/finished", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Map<String, Object>>> getFinishedProjectList(HttpServletRequest request){
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
//        User studentUser = userService.getUserById(stuId);

        List<Project> projects = projectService.getProjectsAndInfoById(stuId);
        List<Map<String, Object>> res = new ArrayList<>();
        for(Project project : projects){
            for(StudentProject stupro : project.getStudentProjects()){
                if (stuId == stupro.getStudentUser().getId() && stupro.getStatus().equals("finished")){

                    Map<String, Object> map = new HashMap<>();
                    User companyUser = project.getCompanyUser();
                    RecommendationLetter rl = recommendationLetterService.getLetterByUserId(companyUser, stuId);
                    if (rl != null) {
                        map.put("letter", rl);
                    } else {
                        map.put("letter", null);
                    }
                    project.setStudentProjects(null);
                    map.put("project", project);
                    map.put("companyName", companyUser.getUserInfo().getFirstName());
                    map.put("avatar", companyUser.getUserInfo().getIconUrl());
                    Set<RateStar> ratestars = companyUser.getReceiveStars();
                    if (ratestars == null || ratestars.size() == 0) {
                        map.put("star", "0");
                    } else {
                        int stars = 0;
                        for (RateStar rs : ratestars) {
                            stars += rs.getStar();
                        }
                        stars /= ratestars.size();
                        map.put("star", stars);
                    }
                    map.put("companyID", companyUser.getId());
                    map.put("status", stupro.getStatus());
                    res.add(map);
                }
            }
        }
        return ResponseEntity.ok().body(res);
    }

    /**
     * GET {prefix}/students/projects
     * @param id
     * @return ResponseEntity<List<Project>>
     */
    @RequestMapping(value = "/projects", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> getDetailOfProjectById(@RequestParam("id") long id){
        Project project = projectService.getProjectById(id);
        User companyUser = project.getCompanyUser();
        Set<RateStar> ratestars = companyUser.getReceiveStars();
        int stars = 0;
        for (RateStar rs : ratestars){
            stars += rs.getStar();
        }
        stars /= ratestars.size();

        Map<String, String> map = new HashMap<>();
        map.put("Project Name", project.getProjectName());
        map.put("Project Url", project.getProjectUrl());
        map.put("Project Id",String.valueOf(project.getId()));
        map.put("Project Description", project.getDescription());
        map.put("Company Name", companyUser.getUsername());
        map.put("Company Icon Url", companyUser.getUserInfo().getIconUrl());
        map.put("Company Description", String.valueOf(companyUser.getId()));
        map.put("Company RateStars", String.valueOf(stars));
        return ResponseEntity.ok().body(map);
    }


    /**
     * POST {prefix}/students/projects/rate
     * @param comId, ratestars, request
     * @return ResponseEntity<Map<String, String>>
     */
    @RequestMapping(value = "/projects/ratestar", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RateStar> rateCompany(@RequestParam("id") long comId, @RequestParam("star")int ratestars, HttpServletRequest request){
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        User companyUser =  userService.getUserById(comId);
        if (companyUser.getUserType().equals("student")){
            throw new ApiRequestException("Please select the correct enterprise!");
        }
        Set<RateStar> rateStars = companyUser.getReceiveStars();
        for(RateStar r : rateStars){
            if (r.getStudentUserId() == stuId){
                throw new ApiRequestException("You have already rated the enterprise!");
            }
        }
        RateStar rs = new RateStar(stuId, companyUser, ratestars, ZonedDateTime.now());
        rateStarService.saveRate(rs);
        rateStars.add(rs);
        companyUser.setReceiveStars(rateStars);
        userService.updateUser(companyUser);
        return ResponseEntity.ok().body(rs);
    }

    /**
     * get {prefix}/students/projects/rate
     * @param comId, ratestars, request
     * @return ResponseEntity<Map<String, String>>
     *
     */
    @RequestMapping(value = "/projects/ratestar", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RateStar> isRateCompany(@RequestParam("id") long comId, HttpServletRequest request){
        HttpSession session = request.getSession();
        long stuId = (long)session.getAttribute("curUser");
        User companyUser =  userService.getUserById(comId);
        if (companyUser.getUserType().equals("student")){
            throw new ApiRequestException("Please select the correct enterprise!");
        }
        Set<RateStar> rateStars = companyUser.getReceiveStars();
        for(RateStar r : rateStars){
            if (r.getStudentUserId() == stuId){
                return ResponseEntity.ok().body(r);
            }
        }
        return ResponseEntity.ok().body(null);
    }


}
