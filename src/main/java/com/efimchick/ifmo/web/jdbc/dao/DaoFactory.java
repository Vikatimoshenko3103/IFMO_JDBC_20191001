package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoFactory {
    private ResultSet RequestData(String sql) throws SQLException {
        ConnectionSource source = ConnectionSource.instance();
        Connection connection = source.createConnection();
        return connection.createStatement().executeQuery(sql);
    }

    private interface Converter<T>{
        T Convert(ResultSet res);
    }

    private class EmployeeConverter implements Converter<Employee> {

        @Override
        public Employee Convert(ResultSet res) {
            return null;
        }
    }

    private class DepartmentConverter implements Converter<Department> {

        @Override
        public Department Convert(ResultSet res) {
            return null;
        }
    }

    private <T>List<T> GetResultList(String sql, Converter<T> converter){
        List<T> temp = new ArrayList<>();
        try {
            ResultSet res = RequestData(sql);
            while (res.next()){
                temp.add(converter.Convert(res));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public EmployeeDao employeeDAO() {
        return new EmployeeDao() {
            @Override
            public List<Employee> getByDepartment(Department department) {
                return GetResultList("", new EmployeeConverter());
            }

            @Override
            public List<Employee> getByManager(Employee employee) {
                return GetResultList("",new EmployeeConverter());
            }

            @Override
            public Optional<Employee> getById(BigInteger Id) {
                return Optional.empty();
            }

            @Override
            public List<Employee> getAll() {
                return GetResultList("",new EmployeeConverter());
            }

            @Override
            public Employee save(Employee employee) {
                return null;
            }

            @Override
            public void delete(Employee employee) {

            }
        };
    }

    public DepartmentDao departmentDAO() {
        return new DepartmentDao() {
            @Override
            public Optional<Department> getById(BigInteger Id) {
                return Optional.empty();
            }

            @Override
            public List<Department> getAll() {
                return null;
            }

            @Override
            public Department save(Department department) {
                return null;
            }

            @Override
            public void delete(Department department) {

            }
        };
    }
}
