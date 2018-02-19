package dao;

import exceptions.EmployeeNotFoundException;
import exceptions.PageDoesntExistException;
import model.Department;
import model.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface EmployeeDao {

    void insert(String name, int active, int department) throws SQLException;

    boolean update(int ID, String name, int active, int department) throws SQLException;

    Employee read(int ID) throws SQLException, EmployeeNotFoundException;

    boolean delete(int ID) throws SQLException;

    int count() throws SQLException;

    int countOfName(String name) throws SQLException;

    ArrayList<Employee> page(int page, int pageSize) throws SQLException, PageDoesntExistException;

    ArrayList<Employee> search(String name, int page, int pageSize) throws SQLException, PageDoesntExistException;

    int lastPage(int recordsAmount) ;

    List<Department> departments() throws SQLException;

}
