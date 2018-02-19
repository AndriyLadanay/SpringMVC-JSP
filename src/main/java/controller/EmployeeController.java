package controller;

import dao.EmployeeDao;
import dbconnection.DatabaseConnection;
import exceptions.EmployeeNotFoundException;
import exceptions.PageDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import pager.Pager;
import services.JSPService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Employee;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    public static final int PAGE_SIZE = 10;

    @Autowired
    private DatabaseConnection connection;
    @Autowired
    private EmployeeDao dao;
    @Autowired
    private Pager pager;


    private JSPService service = new JSPService();

    @RequestMapping(method = RequestMethod.GET)
    public String startPage(Model model) throws SQLException, PageDoesntExistException {
        int lastPage = dao.lastPage(dao.count());
        ArrayList<Employee> employees = dao.page(1, PAGE_SIZE);
        model.addAttribute("employees", employees);
        model.addAttribute("numbers", pager.getPagesLinks(1, lastPage));
        model.addAttribute("currentPage", 1);
        model.addAttribute("lastPage", lastPage);
        return "index";
    }

    @RequestMapping("/{page}")
    public String getPage(Model model, @PathVariable int page)
            throws SQLException, PageDoesntExistException {
        ArrayList<Employee> employees = dao.page(page, PAGE_SIZE);
        int lastPage = dao.lastPage(dao.count());
        model.addAttribute("employees", employees);
        model.addAttribute("numbers", pager.getPagesLinks(page, lastPage));
        model.addAttribute("currentPage", page);
        model.addAttribute("lastPage", lastPage);
        return "index";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable int id, Model model) throws SQLException,
            EmployeeNotFoundException {
        model.addAttribute("employee", dao.read(id));
        return "EmployeeView";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable int id, Model model)
            throws SQLException, EmployeeNotFoundException {
        Employee employee = dao.read(id);
        model.addAttribute("employee", employee);
        model.addAttribute("departments", dao.departments());
        return "EditForm";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editEmployee(@RequestParam int id, @RequestParam String name,
            @RequestParam(defaultValue = "off") String active,
            @RequestParam int department) throws SQLException {
        int intActive = active.equals("on") ? 1 : 0;
        dao.update(id, name, intActive, department);
        return "SuccessEdit";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String insert(Model model) throws SQLException {
        model.addAttribute("departments", dao.departments());
        return "Insert";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insertEmployee(@RequestParam String name,
            @RequestParam(defaultValue = "off") String active,
            @RequestParam int department) throws SQLException {
        int intActive = active.equals("on") ? 1 : 0;
        dao.insert(name, intActive, department);
        return "SuccessInsert";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id, Model model)
            throws SQLException, EmployeeNotFoundException {
        dao.read(id);
        model.addAttribute("id", id);
        return "confirmationDelete";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteEmployee(@RequestParam int id) throws SQLException {
        dao.delete(id);
        return "SuccessDelete";
    }

    @RequestMapping("/search/{name}/{page}")
    public String search(@PathVariable String name, @PathVariable int page,
            Model model) throws SQLException, PageDoesntExistException {
        int recordsCount = dao.countOfName(name);
        int lastPage = dao.lastPage(recordsCount);
        ArrayList<Employee> employees = dao.search(name, page, PAGE_SIZE);
        if (employees.size() == 0) {
            return "SearchWithNoSuccess";
        }
        if (page > lastPage || page < 1) {
            return "PageNotFound";
        }
        model.addAttribute(name);
        model.addAttribute(employees);
        model.addAttribute(pager.getPagesLinks(page, lastPage));
        model.addAttribute(page);
        model.addAttribute(lastPage);

        return "searchView";
    }

    /* @RequestMapping("/lastPage")
    public int lastPage() throws SQLException {
        return dao.lastPage(dao.count());
    } */

    @ExceptionHandler(SQLException.class)
    public String sqlExceptionHandle() {
        return "SQLException";
    }

    @ExceptionHandler(PageDoesntExistException.class)
    public String pageDoesntExist(){
        return "PageNotFound";
    }
}


