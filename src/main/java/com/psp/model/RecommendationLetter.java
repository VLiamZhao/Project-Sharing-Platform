package com.psp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "recommendation_letter")
public class RecommendationLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "student_id")
    private long studentUserId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "company_id")
    private User companyUser;

    @Column(name = "letter_url")
    private String letterUrl;

    @Column(name = "issue_institution")
    private String issueInstitution;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @Column(name = "status")
    private String status;

    public RecommendationLetter() {
    }

    public RecommendationLetter(User companyUser, long studentUserId, String letterUrl, String status) {
        this.companyUser = companyUser;
        this.studentUserId = studentUserId;
        this.letterUrl = letterUrl;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStudentUserId() {
        return studentUserId;
    }

    public void setStudentUserId(long studentUserId) {
        this.studentUserId = studentUserId;
    }

    public User getCompanyUser() {
        return companyUser;
    }

    public void setCompanyUser(User companyUser) {
        this.companyUser = companyUser;
    }

    public String getLetterUrl() {
        return letterUrl;
    }

    public void setLetterUrl(String letterUrl) {
        this.letterUrl = letterUrl;
    }

    public String getIssueInstitution() {
        return issueInstitution;
    }

    public void setIssueInstitution(String issueInstitution) {
        this.issueInstitution = issueInstitution;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
