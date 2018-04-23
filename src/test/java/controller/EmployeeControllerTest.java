package controller;

import model.Department;
import model.Employee;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.DepartmentService;
import services.EmployeeService;
import services.PageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController controller;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private PageService pageService;

    @Mock
    private DepartmentService departmentService;

    private MockMvc mockMvc;

    private ArrayList<Employee> list;

    private List<Integer> numbers;

    @BeforeEach
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        list = new ArrayList<>();
        list.add(new Employee());
        list.add(new Employee());
        numbers = Arrays.asList(1, 2,3,4,5,6,7,8,9,10);
        when(pageService.getTotalPages(10)).thenReturn(1000);
        when(pageService.getTotalPages("Petro", 10)).thenReturn(100);
        when(pageService.getPagesNumbers(anyInt(), anyInt())).thenReturn(numbers);
    }

    @Test
    public void testStartPage() throws Exception{
        when(pageService.page(1, EmployeeController.PAGE_SIZE)).thenReturn(list);
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("employees", list))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("lastPage", 1000))
                .andExpect(model().attribute("numbers", numbers));
        verify(pageService).getTotalPages(EmployeeController.PAGE_SIZE);
        verify(pageService).page(1, EmployeeController.PAGE_SIZE);
    }

    @Test
    public void testEmployeesPage() throws Exception {
        when(pageService.page(5, EmployeeController.PAGE_SIZE)).thenReturn(list);
        mockMvc.perform(get("/employees/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("employees", list))
                .andExpect(model().attribute("currentPage", 5))
                .andExpect(model().attribute("lastPage", 1000))
                .andExpect(model().attribute("numbers", numbers));
        verify(pageService).getPagesNumbers(5, 1000);
        verify(pageService).getTotalPages(EmployeeController.PAGE_SIZE);
        verify(pageService).page(5, EmployeeController.PAGE_SIZE);
    }

    @Test
    public void testView() throws Exception {
        Employee employee = mock(Employee.class);
        when(employeeService.read(5)).thenReturn(employee);
        mockMvc.perform(get("/employees/view/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("EmployeeView"))
                .andExpect(model().attribute("employee", employee));
        verify(employeeService).read(5);
    }

    @Test
    public void testEditGet() throws Exception {
        Employee employee = mock(Employee.class);
        when(employeeService.read(5)).thenReturn(employee);
        when(departmentService.getAll()).thenReturn(Arrays.asList(new Department(), new Department()));
        mockMvc.perform(get("/employees/edit/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditForm"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("departments", hasSize(2)));
    }

    @Test
    public void testEditPost() throws Exception {
        String id = "5";
        int intId = 5;
        String active = "off";
        String name = "Ivanov";
        String dpId = "1";
        int dpID = 1;

        mockMvc.perform(post("/employees/edit").param("id", id)
                .param("name", name)
                .param("dpID", dpId)
                .param("active", active))
                .andExpect(status().isOk())
                .andExpect(view().name("SuccessEdit"));
        verify(employeeService).update(intId, name, active, dpID);
    }

    @Test
    public void testInsertGet() throws Exception {
        when(departmentService.getAll()).thenReturn(Arrays.asList(new Department(), new Department()));
        mockMvc.perform(get("/employees/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("Insert"))
                .andExpect(model().attribute("departments", hasSize(2)));
        verify(departmentService).getAll();
    }

    @Test
    public void testInsertPost() throws Exception {
        String active = "off";
        String name = "Ivanov";
        String department = "1";
        int dpID = 1;
        mockMvc.perform(post("/employees/insert")
                .param("name", name)
                .param("dpID", department)
                .param("active", active))
                .andExpect(status().isOk())
                .andExpect(view().name("SuccessInsert"));
        verify(employeeService).create(name, active, dpID);
    }

    @Test
    public void testDeleteGet() throws Exception {
        int id = 5;
        mockMvc.perform(get("/employees/delete/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmationDelete"))
                .andExpect(model().attribute("id", id));
        verify(employeeService).read(id);
    }

    @Test
    public void testDeletePost() throws Exception {
        String id = "5";
        int intId = 5;
        mockMvc.perform(post("/employees/delete")
                .param("id", id))
                .andExpect(status().isOk())
                .andExpect(view().name("SuccessDelete"));
        verify(employeeService).delete(intId);
    }

    @Test
    public void testSearch() throws Exception {
        String name = "Petro";
        when(pageService.pageOfSearch(name,5, EmployeeController.PAGE_SIZE)).thenReturn(list);
        mockMvc.perform(get("/employees/search/Petro/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("searchView"))
                .andExpect(model().attribute("employees", list))
                .andExpect(model().attribute("currentPage", 5))
                .andExpect(model().attribute("lastPage", 100))
                .andExpect(model().attribute("numbers", hasSize(10)))
                .andExpect(model().attribute("nameToSearch", name));
        verify(pageService).getTotalPages(name,EmployeeController.PAGE_SIZE);
        verify(pageService).pageOfSearch(name,5, EmployeeController.PAGE_SIZE);
    }
}
