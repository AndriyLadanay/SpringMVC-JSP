package controller;

import dao.SqlServerDao;
import exceptions.EmployeeNotFoundException;
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
    private SqlServerDao dao = new SqlServerDao();
    private Pager pager = new Pager();
    private JSPService service = new JSPService();

    @RequestMapping(method = RequestMethod.GET)
    public String startPage(Model model) throws SQLException {
        ArrayList<Employee> employees = dao.getPage(1);
        int recordsCount = dao.count();
        int lastPage = dao.lastPage(recordsCount);
        service.serve(model, employees, pager.getPagesLinks(1, lastPage),
                1, lastPage);
        return "index";
    }

    @RequestMapping("/{page}")
    public String getPage(Model model, @PathVariable int page)
            throws SQLException {
        ArrayList<Employee> employees = dao.getPage(page);
        int lastPage = dao.lastPage(dao.count());
        service.serve(model, employees, pager.getPagesLinks(page, lastPage),
                page, lastPage);
        return "index";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable int id, Model model) throws SQLException,
            EmployeeNotFoundException {
        model.addAttribute("employee", dao.getEmployee(id));
        return "EmployeeView";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable int id, Model model)
            throws SQLException, EmployeeNotFoundException {
        Employee employee = dao.getEmployee(id);
        model.addAttribute("employee", employee);
        model.addAttribute("departments", dao.getDepartments());
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
        model.addAttribute("departments", dao.getDepartments());
        return "Insert";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insertEmployee(@RequestParam String name,
            @RequestParam(defaultValue = "off") String active,
            @RequestParam int department) throws SQLException {
        int intActive = active.equals("on") ? 1 : 0;
        dao.insertEmployee(name, intActive, department);
        return "SuccessInsert";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id, Model model)
            throws SQLException, EmployeeNotFoundException {
        dao.getEmployee(id);
        model.addAttribute("id", id);
        return "confirmationDelete";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteEmployee(@RequestParam int id) throws SQLException {
        dao.deleteEmployee(id);
        return "SuccessDelete";
    }

    @RequestMapping("/search/{name}/{page}")
    public String search(@PathVariable String name, @PathVariable int page,
            Model model) throws SQLException {
        int recordsCount = dao.getRecordsCountWithConditionOnName(name);
        int lastPage = dao.lastPage(recordsCount);
        ArrayList<Employee> employees = dao.searchByName(name, page);
        if (employees.size() == 0) {
            return "SearchWithNoSuccess";
        }
        if (page > lastPage || page < 1) {
            return "PageNotFound";
        }

        service.serve(name, model, employees, pager.getPagesLinks(1, lastPage),
                      page, lastPage);
        return "searchView";
    }

    @RequestMapping("/lastPage")
    public int lastPage() throws SQLException {
        return dao.lastPage(dao.count());
    }

    @ExceptionHandler(SQLException.class)
    public String sqlExceptionHandle() {
        return "SQLException";
    }
}


