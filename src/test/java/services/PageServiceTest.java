package services;

import dao.EmployeeDao;
import exceptions.PageDoesntExistException;
import model.Department;
import model.Employee;
import org.junit.Before;
import org.junit.Test;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class PageServiceTest {

    private PageServiceImpl service;

    ArrayList<Employee> mockEmployees;

    @Before
    public void setUp(){
        service = new PageServiceImpl();
        service.dao = mock(EmployeeDao.class);
        mockEmployees = new ArrayList<>();
        Employee petro = new Employee("Petro", true, new Department(1, "Tech"));
        Employee ivan = new Employee("Ivan", true, new Department(2, "HR"));
        Employee ivanka = new Employee("Ivanka", true, new Department(2, "HR"));
        Employee stas = new Employee("Stas", false, new Department(3, "Finance"));
        petro.setId(1);
        ivan.setId(2);
        ivanka.setId(3);
        stas.setId(4);
        mockEmployees.add(petro);
        mockEmployees.add(ivan);
        mockEmployees.add(ivanka);
        mockEmployees.add(stas);
    }

    @Test
    public void testGetPage() throws SQLException, PageDoesntExistException {
        when(service.dao.find(2, 2)).thenReturn(mockEmployees.subList(2, 4));
        List<Employee> employees = service.page(2, 2);
        assertEquals(employees.get(0), mockEmployees.get(2));
        assertEquals(employees.get(1), mockEmployees.get(3));
    }
    @Test(expected = PageDoesntExistException.class)
    public void testGetPageThatDoesntExist() throws SQLException, PageDoesntExistException {
        when(service.dao.find(1, 3)).thenReturn(new ArrayList<>());
        service.page(4, 1);
    }

    @Test
    public void testGetPageWithName() throws SQLException, PageDoesntExistException {
        when(service.dao.find("Ivan", 1, 0)).thenReturn(mockEmployees.subList(0,1));
        when(service.dao.find("Ivan", 1, 1)).thenReturn(mockEmployees.subList(1,2));
        assertEquals(service.pageOfSearch("Ivan", 1, 1), mockEmployees.subList(0,1));
        assertEquals(service.pageOfSearch("Ivan", 2, 1), mockEmployees.subList(1,2));
    }

    @Test(expected = PageDoesntExistException.class)
    public void testGetPageOfSearchThatDoesntExist() throws SQLException, PageDoesntExistException {
        when(service.dao.find("Ivan", 1, 3)).thenReturn(new ArrayList<>());
        service.pageOfSearch("Ivan", 4, 1);
    }

    @Test
    public void testGetTotalPages() throws SQLException{
        when(service.dao.count()).thenReturn(50);
        assertEquals(5, service.getTotalPages(10));
        assertEquals(8, service.getTotalPages(7));
    }

    @Test
    public void testGetTotalPagesOfName() throws SQLException {
        when(service.dao.countOfName("Ivan")).thenReturn(50);
        assertEquals(5, service.getTotalPages("Ivan", 10));
        assertEquals(8, service.getTotalPages("Ivan", 7));
    }
}
