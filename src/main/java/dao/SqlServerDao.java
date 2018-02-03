package dao;

import dbconnection.DatabaseConnection;
import exceptions.EmployeeNotFoundException;
import model.Department;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;

public class SqlServerDao {
    private Connection connection;
    private String sql = "select emp.empID, emp.empName, emp.empActive, dep.dpName from tblEmployees as emp" +
                         " INNER JOIN tblDepartments as dep ON emp.emp_dpID = dep.dpID ";
    public SqlServerDao(){
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean update(int UserID, String name, int active, int department) throws SQLException{
            PreparedStatement statement = connection.prepareStatement("UPDATE tblEmployees set empName = ?, emp_dpID = ?," +
                    " empActive=? where empID = ?");
            statement.setString(1, name);
            statement.setInt(2, department);
            statement.setInt(3, active);
            statement.setInt(4, UserID);
            if(statement.executeUpdate() == 1){
                return true;
            }
            else {
                return false;
            }
    }

    public boolean delete(int ID) throws SQLException{
            PreparedStatement statement = connection.prepareStatement("DELETE from tblEmployees WHERE empID = ?");
            statement.setInt(1, ID);
            if(statement.executeUpdate() == 1){
                return true;
            }
            else return false;
    }

    public Employee getEmployee(int id) throws SQLException, EmployeeNotFoundException{
        Statement statement = connection.createStatement();
        ArrayList<Employee> list = resultSetToEmployees(statement.executeQuery(sql + " where empID = " + id));
        if(list.size() == 0){
            throw new EmployeeNotFoundException(id);
        }
        else return list.get(0);
    }

    public void insertEmployee(String name, int active, int department) throws SQLException{
            PreparedStatement statement = connection.prepareStatement("insert into tblEmployees (empName, empActive, emp_dpID)\n" +
                    "values (?, ?, ?)");
            statement.setString(1, name);
            statement.setInt(2, active);
            statement.setInt(3, department);
            statement.executeUpdate();
    }

    public ArrayList<Employee> searchByName(String name, int page) throws SQLException{
        ArrayList<Employee> list;
        int recordsCount = countOfName(name);
        int lastPage = lastPage(recordsCount);
        int distanceToTheEnd = lastPage - page;
        if(page <= distanceToTheEnd) {
            PreparedStatement statement = connection.prepareStatement(sql + " WHERE empName LIKE '"
                    + name + "%' ORDER BY empName, empID OFFSET (?) ROWS FETCH FIRST ? ROWS ONLY;");
            statement.setInt(1, (page - 1) * 10);
            statement.setInt(2, 10);
            ResultSet set = statement.executeQuery();
            list = resultSetToEmployees(set);
        }
        else {
            int recordsOnLastPage = recordsCount % 10;
            if(recordsOnLastPage == 0){
                recordsOnLastPage = 10;
            }
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM (" + sql +
                        " WHERE empName LIKE '" + name + "%' ORDER BY empName, empID DESC OFFSET (?) " +
                        "ROWS FETCH FIRST ? ROWS ONLY) as a ORDER BY empName, empID");
                if (page == lastPage){
                    statement.setInt(1, 0);
                    statement.setInt(2, recordsOnLastPage);
                }
                else {
                    statement.setInt(1, recordsOnLastPage + (lastPage - page - 1)*10);
                    statement.setInt(2, 10);
                }
                ResultSet set = statement.executeQuery();
                list = resultSetToEmployees(set);
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

    public ArrayList<Employee> getPage(int page) throws SQLException{
        ArrayList<Employee> list = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(sql + "ORDER BY empID" +
                " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
        statement.setInt(1, (page - 1) * 10);
        statement.setInt(2, 10);
        ResultSet set = statement.executeQuery();
        list = resultSetToEmployees(set);
        return list;
    }

    public ArrayList<Department> getDepartments() throws SQLException{
        ArrayList<Department> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM tblDepartments");
        while (resultSet.next()){
            Department department = new Department();
            department.setDpID(resultSet.getInt(1));
            department.setDpName(resultSet.getString(2));
            list.add(department);
        }
        return list;
    }

    public ArrayList<Employee> getPage(int page, int pageSize) throws SQLException {
        ArrayList<Employee> employees;
        PreparedStatement statement = connection.prepareStatement
                (sql + " ORDER BY empID offset ? ROWS FETCH FIRST ? ROWS ONLY");
        statement.setInt(1, (page - 1) * pageSize);
        statement.setInt(2, pageSize);
        employees = resultSetToEmployees(statement.executeQuery());
        return employees;
    }

    public int count() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT Total_Rows= SUM(st.row_count) " +
                    "FROM sys.dm_db_partition_stats st WHERE " +
                    " object_id=OBJECT_ID('tblEmployees') AND (index_id < 2)");
        resultSet.next();
        return resultSet.getInt(1);
    }
    public int countOfName(String name) throws SQLException{
        Statement statement = connection.createStatement();
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
