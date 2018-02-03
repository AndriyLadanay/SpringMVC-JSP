package dao;

import model.Employee;

import java.util.List;

public interface Dao {

    boolean create();

    boolean update();

    Employee read(int key);

    boolean delete(int key);

    int count();

    List<Employee> search(String name);

}
