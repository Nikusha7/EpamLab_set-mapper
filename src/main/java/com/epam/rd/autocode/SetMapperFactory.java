package com.epam.rd.autocode;


import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class SetMapperFactory {

    public SetMapper<Set<Employee>> employeesSetMapper() {

        return new SetMapper<Set<Employee>>() {
            @Override
            public Set<Employee> mapSet(ResultSet resultSet) {
                Set<Employee> employees = new HashSet<>();

                try {
                    resultSet.last();
                    if (resultSet.getRow() == 1) {

                        BigInteger id = BigInteger.valueOf(resultSet.getInt("ID"));

                        String firstName = resultSet.getString("FIRSTNAME");
                        String lastName = resultSet.getString("LASTNAME");
                        String middleName = resultSet.getString("MIDDLENAME");
                        FullName fullName = new FullName(firstName, lastName, middleName);

                        Position position = Position.valueOf(resultSet.getString("POSITION"));

                        LocalDate hireDate = resultSet.getDate("HIREDATE").toLocalDate();
                        BigDecimal salary = resultSet.getBigDecimal("SALARY");

                        employees.add(new Employee(id, fullName, position, hireDate, salary, null));
                    } else {

                        resultSet.beforeFirst();
                        boolean nullManager = false;
                        while (resultSet.next()) {
                            Integer managerID = (Integer) resultSet.getObject("MANAGER");
                            if (managerID == null) {
                                nullManager = true;
                            }
                        }

                        resultSet.beforeFirst();
                        while (resultSet.next()) {
                            if (nullManager) {
                                resultSet.beforeFirst();
                                try {
                                    employees = constructEmployees(resultSet);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                            } else {
                                resultSet.beforeFirst();
                                employees = notNullCaseEmployees(resultSet);
                            }
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                return employees;
            }

            private Set<Employee> constructEmployees(ResultSet resultSet) throws SQLException {
                Set<Employee> employees = new HashSet<>();
                List<Employee> employeeList = new ArrayList<>();

                while (resultSet.next()) {
                    BigInteger id = BigInteger.valueOf(resultSet.getInt("ID"));

                    String firstName = resultSet.getString("FIRSTNAME");
                    String lastName = resultSet.getString("LASTNAME");
                    String middleName = resultSet.getString("MIDDLENAME");
                    FullName fullName = new FullName(firstName, lastName, middleName);

                    Position position = Position.valueOf(resultSet.getString("POSITION"));

                    LocalDate hireDate = resultSet.getDate("HIREDATE").toLocalDate();
                    BigDecimal salary = resultSet.getBigDecimal("SALARY");

                    Integer managerID = (Integer) resultSet.getObject("MANAGER");
                    if (managerID == null) {
                        Employee employeeWithoutManager = new Employee(id, fullName, position, hireDate, salary, null);
                        employees.add(employeeWithoutManager);
                        employeeList.add(employeeWithoutManager);

                    }
                }
                resultSet.beforeFirst();
                List<Employee> doubleEmployee = new ArrayList<>();
                while (resultSet.next()) {
                    Integer managerID = (Integer) resultSet.getObject("MANAGER");
                    if (managerID != null) {
                        int comparison = employeeList.get(0).getId().compareTo(new BigInteger(managerID.toString()));
                        if (comparison == 0) {
                            BigInteger id = BigInteger.valueOf(resultSet.getInt("ID"));

                            String firstName = resultSet.getString("FIRSTNAME");
                            String lastName = resultSet.getString("LASTNAME");
                            String middleName = resultSet.getString("MIDDLENAME");
                            FullName fullName = new FullName(firstName, lastName, middleName);

                            Position position = Position.valueOf(resultSet.getString("POSITION"));

                            LocalDate hireDate = resultSet.getDate("HIREDATE").toLocalDate();
                            BigDecimal salary = resultSet.getBigDecimal("SALARY");

                            Employee employee = new Employee(id, fullName, position, hireDate, salary, employeeList.get(0));

                            employees.add(employee);
                            doubleEmployee.add(employee);

                        }
                    }
                }

                employeeList.remove(0);

                resultSet.beforeFirst();

                List<Employee> tripleEmployee = new ArrayList<>();
                while (resultSet.next()) {
                    BigInteger id = BigInteger.valueOf(resultSet.getInt("ID"));

                    String firstName = resultSet.getString("FIRSTNAME");
                    String lastName = resultSet.getString("LASTNAME");
                    String middleName = resultSet.getString("MIDDLENAME");
                    FullName fullName = new FullName(firstName, lastName, middleName);

                    Position position = Position.valueOf(resultSet.getString("POSITION"));

                    LocalDate hireDate = resultSet.getDate("HIREDATE").toLocalDate();
                    BigDecimal salary = resultSet.getBigDecimal("SALARY");

                    Integer managerID = (Integer) resultSet.getObject("MANAGER");
                    if (managerID != null) {
                        for (Employee e : doubleEmployee) {
                            int comparison = e.getId().compareTo(new BigInteger(managerID.toString()));
                            if (comparison == 0) {
                                Employee nestedEmployee = new Employee(id, fullName, position, hireDate, salary, e);

                                employees.add(nestedEmployee);
                                tripleEmployee.add(nestedEmployee);

                            }
                        }

                    }

                }

                resultSet.beforeFirst();
                while (resultSet.next()) {
                    BigInteger id = BigInteger.valueOf(resultSet.getInt("ID"));

                    String firstName = resultSet.getString("FIRSTNAME");
                    String lastName = resultSet.getString("LASTNAME");
                    String middleName = resultSet.getString("MIDDLENAME");
                    FullName fullName = new FullName(firstName, lastName, middleName);

                    Position position = Position.valueOf(resultSet.getString("POSITION"));

                    LocalDate hireDate = resultSet.getDate("HIREDATE").toLocalDate();
                    BigDecimal salary = resultSet.getBigDecimal("SALARY");

                    Integer managerID = (Integer) resultSet.getObject("MANAGER");
                    if (managerID != null) {
                        for (Employee e : tripleEmployee) {
                            int comparison = e.getId().compareTo(new BigInteger(managerID.toString()));
                            if (comparison == 0) {
                                Employee nestedEmployee = new Employee(id, fullName, position, hireDate, salary, e);

                                employees.add(nestedEmployee);
                            }
                        }
                    }
                }

                System.out.println(employees);
                return employees;
            }

            private Employee constructEmployee(ResultSet resultSet) throws SQLException {
                BigInteger id = BigInteger.valueOf(resultSet.getInt("ID"));

                String firstName = resultSet.getString("FIRSTNAME");
                String lastName = resultSet.getString("LASTNAME");
                String middleName = resultSet.getString("MIDDLENAME");
                FullName fullName = new FullName(firstName, lastName, middleName);

                Position position = Position.valueOf(resultSet.getString("POSITION"));

                LocalDate hireDate = resultSet.getDate("HIREDATE").toLocalDate();
                BigDecimal salary = resultSet.getBigDecimal("SALARY");

                Integer managerID = resultSet.getInt("MANAGER");

                Employee manager = null;
                if (managerID != null) {
                    manager = constructEmployeeWithManagerID(managerID, resultSet);
                }


                return new Employee(id, fullName, position, hireDate, salary, manager);

            }

            private Employee constructEmployeeWithManagerID(Integer managerID, ResultSet resultSet) throws SQLException {
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    if (resultSet.getInt("ID") == managerID) {
                        Employee manager = constructEmployee(resultSet);
                        Integer grandManagerID = resultSet.getInt("MANAGER");
                        if (grandManagerID != null) {
                            return new Employee(manager.getId(), manager.getFullName(), manager.getPosition(), manager.getHired(), manager.getSalary(), constructEmployeeWithManagerID(grandManagerID, resultSet));
                        } else {
                            return manager;
                        }
                    }
                }
                return null; // Manager not found
            }


            private Set<Employee> notNullCaseEmployees(ResultSet resultSet) {
                List<Employee> employeeList = new ArrayList<>();
                Set<Employee> employees = new HashSet<>();
                Map<Integer, Employee> employeeMap = new HashMap<>();
                Map<Integer, Integer> empIDManagerID = new HashMap<>();
                try {
                    while (resultSet.next()) {
                        BigInteger id = BigInteger.valueOf(resultSet.getInt("ID"));
                        Integer idInteger = resultSet.getInt("ID");

                        String firstName = resultSet.getString("FIRSTNAME");
                        String lastName = resultSet.getString("LASTNAME");
                        String middleName = resultSet.getString("MIDDLENAME");
                        FullName fullName = new FullName(firstName, lastName, middleName);

                        Position position = Position.valueOf(resultSet.getString("POSITION"));

                        LocalDate hireDate = resultSet.getDate("HIREDATE").toLocalDate();
                        BigDecimal salary = resultSet.getBigDecimal("SALARY");

                        Integer managerID = (Integer) resultSet.getObject("MANAGER");
                        System.out.println("ID: " + id + "\nfullName: " + fullName + "\nposition: " + position +
                                "\nhireDate: " + hireDate + "\nsalary: " + salary + "\nmanagerID: " + managerID);


                        empIDManagerID.put(idInteger, managerID);
                        employeeList.add(new Employee(id, fullName, position, hireDate, salary, null));
                    }

                    System.out.println("\nPrinting empIDManagerID:");
                    empIDManagerID.forEach((key, value) -> System.out.println(key + " : " + value));

                    System.out.println("\nPrinting employeeList:");
                    for (Employee e : employeeList) {
                        System.out.println(e);
                    }


                    Set<Integer> keySet = empIDManagerID.keySet();
                    Integer lastManager = null;
                    for (Map.Entry<Integer, Integer> entry : empIDManagerID.entrySet()) {
                        Integer managerID = entry.getValue();
                        if (!keySet.contains(managerID)) {
                            lastManager = entry.getKey();
                        }
                    }
                    System.out.println("\nLast Manager: " + lastManager);


                    for (Map.Entry<Integer, Integer> entry : empIDManagerID.entrySet()) {
                        if (entry.getValue().equals(lastManager)) {
                            for (Employee e : employeeList) {
                                System.out.println("\nComparing " + entry.getKey() + " and " + e.getId());
                                int comparisonResult = e.getId().compareTo(new BigInteger(entry.getKey().toString()));
                                if (comparisonResult == 0) {
                                    for (Employee employee : employeeList) {
                                        System.out.println("\nComparing " + employee.getId() + " and " + lastManager);
                                        int comparison = employee.getId().compareTo(new BigInteger(lastManager.toString()));
                                        if (comparison == 0) {
                                            Employee nestedEmp = new Employee(e.getId(), e.getFullName(), e.getPosition(),
                                                    e.getHired(), e.getSalary(), employee);
                                            employees.add(nestedEmp);
                                        }
                                    }
                                }

                            }

                        }
                    }
                    for (Employee e : employeeList) {
                        int comparison = e.getId().compareTo(new BigInteger(lastManager.toString()));
                        if (comparison == 0) {
                            employees.add(e);
                        }
                    }
                    for (Employee e : employeeList) {
                        int comparisonResult = e.getId().compareTo(new BigInteger(lastManager.toString()));
                        if (comparisonResult == 0) {

                        }
                    }


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return employees;
            }

        };

    }

}