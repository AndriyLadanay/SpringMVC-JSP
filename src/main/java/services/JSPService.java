package services;

import org.springframework.ui.Model;
import model.Employee;
import java.util.ArrayList;

public class JSPService {
    public void serve(Model model, ArrayList<Employee> employees, ArrayList<Integer> numbers, int currentPage, int lastPage){
        model.addAttribute(employees);
        model.addAttribute(numbers);
        model.addAttribute(currentPage);
        model.addAttribute(lastPage);
    }
    public void serve(String nameToSearch, Model model, ArrayList<Employee> employees, ArrayList<Integer> numbers, int currentPage, int lastPage){
        model.addAttribute(nameToSearch);
        model.addAttribute(employees);
        model.addAttribute(numbers);
        model.addAttribute(currentPage);
        model.addAttribute(lastPage);
    }
}
