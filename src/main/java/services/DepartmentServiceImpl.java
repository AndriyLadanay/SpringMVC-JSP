package services;

import dao.EmployeeDao;
import model.Department;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private EmployeeDao dao;

    @Override
    public List<Department> getAll() throws SQLException {
        return dao.departments();
    }
}
