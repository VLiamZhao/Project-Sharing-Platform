package com.psp.repository;

import com.psp.model.User;
import com.psp.model.UserInfo;

import java.util.List;

public interface UserInfoDao {
    UserInfo save(UserInfo userInfo);
    boolean deleteUserInfoById(long id);
    List<UserInfo> getAllUserInfo();
    UserInfo updateUserInfo(UserInfo userInfo);
    UserInfo getUserInfoByUserId(long id);
    String getUserIconUrlById(long id);
}
