package com.efimchick.ifmo.web.jdbc;

import com.efimchick.ifmo.web.jdbc.domain.Employee;

public class RowMapperFactory {

    public RowMapper<Employee> employeeRowMapper() {
        return new EmployeeMap();
    }
}
