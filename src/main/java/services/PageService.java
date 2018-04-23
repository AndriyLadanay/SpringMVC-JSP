package services;

import exceptions.PageDoesntExistException;
import model.Employee;

import java.sql.SQLException;

import java.util.List;

public interface PageService {

    List<Employee> page(int page, int pageSize) throws SQLException, PageDoesntExistException;

    List<Employee> pageOfSearch(String firstName, int page, int pageSize) throws SQLException, PageDoesntExistException;

    int getTotalPages(int pageSize) throws SQLException;

    int getTotalPages(String firstName, int pageSize) throws SQLException;

    List<Integer> getPagesNumbers(int page,  int totalPages);

}