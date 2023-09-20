package com.triply.greendrive.model.vehicle;

import com.triply.greendrive.model.base.JpaBaseEntity;
import com.triply.greendrive.model.company.Company;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vehicles",
		indexes = { @Index(name = "VEHICLE_ID_CODE", columnList = "id_code") })
@EqualsAndHashCode(callSuper = true)
public class Vehicle extends JpaBaseEntity {

	@NotNull
	@Column(name = "id_code")
	private String IDCode;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private VehicleType type;

	@NotNull
	@Column(name = "mileage")
	private Long mileage;

	@NotNull
	@Column(name = "employee_id")
	private String employeeId;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
}
