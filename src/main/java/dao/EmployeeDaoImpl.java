package dao;

import dbconnection.DatabaseConnection;
import model.Department;
import model.Employee;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

    @Autowired
    private DatabaseConnection db;
    private String sql = "select * from tblEmployees";

    @Override
    public Connection getConnection() {
        return db.getConnection();
    }

    @Override
    public boolean create(String name, boolean active, int dpID) throws SQLException{
        PreparedStatement statement = db.getConnection().prepareStatement(
                "insert into tblEmployees (empName, empActive, emp_dpID) values (?, ?, ?)");
        statement.setString(1, name);
        statement.setInt(2, (active ? 1 : 0));
        statement.setInt(3, dpID);
        if(statement.executeUpdate() == 1){
            return true;
        }
        else return false;
    }

    @Override
    public Employee read(int ID) throws SQLException {
        Statement statement = db.getConnection().createStatement();
        ArrayList<Employee> list = parseResultSetToEmployees(statement.executeQuery(sql + " where empID = " + ID));
        return list.size() != 0 ? list.get(0) : null;
    }

    @Override
    public boolean update(int ID, String name, boolean active, int dpID) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement(
                "UPDATE tblEmployees set empName = ?, emp_dpID = ?, empActive=? where empID = ?");
        statement.setString(1, name);
        statement.setInt(2, dpID);
        statement.setInt(3, active ? 1 : 0);
        statement.setInt(4, ID);
        if(statement.executeUpdate() == 1){
            return true;
        }
        else return false;
    }

    @Override
    public boolean delete(int ID) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement(
                "DELETE from tblEmployees WHERE empID = ?");
        statement.setInt(1, ID);
        if(statement.executeUpdate() == 1){
            return true;
        }
        else return false;
    }

    @Override
    public int count() throws SQLException {
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT Total_Rows= SUM(st.row_count) " +
                "FROM sys.dm_db_partition_stats st WHERE " +
                " object_id=OBJECT_ID('tblEmployees') AND (index_id < 2)");
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public int countOfName(String name) throws SQLException {
        Statement statement = db.getConnection().createStatement();
        String sql = "SELECT COUNT (*) FROM tblEmployees WHERE empName LIKE '" + name + "%'";
        ResultSet set = statement.executeQuery(sql);
        set.next();
        return set.getInt(1);
    }

    @Override
    public List<Employee> find(int take, int skip) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement(sql +
                " ORDER BY empID OFFSET ? ROWS FETCH FIRST ? ROWS ONLY;");
        statement.setInt(1, skip);
        statement.setInt(2, take);
        ResultSet resultSet = statement.executeQuery();
        return parseResultSetToEmployees(resultSet);
    }

    @Override
    public List<Employee> find(String name, int take, int skip) throws SQLException {
        PreparedStatement statement = db.getConnection().prepareStatement(sql + " WHERE empName LIKE ?" +
                " ORDER BY empName, empID OFFSET ? ROWS FETCH FIRST ? ROWS ONLY;");
        statement.setString(1, name + "%");
        statement.setInt(2, skip);
        statement.setInt(3, take);
        ResultSet resultSet = statement.executeQuery();
        return parseResultSetToEmployees(resultSet);
    }

    @Override
    public List<Department> departments() throws SQLException {
        ArrayList<Department> list = new ArrayList<>();
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM tblDepartments");
        while (resultSet.next()){
            Department department = new Department();
            department.setID(resultSet.getInt(1));
            department.setName(resultSet.getString(2));
            list.add(department);
        }
        return list;
    }

    private ArrayList<Employee> parseResultSetToEmployees(ResultSet resultSet) throws SQLException{
        ArrayList<Employee> list = new ArrayList<>();
        try {
            while (resultSet.next()){
                Employee employee = new Employee();
                int dpId = resultSet.getInt(4);
                Department department = new Department(dpId, getDepartmentNameByDpId(dpId));
                employee.setId(resultSet.getInt(1));
                employee.setName(resultSet.getString(2));
                employee.setActive(resultSet.getInt(3)== 1 ? true : false);
                employee.setDepartment(department);
                list.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    private String getDepartmentNameByDpId(int id) throws SQLException {
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select dpName from tblDepartments " +
                " where dpID = " + id);
        resultSet.next();
        return resultSet.getString(1);
    }
}
