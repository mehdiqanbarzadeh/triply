package com.triply.greendrive.model.company.dao;

import java.util.Optional;

import com.triply.greendrive.model.company.Company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	boolean existsByName(String name);

	Optional<Company> findByCode(String code);

}
