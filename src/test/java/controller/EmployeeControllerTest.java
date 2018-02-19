package controller;

import dao.EmployeeDao;
import model.Department;
import model.Employee;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pager.Pager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController controller;

    @Mock
    private EmployeeDao dao;

    @Mock
    private Pager pager;

    private MockMvc mockMvc;

    private ArrayList<Employee> list;

    @BeforeEach
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        list = new ArrayList<>();
        list.add(new Employee());
        list.add(new Employee());
        when(dao.count()).thenReturn(1000);
        when(dao.lastPage(1000)).thenReturn(100);
        when(pager.getPagesLinks(anyInt(), anyInt())).thenReturn(Arrays.asList(1, 2,3,4,5,6,7,8,9,10));
    }

    @Test
    public void testStartPage() throws Exception{
        when(dao.page(1, 10)).thenReturn(list);
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("employees", list))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("lastPage", 100))
                .andExpect(model().attribute("numbers", hasSize(10)));
        verify(dao).lastPage(anyInt());
        verify(dao).count();
        verify(dao).page(1, 10);
        verifyNoMoreInteractions(dao);

    }

    @Test
    public void testEmployeesPage() throws Exception {
        when(dao.page(5, 10)).thenReturn(list);
        mockMvc.perform(get("/employees/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("employees", list))
                .andExpect(model().attribute("currentPage", 5))
                .andExpect(model().attribute("lastPage", 100))
                .andExpect(model().attribute("numbers", hasSize(10)));
        verify(dao).lastPage(anyInt());
        verify(dao).count();
        verify(dao).page(5, 10);
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testView() throws Exception {
        Employee employee = mock(Employee.class);
        when(dao.read(5)).thenReturn(employee);
        mockMvc.perform(get("/employees/view/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("EmployeeView"))
                .andExpect(model().attribute("employee", employee));
        verify(dao).read(5);
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testEditGet() throws Exception {
        Employee employee = mock(Employee.class);
        when(dao.read(5)).thenReturn(employee);
        when(dao.departments()).thenReturn(Arrays.asList(new Department(), new Department()));
        mockMvc.perform(get("/employees/edit/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditForm"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("departments", hasSize(2)));
    }

    @Test
    public void testEditPost() throws Exception {
        String id = "5";
        int intID = 5;
        String active = "off";
        int intActive = 0;
        String name = "Ivanov";
        String department = "1";
        int intDepartment = 1;

        mockMvc.perform(post("/employees/edit").param("id", id)
                .param("name", name)
                .param("department", department)
                .param("active", active))
                .andExpect(status().isOk())
                .andExpect(view().name("SuccessEdit"));
        verify(dao).update(intID, name, intActive, intDepartment);
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testInsertGet() throws Exception {
        when(dao.departments()).thenReturn(Arrays.asList(new Department(), new Department()));
        mockMvc.perform(get("/employees/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("Insert"))
                .andExpect(model().attribute("departments", hasSize(2)));
        verify(dao).departments();
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testInsertPost() throws Exception {
        String active = "off";
        int intActive = 0;
        String name = "Ivanov";
        String department = "1";
        int intDepartment = 1;
        mockMvc.perform(post("/employees/insert")
                .param("name", name)
                .param("department", department)
                .param("active", active))
                .andExpect(status().isOk())
                .andExpect(view().name("SuccessInsert"));
        verify(dao).insert(name, intActive, intDepartment);
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testDeleteGet() throws Exception {
        int id = 5;
        mockMvc.perform(get("/employees/delete/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmationDelete"))
                .andExpect(model().attribute("id", id));
        verify(dao).read(id);
    }

    @Test
    public void testDeletePost() throws Exception {
        String id = "5";
        int intId = 5;
        mockMvc.perform(post("/employees/delete")
                .param("id", id))
                .andExpect(status().isOk())
                .andExpect(view().name("SuccessDelete"));
        verify(dao).delete(intId);
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testSearch() throws SQLException {

    }
}
