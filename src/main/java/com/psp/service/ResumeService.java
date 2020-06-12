package com.psp.service;

import com.psp.model.Resume;
import com.psp.repository.ResumeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ResumeService {
    @Autowired
    ResumeDao resumeDao;

    public Resume save(Resume resume){return resumeDao.save(resume);}
    public boolean deleteResumeInfoById(long id){return resumeDao.deleteResumeInfoById(id);}
    public List<Resume> getAllResume(){return resumeDao.getAllResume();}
    public Resume updateResume(Resume resume){return resumeDao.updateResume(resume);}
    public Resume getResumeByUserId(long id){return resumeDao.getResumeByUserId(id);}
}
