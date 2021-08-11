package sapala_mateusz.Payroll;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER_ORDER")
public class Order {
    private @Id @GeneratedValue Long id;
    private String description;
    private Status status;

    public Order() {}

    public Order(String description, Status status) {
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Order)) return false;
        Order order = (Order)o;
        return getId().equals(order.getId()) &&
                getDescription().equals(order.getDescription()) &&
                getStatus().equals(order.getStatus());
    }

    @Override
    public String toString() {
        return "Order{id="+getId()+", description:'"+getDescription()+"', status='"+getStatus()+"'}";
    }
}