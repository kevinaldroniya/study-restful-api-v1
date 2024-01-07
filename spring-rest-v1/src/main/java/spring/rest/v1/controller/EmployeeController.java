package spring.rest.v1.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.rest.v1.dto.EmployeeDto;
import spring.rest.v1.service.EmployeeService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(path = "employees")
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto employee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping(path = "employees/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("employeeId") Long employeeId){
        EmployeeDto employee = employeeService.getEmployeeById(employeeId);
//        return new ResponseEntity<>(employee, HttpStatus.OK);
        return ResponseEntity.ok(employee);
    }

    @GetMapping(path = "/employees")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        List<EmployeeDto> employees = employeeService.getAllEmployees();
//        return new ResponseEntity<>(employees, HttpStatus.OK);
        return ResponseEntity.ok(employees);
    }

    @PutMapping(path = "/employees/{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable("employeeId")Long employeeId,
            @RequestBody EmployeeDto employeeDto
    ){
        employeeDto.setId(employeeId);
        EmployeeDto updateEmployee = employeeService.updateEmployee(employeeDto);
        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }

    @DeleteMapping(path = "/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable("employeeId")Long employeeId
    ){
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>("Employee Delete Success", HttpStatus.OK);
    }
}
