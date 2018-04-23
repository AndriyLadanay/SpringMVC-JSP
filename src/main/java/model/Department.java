package model;

public class Department {
    private int ID;
    private String name;

    public Department(){

    }

    public Department(int id, String name){
        this.ID = id;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
