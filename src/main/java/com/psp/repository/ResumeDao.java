package com.psp.repository;

import com.psp.model.Resume;
import com.psp.model.UserInfo;

import java.util.List;

public interface ResumeDao {
    Resume save(Resume resume);
    boolean deleteResumeInfoById(long id);
    List<Resume> getAllResume();
    Resume updateResume(Resume resume);
    Resume getResumeByUserId(long id);
}
