package services;

import dao.EmployeeDao;
import exceptions.PageDoesntExistException;
import model.Employee;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PageServiceImpl implements PageService {

    @Autowired
    protected EmployeeDao dao;

    @Override
    public List<Employee> page(int page, int pageSize) throws SQLException, PageDoesntExistException {
        List<Employee> employees = dao.find(pageSize, (page - 1) * pageSize);
        if(employees.isEmpty()) throw new PageDoesntExistException() ;
        return employees;
    }

    @Override
    public List<Employee> pageOfSearch(String firstName, int page, int pageSize) throws SQLException,
            PageDoesntExistException {
        List<Employee> employees = dao.find(firstName, pageSize, (page - 1) * pageSize);
        if(employees.isEmpty()) throw new PageDoesntExistException();
        return employees;
    }

    @Override
    public int getTotalPages(int pageSize) throws SQLException {
        int recordsAmount = dao.count();
        return getTotalPages(recordsAmount, pageSize);
    }

    @Override
    public int getTotalPages(String firstName, int pageSize) throws SQLException {
        int recordsAmount = dao.countOfName(firstName);
        return getTotalPages(recordsAmount, pageSize);
    }

    @Override
    public List<Integer> getPagesNumbers(int currentPage, int totalPages) {
        int start;
        int end;
        if (currentPage % 10 == 0){
            end = currentPage;
            start = end - 9;
            if(totalPages < end){
                end = totalPages;
            }
        }
        else {
            start = currentPage / 10 * 10 + 1;
            end = start + 9;
            if(totalPages < end){
                end = totalPages;
            }
        }
        ArrayList<Integer> pages = range(start, end);
        return  pages;
    }

    private int getTotalPages(int recordsAmount, int pageSize){
        int lastPageNumber;
        if(recordsAmount % pageSize == 0){
            lastPageNumber = recordsAmount/pageSize;
        }
        else {
            lastPageNumber = recordsAmount/pageSize + 1;
        }
        return lastPageNumber;
    }

    private ArrayList<Integer> range(int start, int end){
        ArrayList<Integer> numbers = new ArrayList<>();
        for(int i = start; i <= end; i++){
            numbers.add(i);
        }
        return numbers;
    }
}
