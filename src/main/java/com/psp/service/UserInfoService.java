package com.psp.service;

import com.psp.model.UserInfo;
import com.psp.repository.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserInfoService {
    @Autowired
    UserInfoDao userInfoDao;

    public UserInfo save(UserInfo userInfo){return userInfoDao.save(userInfo);}
    public boolean deleteUserInfoById(long id){return userInfoDao.deleteUserInfoById(id);}
    public List<UserInfo> getAllUserInfo(){return userInfoDao.getAllUserInfo();}
    public UserInfo updateUserInfo(UserInfo userInfo){return userInfoDao.updateUserInfo(userInfo);}
    public UserInfo getUserInfoByUserId(long id){return userInfoDao.getUserInfoByUserId(id);}
    public String getUserIconUrlById(long id){return userInfoDao.getUserIconUrlById(id);}
}
