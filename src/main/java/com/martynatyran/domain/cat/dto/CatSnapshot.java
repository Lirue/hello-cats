package com.martynatyran.domain.cat.dto;

import com.martynatyran.domain.cat.entity.GenderType;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * A class that is a Data Transfer Object
 * for Cat Entity. Provides all only getters of
 * Cat Entity values so that the entity could
 * not be modified.
 */
@Getter
public class CatSnapshot {

    private Long id;

    private String firstName;

    private String lastName;

    private int age;

    private GenderType gender;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * A constructor to create a CatSnapshot from values passed
     * in the parameters
     * @param id Cat's id
     * @param firstName Cat's first name
     * @param lastName Cat's last name
     * @param age Cat's age
     * @param gender Cat's gender
     * @param createdAt when the Cat was created in the database
     * @param updatedAt when the Cat was updated in the database
     */
    public CatSnapshot(Long id, String firstName, String lastName, int age, GenderType gender, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
