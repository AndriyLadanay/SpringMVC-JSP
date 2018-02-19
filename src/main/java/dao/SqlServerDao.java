package dao;

import dbconnection.DatabaseConnection;
import exceptions.EmployeeNotFoundException;
import exceptions.PageDoesntExistException;
import model.Department;
import model.Employee;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;

public class SqlServerDao implements EmployeeDao {

    @Autowired
    private DatabaseConnection db;
    private String sql = "select emp.empID, emp.empName, emp.empActive, dep.dpName from tblEmployees as emp" +
                         " INNER JOIN tblDepartments as dep ON emp.emp_dpID = dep.dpID ";

    public boolean update(int ID, String name, int active, int department) throws SQLException{
            PreparedStatement statement = db.getConnection().prepareStatement("UPDATE tblEmployees set empName = ?, emp_dpID = ?," +
                    " empActive=? where empID = ?");
            statement.setString(1, name);
            statement.setInt(2, department);
            statement.setInt(3, active);
            statement.setInt(4, ID);
            if(statement.executeUpdate() == 1){
                return true;
            }
            else {
                return false;
            }
    }

    public boolean delete(int ID) throws SQLException{
            PreparedStatement statement = db.getConnection().prepareStatement("DELETE from tblEmployees WHERE empID = ?");
            statement.setInt(1, ID);
            if(statement.executeUpdate() == 1){
                return true;
            }
            else return false;
    }

    public Employee read(int id) throws SQLException, EmployeeNotFoundException{
        Statement statement = db.getConnection().createStatement();
        ArrayList<Employee> list = resultSetToEmployees(statement.executeQuery(sql + " where empID = " + id));
        if(list.size() == 0){
            throw new EmployeeNotFoundException(id);
        }
        else return list.get(0);
    }

    public void insert(String name, int active, int department) throws SQLException{
            PreparedStatement statement = db.getConnection().prepareStatement("insert into tblEmployees (empName, empActive, emp_dpID)\n" +
                    "values (?, ?, ?)");
            statement.setString(1, name);
            statement.setInt(2, active);
            statement.setInt(3, department);
            System.out.println(statement.executeUpdate());
    }

    public ArrayList<Employee> search(String name, int page, int pageSize) throws SQLException,
            PageDoesntExistException {
        PreparedStatement statement = db.getConnection().prepareStatement(sql + " WHERE empName LIKE '"
                + name + "%' ORDER BY empName, empID OFFSET (?) ROWS FETCH FIRST ? ROWS ONLY;");
        statement.setInt(1, (page - 1) * pageSize);
        statement.setInt(2, pageSize);
        ResultSet set = statement.executeQuery();
        ArrayList<Employee> list = resultSetToEmployees(set);
        if(list.size() == 0) {
            throw new PageDoesntExistException();
        }
        return list;
    }

    private ArrayList<Employee> resultSetToEmployees(ResultSet set){
        ArrayList<Employee> list = new ArrayList<>();
        try {
            while (set.next()){
                Employee employee = new Employee();
                employee.setId(set.getInt(1));
                employee.setName(set.getString(2));
                employee.setActive(set.getInt(3)== 1 ? "yes" : "no");
                employee.setDepartment(set.getString(4));
                list.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Department> departments() throws SQLException{
        ArrayList<Department> list = new ArrayList<>();
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM tblDepartments");
        while (resultSet.next()){
            Department department = new Department();
            department.setDpID(resultSet.getInt(1));
            department.setDpName(resultSet.getString(2));
            list.add(department);
        }
        return list;
    }

    public ArrayList<Employee> page(int page, int pageSize) throws SQLException,
            PageDoesntExistException {
        PreparedStatement statement = db.getConnection().prepareStatement
                (sql + " ORDER BY empID offset ? ROWS FETCH FIRST ? ROWS ONLY");
        statement.setInt(1, (page - 1) * pageSize);
        statement.setInt(2, pageSize);
        ArrayList<Employee> employees = resultSetToEmployees(statement.executeQuery());
        if (employees.size() == 0) {
            throw new PageDoesntExistException();
        }
        return employees;
    }

    public int count() throws SQLException {
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT Total_Rows= SUM(st.row_count) " +
                "FROM sys.dm_db_partition_stats st WHERE " +
                " object_id=OBJECT_ID('tblEmployees') AND (index_id < 2)");
        resultSet.next();
        return resultSet.getInt(1);
    }
    public int countOfName(String name) throws SQLException{
        Statement statement = db.getConnection().createStatement();
        String sql = "SELECT COUNT (*) FROM tblEmployees WHERE empName LIKE '" + name + "%'";
        ResultSet set = statement.executeQuery(sql);
        set.next();
        return set.getInt(1);
    }

    public int lastPage(int recordsAmount){
        int lastPage;
        if(recordsAmount % 10 == 0){
            lastPage = recordsAmount/10;
        }
        else {
            lastPage = recordsAmount/10 + 1;
        }
        return lastPage;
    }
}
