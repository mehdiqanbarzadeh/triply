package com.triply.greendrive.model.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass
public class JpaBaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate = new Date().toInstant();

    @LastModifiedDate
    @Column(name = "modification_date")
    private Instant modificationDate;


}
