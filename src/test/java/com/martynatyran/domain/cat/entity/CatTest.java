package com.martynatyran.domain.cat.entity;

import com.martynatyran.Application;
import com.martynatyran.domain.cat.config.Constants;
import com.martynatyran.domain.cat.dto.CatSnapshot;
import com.martynatyran.domain.cat.exception.EntityInStateNewException;
import com.martynatyran.domain.cat.repo.ICatRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CatTest {
    @Autowired
    private ICatRepository catRepository;

    private static final String CLAZZ = CatTest.class.getSimpleName();
    private static final int AGE = 12;
    private static final GenderType GENDER = GenderType.FEMALE;
    private static Validator validator;
    private Cat cat;

    @Before
    public void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        cat = new Cat(CLAZZ, CLAZZ, AGE, GENDER);
        cat = catRepository.save(cat);
    }

    @After
    public void tearDown(){
        catRepository.deleteAll();
    }

    @Test(expected = EntityInStateNewException.class)
    public void shouldThrowEntityInStateNewExceptionInToSnapshotWhenEntityIsNotPersisted() {
        // given
        Cat cat = new Cat("Jon", "Snow", 24, GenderType.MALE);

        // when
        cat.toSnapshot();

        // then
        // new EntityInStateNewException is thrown
    }

    @Test
    public void shouldEditFirstName(){
        //given
        String test = "test";
        CatSnapshot catSnapshot;

        //when
        cat.setFirstName(test);
        catSnapshot = cat.toSnapshot();
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertThat(catSnapshot.getFirstName(), is(equalTo(test)));
        Assert.assertEquals( 0, constraintViolations.size() );
    }

    @Test
    public void shouldNotEditFirstNameWhenFirstNameIsNull(){
        //given
        String newFirstName = null;

        //when
        cat.setFirstName(newFirstName);
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "may not be null",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void shouldNotEditFirstNameWhenFirstNameIsTooShort(){
        //given
        String test = StringUtils.repeat('a', Constants.MIN_NAME_LENGTH-1);

        //when
        cat.setFirstName(test);
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "size must be between " + Constants.MIN_NAME_LENGTH + " and " + Constants.MAX_NAME_LENGTH,
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void shouldNotEditFirstNameWhenFirstNameIsTooLong(){
        //given
        String newFirstName = StringUtils.repeat('a', Constants.MAX_NAME_LENGTH+1);

        //when
        cat.setFirstName(newFirstName);
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "size must be between " + Constants.MIN_NAME_LENGTH + " and " + Constants.MAX_NAME_LENGTH,
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void shouldEditLastName(){
        //given
        String test = "test";
        CatSnapshot catSnapshot;

        //when
        cat.setLastName(test);
        catSnapshot = cat.toSnapshot();
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertThat(catSnapshot.getLastName(), is(equalTo(test)));
        Assert.assertEquals( 0, constraintViolations.size() );
    }


    @Test
    public void shouldNotEditLastNameWhenLastNameIsNull(){
        //given
        String newLastName = null;

        //when
        cat.setLastName(newLastName);
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "may not be null",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void shouldNotEditLastNameWhenLastNameIsTooShort(){
        //given
        String newLastName = StringUtils.repeat('a', Constants.MIN_NAME_LENGTH-1);

        //when
        cat.setLastName(newLastName);
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "size must be between " + Constants.MIN_NAME_LENGTH + " and " + Constants.MAX_NAME_LENGTH,
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void shouldNotEditLastNameWhenLastNameIsTooLong(){
        //given
        String newLastName = StringUtils.repeat('a', Constants.MAX_NAME_LENGTH+1);

        //when
        cat.setLastName(newLastName);
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "size must be between " + Constants.MIN_NAME_LENGTH + " and " + Constants.MAX_NAME_LENGTH,
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void shouldEditAge(){
        //given
        int test = 10;
        CatSnapshot catSnapshot;

        //when
        cat.setAge(test);
        catSnapshot = cat.toSnapshot();
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertThat(catSnapshot.getAge(), is(equalTo(test)));
        Assert.assertEquals( 0, constraintViolations.size() );
    }

    @Test
    public void shouldNotEditAgeWhenAgeIsTooLow(){
        //given
        int newAge = Constants.MIN_AGE-1;

        //when
        cat.setAge(newAge);
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "must be greater than or equal to " + Constants.MIN_AGE,
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void shouldNotEditAgeWhenAgeIsTooHigh(){
        //given
        int newAge = Constants.MAX_AGE+1;

        //when
        cat.setAge(newAge);
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "must be less than or equal to " + Constants.MAX_AGE,
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void shouldEditGender(){
        //given
        GenderType genderType = GenderType.MALE;
        CatSnapshot catSnapshot;

        //when
        cat.setGender(genderType);
        catSnapshot = cat.toSnapshot();
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertThat(catSnapshot.getGender(), is(equalTo(genderType)));
        Assert.assertEquals( 0, constraintViolations.size() );
    }

    @Test
    public void shouldNotEditGenderWhenGenderIsNull(){
        //given
        GenderType genderType = null;

        //when
        cat.setGender(genderType);
        Set<ConstraintViolation<Cat>> constraintViolations =
                validator.validate( cat );

        //then
        Assert.assertEquals( 1, constraintViolations.size() );
        Assert.assertEquals(
                "may not be null",
                constraintViolations.iterator().next().getMessage());
    }


    @Test
    public void shouldReturnSnapshotWhenToSnapshotInvokedAndEntityIsPersisted(){
        //given
        // an entity created in setup()

        //when
        CatSnapshot catSnapshot = cat.toSnapshot();

        //then
        Assert.assertNotNull(catSnapshot);
        Assert.assertNotNull(catSnapshot.getCreatedAt());
        Assert.assertNotNull(catSnapshot.getUpdatedAt());
        Assert.assertThat(catSnapshot.getFirstName(), is(equalTo(CLAZZ)));
        Assert.assertThat(catSnapshot.getLastName(), is(equalTo(CLAZZ)));
        Assert.assertThat(catSnapshot.getAge(), is(equalTo(AGE)));
        Assert.assertThat(catSnapshot.getGender(), is(equalTo(GENDER)));
    }
}
