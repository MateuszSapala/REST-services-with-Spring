package sapala_mateusz.Payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeModelAssembler assembler) {
        this.employeeRepository = employeeRepository;
        this.assembler = assembler;
    }

    @GetMapping(path="/employees")
    CollectionModel<EntityModel<Employee>> getAllEmployee(){
        List<EntityModel<Employee>> employees = employeeRepository.findAll().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).getAllEmployee()).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee){
        EntityModel<Employee> entityModel =
        assembler.toModel(employeeRepository.save(newEmployee));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> getOneEmployee(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException(id));
        return  assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id){
          Employee updatedEmployee = employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return employeeRepository.save(employee);
                })
                .orElseGet(()->{
                    newEmployee.setId(id);
                    return employeeRepository.save(newEmployee);
                });
          EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                  .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id){
        employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
