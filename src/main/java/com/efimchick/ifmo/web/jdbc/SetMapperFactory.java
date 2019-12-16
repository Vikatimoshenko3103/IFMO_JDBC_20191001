package com.efimchick.ifmo.web.jdbc;

import com.efimchick.ifmo.web.jdbc.domain.Employee;

import java.util.Set;

public class SetMapperFactory {

    public SetMapper<Set<Employee>> employeesSetMapper() {
        return new EmployeeSetMapper();
    }
}
