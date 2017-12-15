package Controller;

import Dao.Dao;
import Exceptions.EmployeeNotFoundException;
import Exceptions.PageDoesntExistException;
import Pager.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import Model.Employee;

@Controller
@SessionAttributes({"currentPage", "lastID"})
@RequestMapping("/employees")
public class HelloController {
    Dao dao = new Dao();
    Pager pager = new Pager();


    @RequestMapping(method = RequestMethod.GET)
    public String startPage(Model model) throws SQLException {
        ArrayList<Employee> employees = dao.getPage(1);
        int recordsCount = dao.getRecordsCount();
        int lastPage = dao.getLastPage(recordsCount);
        model.addAttribute("lastID", employees.get(0).getId());
        model.addAttribute("currentPage", 1);
        model.addAttribute("numbers", pager.getPagesLinks(1, lastPage));
        model.addAttribute("employees", employees);
        model.addAttribute("previous", 0);
        model.addAttribute("next", 2);
        model.addAttribute("lastPage", lastPage);
        return "index";
    }

    @RequestMapping("/{page}")
    public String getPage(Model model, @PathVariable int page,
                                         @ModelAttribute("currentPage") int currentPage,
                                         @ModelAttribute("lastID") int lastID) throws SQLException,
                                                                                      PageDoesntExistException{
        int recordsCount = dao.getRecordsCount();
        int lastPage = dao.getLastPage(recordsCount);
        if (page > lastPage || page < 1){
            throw new PageDoesntExistException();
        }
        ArrayList<Employee> employees = dao.getPage(page, currentPage, lastID);
        model.addAttribute("numbers", pager.getPagesLinks(page, lastPage));
        model.addAttribute("employees", employees);
        model.addAttribute("previous", page - 1);
        model.addAttribute("next", page + 1);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("lastID", employees.get(0).getId());
        return "index";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable int id, Model model) throws SQLException, EmployeeNotFoundException{
        model.addAttribute("employee", dao.getEmployee(id));
        return "EmployeeView";
    }
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable int id, Model model) throws SQLException, EmployeeNotFoundException{
        dao.getEmployee(id);
        model.addAttribute("id", id);
        model.addAttribute("departments", dao.getDepartments());
        return "EditForm";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editEmployee (@RequestParam int id,
                                 @RequestParam String name,
                                 @RequestParam(defaultValue = "off") String active,
                                 @RequestParam int department) throws SQLException {
        int intActive = active.equals("on") ? 1 : 0;
        dao.updateEmployee(id, name, intActive, department);
        return "SuccessEdit";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String insert(Model model) throws SQLException{
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
    public String delete(@PathVariable int id, Model model) throws SQLException, EmployeeNotFoundException {
        dao.getEmployee(id);
        model.addAttribute("id", id);
        return "confirmationDelete";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteEmployee(@RequestParam int id) throws SQLException{
        dao.deleteEmployee(id);
        return "SuccessDelete";
    }

    @RequestMapping("/search/{name}/{page}")
    public String search(@PathVariable String name, @PathVariable int page,
                         Model model) throws SQLException, PageDoesntExistException
                                                                   {
        int recordsCount = dao.getRecordsCountWithConditionOnName(name);
        int lastPage = dao.getLastPage(recordsCount);
        ArrayList<Employee> employees = dao.searchByName(name, page);
        if(employees.size() == 0){
            return "SearchWithNoSuccess";
        }
        if(page > lastPage || page < 1){
            throw new PageDoesntExistException();
        }
        model.addAttribute("employees", employees);
        model.addAttribute("nameToSearch", name);
        model.addAttribute("previous", page - 1);
        model.addAttribute("next", page + 1);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("numbers", pager.getPagesLinks(page, lastPage));
        return "searchView";
    }

    @ExceptionHandler(SQLException.class)
    public String SQLExceptionHandle(){
        return "SQLException";
    }
    @ExceptionHandler(EmployeeNotFoundException.class)
    public String EmployeeNotFoundHandle(){
        return "EmployeeNotFound";
    }
    @ExceptionHandler(PageDoesntExistException.class)
    public String PageNotFoundHandle(){
        return "PageNotFound";
    }
}


