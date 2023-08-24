package com.afs.restapi;

import com.afs.restapi.entity.Company;
import com.afs.restapi.entity.Employee;
import com.afs.restapi.exception.CompanyNotFoundException;
import com.afs.restapi.exception.EmployeeNotFoundException;
import com.afs.restapi.repository.CompanyJpaRepository;
import com.afs.restapi.repository.EmployeeJpaRepository;
import com.afs.restapi.service.CompanyService;
import com.afs.restapi.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompanyServiceTest {

    private CompanyService companyService;

    @Mock
    private CompanyJpaRepository mockedCompanyJpaRepository;

    @Mock
    private EmployeeJpaRepository mockedEmployeeJpaRepository;

    @BeforeEach
    void setUp() {
        mockedCompanyJpaRepository = mock(CompanyJpaRepository.class);
        companyService = new CompanyService(mockedCompanyJpaRepository, mockedEmployeeJpaRepository);
    }

    @Test
    void should_return_created_company_when_create_given_company_service_and_valid_id() {
        //given
        Company company = new Company(1L, "Fully Booked");
        when(mockedCompanyJpaRepository.save(company)).thenReturn(company);

        //when
        Company companyResponse = companyService.create(company);

        //then
        assertEquals(company.getId(), companyResponse.getId());
        assertEquals(company.getName(), companyResponse.getName());
    }

    @Test
    void should_return_company_when_findById_given_company_service_and_company_id() {
        //given
        Company company = new Company(1L, "Fully Booked");
        when(mockedCompanyJpaRepository.findById(company.getId())).thenReturn(Optional.of(company));

        //when
        Company companyResponse = companyService.findById(company.getId());

        //then
        assertEquals(company.getId(), companyResponse.getId());
        assertEquals(company.getName(), companyResponse.getName());
    }

    @Test
    void should_return_companyNotFoundException_when_findById_given_company_service_and_invalid_id() {
        //given
        long id = 99L;

        //when
        CompanyNotFoundException companyNotFoundException = assertThrows(CompanyNotFoundException.class,
                () -> companyService.findById(id));

        //then
        assertEquals("Company id not found", companyNotFoundException.getMessage());
    }
}
