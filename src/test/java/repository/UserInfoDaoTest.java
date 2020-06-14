package repository;

import com.psp.init.ApplicationBootstrap;
import com.psp.model.Role;
import com.psp.model.User;
import com.psp.model.UserInfo;
import com.psp.repository.RoleDao;
import com.psp.repository.UserDao;
import com.psp.repository.UserInfoDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class UserInfoDaoTest {
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;

    User u1 = new User("testcompany", "123", "testcom@test.com", "company");
    User u2 = new User("teststudent", "123", "teststu@test.com", "student");
    UserInfo ui1 = new UserInfo(u1, "Liam", "Zhao", "UW", ZonedDateTime.now());

    @Before
    public void init(){
        u1.setUserInfo(ui1);
        ui1.setUser(u1);
        Role r1 = roleDao.getRoleById(3L);
        u1.setRole(r1);
        userDao.save(u1);
        userInfoDao.save(ui1);
    }

    @After
    public void tearDown(){
        User u = userDao.getUserByEmail("testcom@test.com");
        long infoId = u.getUserInfo().getId();
        userInfoDao.deleteUserInfoById(infoId);
        userDao.deleteUserById(u.getId());

    }

    @Test
    public void getUserInfoById(){
        User u = userDao.getUserByEmail("testcom@test.com");
        UserInfo ui0 = userInfoDao.getUserInfoByUserId(u.getId());
        Assert.assertEquals("Liam", ui0.getFirstName());
    }
}
