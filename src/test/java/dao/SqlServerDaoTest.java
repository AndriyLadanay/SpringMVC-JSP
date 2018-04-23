package dao;

import model.Employee;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-test.xml"})
public class SqlServerDaoTest {

    @Autowired
    private EmployeeDao dao;

    @Before
    public void setUp() throws SQLException {
         dao.getConnection().setAutoCommit(false);
    }

    @Test
    public void test_count() throws SQLException {
        assertEquals(3, dao.count());
    }

    @Test
    public void testRead() throws SQLException {
        assertEquals("Petro", dao.read(1).getName());
        assertEquals("Tech", dao.read(1).getDepartment().getName());
        assertEquals(true, dao.read(1).isActive());
    }

    @Test
    public void testCreate() throws SQLException {
        dao.create("Katya", true, 2);
        Employee katya = dao.find("Katya", 1, 0).get(0);
        assertEquals("Katya", katya.getName());
        assertEquals("HR", katya.getDepartment().getName());
        assertEquals(true, katya.isActive());
    }

    @Test
    public void testUpdate() throws SQLException {
        dao.update(1, "Vasyl", false, 1);
        Employee vasyl = dao.read(1);
        assertEquals("Vasyl", vasyl.getName());
        assertEquals("Tech", vasyl.getDepartment().getName());
        assertEquals(false, vasyl.isActive());
    }

    @Test
    public void testDelete() throws SQLException {
        dao.delete(1);
        assertNull( dao.read(1));
    }

    @Test
    public void testFind() throws SQLException {
        List<Employee> employees = dao.find("Ivan", 2, 0);
        Employee ivan = employees.get(0);
        Employee ivanka = employees.get(1);
        assertEquals(2, employees.size());
        assertEquals("Ivan", ivan.getName());
        assertEquals("HR", ivan.getDepartment().getName());
        assertEquals(true, ivan.isActive());
        assertEquals("Ivanka", ivanka.getName());
        assertEquals("HR", ivanka.getDepartment().getName());
        assertEquals(true, ivanka.isActive());
    }

    @Test
    public void testFindUsingSkipOneEmployee() throws SQLException{
        List<Employee> employees = dao.find("Ivan", 2, 1);
        Employee ivanka = employees.get(0);
        assertEquals(1, employees.size());
        assertEquals("Ivanka", ivanka.getName());
        assertEquals("HR", ivanka.getDepartment().getName());
        assertEquals(true, ivanka.isActive());
    }

    @After
    public void finish() throws SQLException {
        dao.getConnection().rollback();
    }
}
