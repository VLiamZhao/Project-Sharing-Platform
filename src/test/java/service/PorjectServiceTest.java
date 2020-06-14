package service;

import com.psp.init.ApplicationBootstrap;
import com.psp.service.ProjectService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class PorjectServiceTest {
    @Autowired
    ProjectService projectService;

    @Test
    public void getProjectById(){
        Assert.assertNotNull(projectService.getProjectById(1L));
    }
}
