package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.domain.Department;

public interface DepartmentDao {
    Department getById(Long Id);
    Department getByName(String Name);
}