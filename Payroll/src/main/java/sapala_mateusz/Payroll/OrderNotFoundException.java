package sapala_mateusz.Payroll;

public class OrderNotFoundException extends RuntimeException {
    OrderNotFoundException(Long id){
        super("Could not found order "+id);
    }
}
