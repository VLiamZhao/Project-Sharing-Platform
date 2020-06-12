package com.psp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.psp.model.view.JsView;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username")
    private String username;


    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "phone_num")
    private String phoneNum;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "role_id")
    private Role role;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private UserInfo userInfo;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToOne(mappedBy = "studentUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Resume resume;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "companyUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<RateStar> receiveStars;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "companyUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<RecommendationLetter> issuedRecommendationLetters;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "studentUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<StudentProject> studentProjects;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "companyUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Project> postedProjects;

    public User() {
    }

    public User(String username, String password, String email, String userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<RateStar> getReceiveStars() {
        return receiveStars;
    }

    public void setReceiveStars(Set<RateStar> receiveStars) {
        this.receiveStars = receiveStars;
    }

    public Set<RecommendationLetter> getIssuedRecommendationLetters() {
        return issuedRecommendationLetters;
    }

    public void setIssuedRecommendationLetters(Set<RecommendationLetter> issuedRecommendationLetters) {
        this.issuedRecommendationLetters = issuedRecommendationLetters;
    }

    public Set<StudentProject> getStudentProjects() {
        return studentProjects;
    }

    public void setStudentProjects(Set<StudentProject> studentProjects) {
        this.studentProjects = studentProjects;
    }

    public Set<Project> getPostedProjects() {
        return postedProjects;
    }

    public void setPostedProjects(Set<Project> postedProjects) {
        this.postedProjects = postedProjects;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
