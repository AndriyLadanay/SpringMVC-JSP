import Dao.Dao;
import Exceptions.EmployeeNotFoundException;
import Model.Employee;
import Pager.Pager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PagerTest {
    public static void main(String[] args) throws SQLException, EmployeeNotFoundException {
        Dao dao = new Dao();
        System.out.println(dao.getEmployee(13));
    }
}
