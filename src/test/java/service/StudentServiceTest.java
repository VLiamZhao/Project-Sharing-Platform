package service;

import com.psp.init.ApplicationBootstrap;
import com.psp.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class StudentServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void getUserByIdTest(){
        Assert.assertEquals(userService.getUserById(1l).getUsername(), "Tommy");
    }

}
