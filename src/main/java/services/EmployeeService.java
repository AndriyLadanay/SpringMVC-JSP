package services;

import exceptions.EmployeeNotFoundException;
import model.Employee;

import java.sql.SQLException;

public interface EmployeeService {

    void create(String firstName, String active, int dpID) throws SQLException;

    Employee read(int Id) throws SQLException, EmployeeNotFoundException;

    void update(int Id, String firstName, String active, int dpID) throws SQLException,
            EmployeeNotFoundException;

    void delete(int Id) throws SQLException, EmployeeNotFoundException;

}
