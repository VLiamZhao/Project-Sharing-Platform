package repository;

import com.psp.init.ApplicationBootstrap;
import com.psp.model.Role;
import com.psp.repository.RoleDao;
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
public class RoleDaoTest {
    @Autowired
    RoleDao roleDao;

    Role r1 = new Role("Test", "/auth, /students, /projects, /enterprises",
            true, true, true, true);
    Role r2 = new Role("Test2", "/auth, /enterprises",
            true, true, false, false);
    int roleNum = 0;

    @Before
    public void init(){
        roleDao.save(r1);

    }

    @After
    public void tearDown(){
        roleDao.deleteRoleById(r1.getId());
    }

    @Test
    public void getAllRolesTest(){
        List<Role> roles = roleDao.getRoles();
        Assert.assertEquals(4, roles.size());
    }

    @Test
    public void getRoleByIdTest(){
        Role role = roleDao.getRoleById(3L);
        Assert.assertEquals("Company", role.getName());
    }
}
