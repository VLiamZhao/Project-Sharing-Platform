package service;

import com.psp.init.ApplicationBootstrap;
import com.psp.model.Role;
import com.psp.service.RoleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class RoleServiceTest {
    @Autowired
    RoleService roleService;

    @Test
    public void getRoleByNameTest(){
        Role r = roleService.getRoleByName(Role.studentRole);
        Assert.assertNotNull(r);
    }
}
