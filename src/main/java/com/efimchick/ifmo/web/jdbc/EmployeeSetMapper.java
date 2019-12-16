package com.efimchick.ifmo.web.jdbc;

import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class EmployeeSetMapper implements SetMapper<Set<Employee>> {

    private EmployeeInfo GenerateEmployee(ResultSet resultSet) throws SQLException {
        BigInteger managerId= BigInteger.valueOf(-1);

        if (resultSet.getString("manager") != null){
            managerId = BigInteger.valueOf(resultSet.getInt("manager"));
        }
        return new EmployeeInfo(
                BigInteger.valueOf(resultSet.getInt("id")),
                new FullName(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("middleName")
                ),
                Position.valueOf(resultSet.getString("position")),
                LocalDate.parse(resultSet.getString("hireDate")),
                resultSet.getBigDecimal("salary"),
                managerId
        );
    }

    public class EmployeeInfo extends Employee{

        private final BigInteger managerId;

        EmployeeInfo(BigInteger id, FullName fullName, Position position, LocalDate hired, BigDecimal salary, BigInteger managerId) {
            super(id, fullName, position, hired, salary, null);
            this.managerId = managerId;
        }

        Employee ToEmployee(Map<BigInteger, EmployeeInfo> data){
            Employee manager = null;
            if (data.get(managerId) != null){
                manager = data.get(managerId).ToEmployee(data);
            }
            return new Employee(getId(),getFullName(),getPosition(),getHired(),getSalary(), manager);
        }
    }

    @Override
    public Set<Employee> mapSet(ResultSet resultSet) {
        try {
            Set<EmployeeInfo> data = new HashSet<>();
            Map<BigInteger, EmployeeInfo> mapData = new HashMap<>();
            while (resultSet.next()) {
                EmployeeInfo employee = GenerateEmployee(resultSet);

                data.add(employee);
                mapData.put(employee.getId(), employee);
            }
            Set<Employee> set = new HashSet<>();
            for (EmployeeInfo entry : data){
                set.add(entry.ToEmployee(mapData));
            }
            return set;
        }
        catch (SQLException e) {
            return null;
        }
    }
}
