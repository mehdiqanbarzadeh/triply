package com.triply.greendrive.model.vehicle.dao;

import com.triply.greendrive.model.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByIDCode(String IdCode);

    List<Vehicle> findByCompanyCodeAndCreationDateAfter(String companyCode, Instant instant);

	List<Vehicle> findAllByCompanyCode(String code);


    List<Vehicle> findByEmployeeIdAndCreationDateAfter(String employeeId, Instant instant);

    List<Vehicle> findByCreationDateAfter(Instant instant);

}
