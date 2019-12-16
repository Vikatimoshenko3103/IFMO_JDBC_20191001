package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoFactory {
    private int UpdateRequest(String sql) throws SQLException {
        ConnectionSource source = ConnectionSource.instance();
        Connection connection = source.createConnection();
        return connection.createStatement().executeUpdate(sql);
    }

    private ResultSet RequestData(String sql) throws SQLException {
        ConnectionSource source = ConnectionSource.instance();
        Connection connection = source.createConnection();
        return connection.createStatement().executeQuery(sql);
    }

    private interface Converter<T>{
        T Convert(ResultSet res) throws SQLException;
        String ToStringInsertValues(T value);
        String ToStringUpdateValues(T value);
    }

    private class EmployeeConverter implements Converter<Employee> {

        @Override
        public Employee Convert(ResultSet res) throws SQLException {
            return  new Employee(new BigInteger(res.getString("id")),
                    new FullName(res.getString("firstname"), res.getString("lastname"), res.getString("middlename")),
                    Position.valueOf(res.getString("position")),
                    LocalDate.parse(res.getString("hiredate")),
                    new BigDecimal(res.getString("salary")),
                    BigInteger.valueOf(res.getInt("manager")),
                    BigInteger.valueOf(res.getInt("department")));
        }

        @Override
        public String ToStringInsertValues(Employee value) {
            return "("
                    + String.join(",",
                    DataFinal(value.getId().toString()),
                    DataFinal(value.getFullName().getFirstName()),
                    DataFinal(value.getFullName().getLastName()),
                    DataFinal(value.getFullName().getMiddleName()),
                    DataFinal(value.getPosition().toString()),
                    DataFinal(value.getManagerId().toString()),
                    DataFinal(value.getHired().toString()),
                    DataFinal(value.getSalary().toString()),
                    DataFinal(value.getDepartmentId().toString())
            )
                    + ")";
        }

        private String DataFinal(String input){
            return "'" + input + "'";
        }

        @Override
        public String ToStringUpdateValues(Employee value) {
            return ""
                    + String.join(",",
                    "id = " + DataFinal(value.getId().toString()),
                    "firstname = " + DataFinal(value.getFullName().getFirstName()),
                    "lastname = " + DataFinal(value.getFullName().getMiddleName()),
                    "middlename = " + DataFinal(value.getFullName().getLastName()),
                    "position = " + DataFinal(value.getPosition().toString()),
                    "hiredate = " + DataFinal(value.getHired().toString()),
                    "salary = " + DataFinal(value.getSalary().toString()),
                    "manager = " + DataFinal(value.getManagerId().toString()),
                    "department = " + DataFinal(value.getDepartmentId().toString())
            )
                    + "";
        }

    }

    private class DepartmentConverter implements Converter<Department> {

        @Override
        public Department Convert(ResultSet res) throws SQLException {
            return new Department(
                    new BigInteger(res.getString("id")),
                    res.getString("name"),
                    res.getString("location")
            );
        }

        private String DataFinal(String input){
            return "'" + input + "'";
        }

        @Override
        public String ToStringInsertValues(Department value) {
            return "("+ String.join(",",
                    DataFinal(value.getId().toString()),
                    DataFinal(value.getName()),
                    DataFinal(value.getLocation())
            ) + ")";
        }

        @Override
        public String ToStringUpdateValues(Department value) {
            return String.join(",",
                    "id = " + DataFinal(value.getId().toString()),
                    "name = " + DataFinal(value.getName()),
                    "location = " + DataFinal(value.getLocation())
            );
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
                return GetResultList("SELECT * FROM employee WHERE department = " + department.getId(), new EmployeeConverter());
            }

            @Override
            public List<Employee> getByManager(Employee employee) {
                return GetResultList("SELECT * FROM employee WHERE manager = " + employee.getId(),new EmployeeConverter());
            }

            @Override
            public Optional<Employee> getById(BigInteger Id) {
                List<Employee> resultList = GetResultList("SELECT * FROM employee WHERE id = " + Id, new EmployeeConverter());
                if (resultList.size()>0){
                    return Optional.of(resultList.get(0));
                }
                return Optional.empty();
            }

            @Override
            public List<Employee> getAll() {
                return GetResultList("SELECT * FROM employee",new EmployeeConverter());
            }

            @Override
            public Employee save(Employee employee) {
                String sql = "UPDATE EMPLOYEE SET " + new EmployeeConverter().ToStringUpdateValues(employee) + " WHERE id = " + employee.getId();
                try {
                    if (UpdateRequest(sql) == 0){
                        sql = "INSERT INTO EMPLOYEE VALUES " + new EmployeeConverter().ToStringInsertValues(employee);
                        UpdateRequest(sql);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return employee;
            }

            @Override
            public void delete(Employee employee) {
                try {
                    UpdateRequest("DELETE FROM EMPLOYEE WHERE id = " + employee.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public DepartmentDao departmentDAO() {
        return new DepartmentDao() {
            @Override
            public Optional<Department> getById(BigInteger Id) {
                List<Department> resultList = GetResultList("SELECT * FROM DEPARTMENT WHERE id = " + Id, new DepartmentConverter());
                if (resultList.size()>0){
                    return Optional.of(resultList.get(0));
                }
                return Optional.empty();
            }

            @Override
            public List<Department> getAll() {
                return GetResultList("SELECT * FROM DEPARTMENT",new DepartmentConverter());
            }

            @Override
            public Department save(Department department) {
                String sql = "UPDATE DEPARTMENT SET " + new DepartmentConverter().ToStringUpdateValues(department) + " WHERE id = " + department.getId();
                try {
                    if (UpdateRequest(sql) == 0){
                        sql = "INSERT INTO DEPARTMENT VALUES " + new DepartmentConverter().ToStringInsertValues(department);
                        UpdateRequest(sql);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println(sql);
                }
                return department;
            }

            @Override
            public void delete(Department department) {
                try {
                    UpdateRequest("DELETE FROM DEPARTMENT WHERE id = " + department.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
