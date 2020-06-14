package service;

import com.psp.init.ApplicationBootstrap;
import com.psp.model.Role;
import com.psp.model.User;
import com.psp.repository.RoleDao;
import com.psp.repository.UserDao;
import com.psp.service.JWTService;
import io.jsonwebtoken.Claims;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class JWTServiceTest {
    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;
    @Autowired
    JWTService jwtService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    User u1 = new User("testcompany", "123", "testcom@test.com", "company");
    User u2 = new User("teststudent", "123", "teststu@test.com", "student");

    @Before
    public void init(){
        Role r1 = roleDao.getRoleById(3L);
        Role r2 = roleDao.getRoleById(2L);
        u1.setRole(r1);
        u2.setRole(r2);
        u1 = userDao.save(u1);
//        u2 = userDao.save(u2);
    }

    @After
    public void tearDown(){

        userDao.deleteUserById(u1.getId());
//        userDao.deleteUserById(u2.getId());
    }


    //passed
    @Test
    public void generateTokenTest(){
        String token = jwtService.generateToken(userDao.getUserByEmail("testcom@test.com"));
        String patternString = "^[A-Za-z0-9_]+\\.[A-Za-z0-9_]+\\.?[A-Za-z0-9_]*$";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(token);
        Assert.assertTrue(token, matcher.matches());
    }
    //passed
    @Test
    public void decodeTokenTest(){
        String token = jwtService.generateToken(u1);
        Claims c = jwtService.decodeJwtToken(token);
        String username = c.getSubject();
        Assert.assertEquals(u1.getUsername(), username);
    }
}