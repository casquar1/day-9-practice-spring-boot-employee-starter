package com.afs.restapi;

import com.afs.restapi.exception.EmployeeCreateException;
import com.afs.restapi.exception.EmployeeNotFoundException;
import com.afs.restapi.repository.EmployeeJpaRepository;
import com.afs.restapi.service.EmployeeService;
import com.afs.restapi.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @Mock
    private EmployeeJpaRepository mockedEmployeeJpaRepository;

    @BeforeEach
    void setUp() {
        mockedEmployeeJpaRepository = mock(EmployeeJpaRepository.class);
        employeeService = new EmployeeService(mockedEmployeeJpaRepository);
    }

    @Test
    void should_return_created_employee_when_create_given_service_and_employee_with_valid_age() {
        //given
        Employee employee = new Employee(1L, "Kate", 23, "Female", 5000);
        Employee savedEmployee = new Employee(1L, "Kate", 23, "Female", 5000);
        when(mockedEmployeeJpaRepository.save(employee)).thenReturn(savedEmployee);

        //when
        Employee employeeResponse = employeeService.create(employee);

        //then
        assertEquals(savedEmployee.getId(), employeeResponse.getId());
        assertEquals("Kate", employeeResponse.getName());
        assertEquals(23, employeeResponse.getAge());
        assertEquals("Female", employeeResponse.getGender());
        assertEquals(5000, employeeResponse.getSalary());
    }

    @Test
    void should_throw_exception_when_create_given_employee_service_and_employee_whose_age_is_less_than_18() {
        //given
        Employee employee = new Employee(1L, "Kate", 16, "Female", 5000);

        //when
        EmployeeCreateException employeeCreateException = assertThrows(EmployeeCreateException.class, () ->
                employeeService.create(employee));

        //then
        assertEquals("Employee must be 18-65 years old", employeeCreateException.getMessage());
    }

    @Test
    void should_throw_exception_when_create_given_employee_service_and_employee_whose_age_is_more_than_65() {
        //given
        Employee employee = new Employee(1L, "Kate", 66 , "Female", 5000);
        //when
        EmployeeCreateException employeeCreateException = assertThrows(EmployeeCreateException.class, () ->
                employeeService.create(employee));

        //then
        assertEquals("Employee must be 18-65 years old", employeeCreateException.getMessage());
    }

    @Test
    void should_return_employee_when_findById_given_employee_service_and_employee_id() {
        //given
        Employee employee = new Employee(1L, "Kate", 23, "Female", 5000);
        when(mockedEmployeeJpaRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        //when
        Employee employeeResponse = employeeService.findById(employee.getId());

        //then
        assertEquals(employee.getId(), employeeResponse.getId());
        assertEquals("Kate", employeeResponse.getName());
        assertEquals(23, employeeResponse.getAge());
        assertEquals("Female", employeeResponse.getGender());
        assertEquals(5000, employeeResponse.getSalary());
    }

    @Test
    void should_return_EmployeeNotFoundException_when_findById_given_employee_service_and_invalid_employee_id() {
        //given
        long id = 99L;

        //when
        EmployeeNotFoundException employeeNotFoundException = assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.findById(id));

        //then
        assertEquals("Employee id not found", employeeNotFoundException.getMessage());
    }

    @Test
    void should_not_update_employee_age_when_update_given_employee_service_and_null_age() {
        //given
        Employee employee = new Employee(1L, "Kate", 23, "Female", 5000);
        Employee updatedEmployee = new Employee(null, "Kate", null, "Female", 5500);
        when(mockedEmployeeJpaRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        //when
        employeeService.update(employee.getId(), updatedEmployee);

        //then
        verify(mockedEmployeeJpaRepository).save(argThat(tempEmployee -> {
            assertEquals(employee.getSalary(), tempEmployee.getSalary());
            assertEquals(employee.getId(), tempEmployee.getId());
            assertEquals(employee.getName(), tempEmployee.getName());
            assertEquals(employee.getAge(), tempEmployee.getAge());
            assertEquals(employee.getGender(), tempEmployee.getGender());
            return true;
        }));
    }

    @Test
    void should_not_update_employee_salary_when_update_given_employee_service_and_null_salary() {
        //given
        Employee employee = new Employee(1L, "Kate", 23, "Female", 5000);
        Employee updatedEmployee = new Employee(null, "Kate", 24, "Female", null);
        when(mockedEmployeeJpaRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        //when
        employeeService.update(employee.getId(), updatedEmployee);

        //then
        verify(mockedEmployeeJpaRepository).save(argThat(tempEmployee -> {
            assertEquals(employee.getSalary(), tempEmployee.getSalary());
            assertEquals(employee.getId(), tempEmployee.getId());
            assertEquals(employee.getName(), tempEmployee.getName());
            assertEquals(employee.getAge(), tempEmployee.getAge());
            assertEquals(employee.getGender(), tempEmployee.getGender());
            return true;
        }));
    }
}

