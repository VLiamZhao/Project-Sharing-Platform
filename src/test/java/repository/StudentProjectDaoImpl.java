package repository;

import com.psp.init.ApplicationBootstrap;
import com.psp.model.StudentProject;
import com.psp.repository.StudentProjectDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
public class StudentProjectDaoImpl {
    @Autowired
    StudentProjectDao studentProjectDao;

    @Test
    public void getStudentProjectByStudentIdAndProjectId(){
        StudentProject studentProject = studentProjectDao.getStudentProjectByStudentIdAndProjectId(3L, 3L);
        Assert.assertNull(studentProject);
    }
}
