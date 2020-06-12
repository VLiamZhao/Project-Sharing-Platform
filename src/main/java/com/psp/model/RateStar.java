package com.psp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ratestar")
public class RateStar {
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

    @Column(name = "star_num")
    private int star;

    @Column(name = "rate_time")
    private ZonedDateTime createTime;

    public RateStar() {
    }

    public RateStar(long studentUserId, User companyUser, int star) {
        this.studentUserId = studentUserId;
        this.companyUser = companyUser;
        this.star = star;
    }

    public RateStar(long studentUserId, User companyUser, int star, ZonedDateTime createTime) {
        this.studentUserId = studentUserId;
        this.companyUser = companyUser;
        this.star = star;
        this.createTime = createTime;
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

    public void setStudentUserId(long studentUser) {
        this.studentUserId = studentUser;
    }

    public User getCompanyUser() {
        return companyUser;
    }

    public void setCompanyUser(User companyUser) {
        this.companyUser = companyUser;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }
}
