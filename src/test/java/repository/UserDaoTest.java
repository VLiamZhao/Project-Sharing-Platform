package repository;

import com.psp.init.ApplicationBootstrap;
import com.psp.model.Role;
import com.psp.model.User;
import com.psp.repository.RoleDao;
import com.psp.repository.UserDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

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

    @Test
    public void getUserByIdTest(){
        User user = userDao.getUserById(1L);
        Assert.assertEquals(user.getUsername(), "Tommy");
    }

    @Test
    public void getUserByEmailTest(){
        User user = userDao.getUserByEmail("testcom@test.com");
        Assert.assertEquals(user.getUsername(), "testcompany");
    }

    @Test
    public void getStudentsTest(){
        List<User> uList = userDao.getStudents();
        Assert.assertEquals(uList.size(), 2);
    }

    @Test
    public void saveDeleteStudentTest(){
        User u = null;
        u = userDao.save(u2);
        Assert.assertNotNull(u);
        boolean flag = userDao.deleteUserById(u.getId());
        Assert.assertTrue(flag);
    }

    @Test
    public void getUserByCredentials(){
        User u = null;
        u = userDao.getUserByCredentials("testcom@test.com", "123");
        Assert.assertEquals("testcompany", u.getUsername());
    }

    @Test
    public void updateUserTest(){
        u1.setUsername("updatedName");
        u1 = userDao.updateUser(u1);
        Assert.assertEquals("updatedName",u1.getUsername());
    }
}
