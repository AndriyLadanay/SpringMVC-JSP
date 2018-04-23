package services;

import dao.EmployeeDao;
import exceptions.EmployeeNotFoundException;
import model.Employee;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDao dao;


    @Override
    public void create(String firstName, String active, int dpID) throws SQLException {
        boolean boolActive = active.equals("on") ? true : false;
        dao.create(firstName, boolActive, dpID);
    }

    @Override
    public Employee read(int Id) throws EmployeeNotFoundException, SQLException {
        Employee employee = dao.read(Id);
        if(employee != null){
            return employee;
        }
        else throw new EmployeeNotFoundException(Id);
    }

    @Override
    public void update(int Id, String firstName, String active, int dpID) throws SQLException,
            EmployeeNotFoundException {
        dao.read(Id);
        boolean boolActive = active.equals("on") ? true : false;
        dao.update(Id, firstName, boolActive, dpID);
    }

    @Override
    public void delete(int Id) throws SQLException, EmployeeNotFoundException {
        dao.read(Id);
        dao.delete(Id);
    }
}
