package sapala_mateusz.Payroll;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Employee {
    private @Id @GeneratedValue Long id;
    private String name;
    private String role;

    public Employee() {}

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o){
        if(this ==o) return true;
        if(!(o instanceof Employee)) return false;
        Employee employee = (Employee)o;
        return this.id==employee.id &&
                this.name.equals(employee.name) &&
                this.role.equals(employee.role);
    }

    @Override
    public String toString(){
        return "Employee{id="+id+",name="+name+",role="+role+"}";
    }
}
