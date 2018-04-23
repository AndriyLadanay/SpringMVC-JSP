package dao;

import dbconnection.DatabaseConnection;
import exceptions.EmployeeNotFoundException;
import exceptions.PageDoesntExistException;
import model.Department;
import model.Employee;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface EmployeeDao {

    Connection getConnection();

    boolean create(String name, boolean active, int dpID) throws SQLException;

    Employee read(int ID) throws SQLException;

    boolean update(int ID, String name, boolean active, int dpID) throws SQLException;

    boolean delete(int ID) throws SQLException;

    int count() throws SQLException;

    int countOfName(String name) throws SQLException;

    List<Employee> find(int take, int skip) throws SQLException;

    List<Employee> find(String name, int take, int skip) throws SQLException;

    List<Department> departments() throws SQLException;

}
