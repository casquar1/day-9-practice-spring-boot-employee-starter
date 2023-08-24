package com.afs.restapi;

import com.afs.restapi.repository.EmployeeJpaRepository;
import com.afs.restapi.service.EmployeeService;
import com.afs.restapi.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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
}

