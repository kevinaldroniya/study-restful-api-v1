import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import spring.rest.v1.dto.EmployeeDto;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestClientTest {
    private final RestClient restClient;

    public RestClientTest(){
        restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }


    @Test
    public void createEmployeeTest(){
        EmployeeDto newEmployee = new EmployeeDto(
                null,"admin","admin","admin@admin.com"
        );

        EmployeeDto savedEmployee = restClient.post()
                .uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newEmployee)
                .retrieve()
                .body(EmployeeDto.class);

        System.out.println(savedEmployee.toString());
    }

    @Test
    public void getEmployeeByIdTest(){
        Long employeeId = 1L;

        EmployeeDto employeeDto = restClient.get()
                .uri("/api/employees/{employeeId}", employeeId)
                .retrieve()
                .body(EmployeeDto.class);

        System.out.println(employeeDto);
    }

    @Test
    public void getAllEmployeesTest(){
        List<EmployeeDto> employees = restClient.get()
                .uri("/api/employees")
                .retrieve()
                .body(new ParameterizedTypeReference<List<EmployeeDto>>() {
                });

        employees.forEach(employeeDto -> {
            System.out.println(employeeDto.toString());
        });
    }

    @Test
    public void updateEmployeeTest(){
        Long employeeId = 4L;

        EmployeeDto updatedEmployee = new EmployeeDto();
        updatedEmployee.setFirstName("Yuta");
        updatedEmployee.setLastName("Okotsu");
        updatedEmployee.setEmail("yuta.okotsu@jjk.com");

        EmployeeDto employeeDto = restClient.put()
                .uri("/api/employees/{employeeId}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedEmployee)
                .retrieve()
                .body(EmployeeDto.class);

        System.out.println(employeeDto);
    }

    @Test
    public void deleteEmployeeTest(){
        Long employeeId = 3L;

        String response = restClient.delete()
                .uri("/api/employees/{employeeId}", employeeId)
                .retrieve()
                .body(String.class);

        System.out.println(response);
    }

    @Test
    public void exceptionHandlingClientErrorDemo(){
        HttpClientErrorException httpClientErrorException = Assertions.assertThrows(HttpClientErrorException.class,
                () -> {
                    EmployeeDto employeeDto = restClient.get()
                            .uri("/employees/404")
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .body(EmployeeDto.class);
                });

        Assertions.assertEquals(404, httpClientErrorException.getStatusCode().value());
    }

    @Test
    public void exceptionHandlingServerErrorDemo(){
        HttpServerErrorException httpServerErrorException = Assertions.assertThrows(HttpServerErrorException.class,
                () -> {
                    EmployeeDto employeeDto = restClient.get()
                            .uri("/api/employees/500")
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .body(EmployeeDto.class);
                });

        Assertions.assertEquals(500, httpServerErrorException.getStatusCode().value());
    }

}
