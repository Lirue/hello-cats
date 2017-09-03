package com.martynatyran.domain.cat.controller;

import com.martynatyran.domain.cat.config.Constants;
import com.martynatyran.domain.cat.entity.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Representation of a cat that is mapped from incoming JSON
 * and will be used as an input to perform actions like add, edit etc.
 */
@Getter
@Setter
@AllArgsConstructor
public class CatNew {

    @NotNull
    @Size(min= Constants.MIN_NAME_LENGTH,
            max=Constants.MAX_NAME_LENGTH)
    private String firstName;

    @NotNull
    @Size(min= Constants.MIN_NAME_LENGTH,
            max=Constants.MAX_NAME_LENGTH)
    private String lastName;

    @Min(Constants.MIN_AGE)
    @Max(Constants.MAX_AGE)
    private Integer age;

    @NotNull
    private GenderType gender;
}
