package com.triply.greendrive.model.company;

import com.triply.greendrive.model.base.JpaBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "companies",
		indexes = { @Index(name = "COMPANY_CODE", columnList = "code") })
@EqualsAndHashCode(callSuper = true)
public class Company extends JpaBaseEntity {

	@NotNull
	@Column(name = "code", unique = true)
	private String code;

	@NotNull
	@Column(name = "name")
	private String name;

}
