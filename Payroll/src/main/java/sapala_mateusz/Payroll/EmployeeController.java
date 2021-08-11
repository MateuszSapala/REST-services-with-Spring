package sapala_mateusz.Payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping(path="/employees")
    CollectionModel<EntityModel<Employee>> getAllEmployee(){
        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).getAllEmployee()).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee){
        EntityModel<Employee> entityModel =
        assembler.toModel(repository.save(newEmployee));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> getOneEmployee(@PathVariable Long id){
        Employee employee = repository.findById(id).orElseThrow(()->new EmployeeNotFoundException(id));
        return  assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id){
          Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(()->{
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
          EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);
          return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                  .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
    }
}
