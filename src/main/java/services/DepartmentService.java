package services;

import model.Department;

import java.sql.SQLException;
import java.util.List;

public interface DepartmentService {
    List<Department> getAll() throws SQLException;
}
