package controller;

import exceptions.EmployeeNotFoundException;
import exceptions.PageDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

import model.Employee;
import services.DepartmentService;
import services.EmployeeService;
import services.PageService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    public static final int PAGE_SIZE = 10;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PageService pageService;
    @Autowired
    private DepartmentService departmentService;


    @RequestMapping(method = RequestMethod.GET)
    public String startPage(HttpServletResponse response, Model model) throws SQLException, PageDoesntExistException {
        response.setHeader("Cache-Control", "no-store");
        int totalPages = pageService.getTotalPages(PAGE_SIZE);
        model.addAttribute("employees", pageService.page(1, PAGE_SIZE));
        model.addAttribute("numbers", pageService.getPagesNumbers(1, totalPages));
        model.addAttribute("currentPage", 1);
        model.addAttribute("lastPage", totalPages);
        return "index";
    }

    @RequestMapping("/{page}")
    public String getPage(HttpServletResponse response, Model model, @PathVariable int page)
            throws SQLException, PageDoesntExistException {
        response.setHeader("Cache-Control", "no-store");
        int lastPage = pageService.getTotalPages(PAGE_SIZE);
        model.addAttribute("employees", pageService.page(page, PAGE_SIZE));
        model.addAttribute("numbers", pageService.getPagesNumbers(page, lastPage));
        model.addAttribute("currentPage", page);
        model.addAttribute("lastPage", lastPage);
        return "index";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable int id, Model model) throws SQLException,
            EmployeeNotFoundException {
        model.addAttribute("employee", employeeService.read(id));
        return "EmployeeView";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable int id, Model model)
            throws SQLException, EmployeeNotFoundException {
        model.addAttribute("employee", employeeService.read(id));
        model.addAttribute("departments", departmentService.getAll());
        return "EditForm";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editEmployee(@RequestParam int id, @RequestParam String name,
            @RequestParam(defaultValue = "off") String active,
            @RequestParam int dpID) throws SQLException, EmployeeNotFoundException {
        employeeService.update(id, name, active, dpID);
        return "SuccessEdit";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String insert(Model model) throws SQLException {
        model.addAttribute("departments", departmentService.getAll());
        return "Insert";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insertEmployee(@RequestParam String name,
            @RequestParam(defaultValue = "off") String active,
            @RequestParam int dpID) throws SQLException {

        employeeService.create(name, active, dpID);
        return "SuccessInsert";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id, Model model)
            throws SQLException, EmployeeNotFoundException {
        employeeService.read(id);
        model.addAttribute("id", id);
        return "confirmationDelete";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteEmployee(@RequestParam int id) throws SQLException, EmployeeNotFoundException {
        employeeService.delete(id);
        return "SuccessDelete";
    }

    @RequestMapping("/search/{name}/{page}")
    public String search(@PathVariable String name, @PathVariable int page,
            Model model) throws SQLException, PageDoesntExistException {

        int lastPage = pageService.getTotalPages(name, PAGE_SIZE);
        List<Employee> employees = pageService.pageOfSearch(name, page, PAGE_SIZE);
        if (employees.size() == 0) return "SearchWithNoSuccess";
        if (page > lastPage || page < 1) return "PageNotFound";
        model.addAttribute("nameToSearch", name);
        model.addAttribute("employees", employees);
        model.addAttribute("numbers", pageService.getPagesNumbers(page, lastPage));
        model.addAttribute("currentPage", page);
        model.addAttribute("lastPage", lastPage);

        return "searchView";
    }

    @ExceptionHandler(SQLException.class)
    public String sqlExceptionHandle(SQLException e) {
        e.printStackTrace();
        return "SQLException";
    }

    @ExceptionHandler(PageDoesntExistException.class)
    public String pageDoesntExist(){
        return "PageNotFound";
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public String employeeNotFound(){
        return "EmployeeNotFound";
    }
}


