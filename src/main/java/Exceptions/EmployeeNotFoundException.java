package Exceptions;

public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(int id){
        super("EmployeeNotFoundException with id="+id);
    }
}
