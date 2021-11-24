package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select e from Employee e " +
            "where lower(e.name) like lower(concat('%', :searchName, '%')) " +
            "and lower(e.surname) like lower(concat('%', :searchSurname, '%')) " +
            "and lower(e.position) like lower(concat('%', :searchPosition, '%'))")
    List<Employee> search(@Param("searchName") String searchName, @Param("searchSurname") String searchSurname, @Param("searchPosition") String searchPosition);
}
