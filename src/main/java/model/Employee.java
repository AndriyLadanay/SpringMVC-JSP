package model;

public class Employee {
    private int id;
    private String name;
    private boolean active;
    private Department department;

    public Employee() {

    }

    public Employee(String name, boolean active, Department department) {
        this.name = name;
        this.active = active;
        this.department = new Department();
        this.department.setID(department.getID());
        this.department.setName(department.getName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) {
        this.department = department;
    }

}
