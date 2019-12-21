package com.efimchick.ifmo.web.jdbc.service;

import com.efimchick.ifmo.web.jdbc.dao.DaoFactory;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;

import java.util.List;

public class ServiceFactory {

    public EmployeeService employeeService(){
        return new EmployeeService() {
            @Override
            public List<Employee> getAllSortByHireDate(Paging paging) {
                return new DaoFactory().employeeDAO().getAll(paging.page-1,paging.itemPerPage,"HIREDATE");
            }

            @Override
            public List<Employee> getAllSortByLastname(Paging paging) {
                return new DaoFactory().employeeDAO().getAll(paging.page-1,paging.itemPerPage,"LASTNAME");
            }

            @Override
            public List<Employee> getAllSortBySalary(Paging paging) {
                return new DaoFactory().employeeDAO().getAll(paging.page-1,paging.itemPerPage,"SALARY");
            }

            @Override
            public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
                return new DaoFactory().employeeDAO().getAll(paging.page-1,paging.itemPerPage,"DEPARTMENT","LASTNAME");
            }

            @Override
            public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
                return new DaoFactory().employeeDAO().getByDepartmentId(department.getId().toString(),paging.page-1,paging.itemPerPage,"HIREDATE");
            }

            @Override
            public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
                return new DaoFactory().employeeDAO().getByDepartmentId(department.getId().toString(),paging.page-1,paging.itemPerPage,"SALARY");
            }

            @Override
            public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
                return new DaoFactory().employeeDAO().getByDepartmentId(department.getId().toString(),paging.page-1,paging.itemPerPage,"LASTNAME");
            }

            @Override
            public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
                return new DaoFactory().employeeDAO().getByManagerId(manager.getId().toString(),paging.page-1,paging.itemPerPage,"LASTNAME");
            }

            @Override
            public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
                return new DaoFactory().employeeDAO().getByManagerId(manager.getId().toString(),paging.page-1,paging.itemPerPage,"HIREDATE");
            }

            @Override
            public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
                return new DaoFactory().employeeDAO().getByManagerId(manager.getId().toString(),paging.page-1,paging.itemPerPage,"SALARY");
            }

            @Override
            public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
                return new DaoFactory().employeeDAO().getById(employee.getId().toString(), true);
            }

            @Override
            public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
                List<Employee> temp = new DaoFactory().employeeDAO().getByDepartmentId(department.getId().toString(),salaryRank-1,1,"SALARY DESC");
                if (temp.size() > 0){
                    return temp.get(0);
                }
                return null;
            }
        };
    }
}
