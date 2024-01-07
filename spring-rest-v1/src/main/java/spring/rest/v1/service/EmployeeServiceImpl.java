package spring.rest.v1.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.rest.v1.converter.EmployeeConverter;
import spring.rest.v1.dto.EmployeeDto;
import spring.rest.v1.entity.Employee;
import spring.rest.v1.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeConverter.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeConverter.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        // we need to check whether employee with given id is exist in DB or not
        Employee existingEmployee = employeeRepository.findById(employeeId).
                orElseThrow(() ->
                    new IllegalArgumentException(
                            "Employee not exits with a given id :"+employeeId
                    )
                );
        return EmployeeConverter.mapToEmployeeDto(existingEmployee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDto> employeeDtoList = employees.stream().map(employee -> EmployeeConverter.mapToEmployeeDto(employee))
                .collect(Collectors.toList());
        return employeeDtoList;
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        Employee employeeExist = employeeRepository.findById(employeeDto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Employee doesn't exist with a given id :" + employeeDto.getId()
                ));
        Employee employee = EmployeeConverter.mapToEmployee(employeeDto);
        Employee updateEmployee = employeeRepository.save(employee);
        EmployeeDto employeeDto1 = EmployeeConverter.mapToEmployeeDto(updateEmployee);
        return employeeDto1;
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).
                orElseThrow(() -> new IllegalArgumentException(
                        "Employee doesn't exist with a given id :" + employeeId
                ));
        employeeRepository.delete(employee);
    }
}
