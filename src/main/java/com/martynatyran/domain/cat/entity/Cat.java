package com.martynatyran.domain.cat.entity;

import com.martynatyran.domain.cat.config.Constants;
import com.martynatyran.domain.cat.dto.CatSnapshot;
import com.martynatyran.domain.cat.exception.EntityInStateNewException;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents Cat database tuple in form of
 * an object.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Setter
public class Cat implements Serializable {
    private static final long serialVersionUID = 7972265007575707207L;

    /**
     * Autogenereted id for a Cat Entity.
     * Id value is a key value that identifies Cat-related objects.
     */
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Size(min=Constants.MIN_NAME_LENGTH,
            max=Constants.MAX_NAME_LENGTH)
    private String firstName;

    @NotNull
    @Size(min=Constants.MIN_NAME_LENGTH,
            max=Constants.MAX_NAME_LENGTH)
    private String lastName;

    @Min(Constants.MIN_AGE)
    @Max(Constants.MAX_AGE)
    private int age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    /**
     * When the entity was created in a database.
     * Set automatically by Hibernate.
     */
    @CreatedDate
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    /**
     * When the entity was updated in a database.
     * Set automatically by Hibernate.
     */
    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    /**
     * Constructor for JPA/Hibernate purposes
     */
    protected Cat() {
    }

    /**
     * Constructor of Cat Entity.
     * @param firstName Cat's first name. Cannot be null
     * @param lastName Cat's last name. Cannot be null
     * @param age Cat's age. Restricted to range 0-100
     * @param gender Cat's gender. Possible options: MALE, FEMALE
     */
    public Cat(String firstName, String lastName, int age, GenderType gender){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
    }

    /**
     * Creates new instance of Data Transfer Object from the Cat Entity.
     * @return Snapshot of a Cat Entity
     */
    public CatSnapshot toSnapshot(){
        if(id == null){
            throw new EntityInStateNewException();
        }
        return new CatSnapshot(id, firstName, lastName, age, gender, createdAt, updatedAt);
    }
}
