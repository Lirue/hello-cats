package com.martynatyran.domain.cat.bo;

import com.martynatyran.Application;
import com.martynatyran.domain.cat.controller.CatNew;
import com.martynatyran.domain.cat.dto.CatSnapshot;
import com.martynatyran.domain.cat.entity.GenderType;
import com.martynatyran.domain.cat.finder.ICatSnapshotFinder;
import com.martynatyran.domain.cat.repo.ICatRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CatBOTest {
    //add, edit, delete
    @Autowired
    private ICatBO catBO;

    @Autowired
    private ICatSnapshotFinder catSnapshotFinder;

    @Autowired
    private ICatRepository catRepository;

    private static final String CLAZZ = CatBOTest.class.getSimpleName();
    private static final int AGE = 12;
    private static final GenderType GENDER = GenderType.FEMALE;
    private CatSnapshot catSnapshot;
    private CatNew catNew;

    @Before
    public void setUp(){
        catNew = new CatNew(CLAZZ, CLAZZ, AGE, GENDER);
        catSnapshot = catRepository
                .findOne(catBO.add(catNew))
                .toSnapshot();
    }

    @After
    public void tearDown(){
        catRepository.deleteAll();
    }

    @Test
    public void shouldAddCat(){
        //given

        //when
        catSnapshot = catRepository
                .findOne(catBO.add(catNew))
                .toSnapshot();

        //then
        Assert.assertNotNull(catSnapshot);
        Assert.assertThat(catSnapshot.getFirstName(), is(equalTo(CLAZZ)));
        Assert.assertThat(catSnapshot.getLastName(), is(equalTo(CLAZZ)));
        Assert.assertThat(catSnapshot.getAge(), is(equalTo(AGE)));
        Assert.assertThat(catSnapshot.getGender(), is(equalTo(GENDER)));
    }

    @Test
    public void shouldEditCat(){
        //given
        String firstName = "firstNameTest";
        String lastName = "lastNameTest";
        int age = 10;
        GenderType genderType = GenderType.MALE;
        catNew = new CatNew(firstName, lastName, age, genderType);

        //when
        catBO.edit(catSnapshot.getId(), catNew);

        //then
        CatSnapshot newCatSnapshot = catRepository.findOne(catSnapshot.getId()).toSnapshot();
        Assert.assertNotNull(newCatSnapshot);
        Assert.assertThat(newCatSnapshot.getFirstName(), is(equalTo(firstName)));
        Assert.assertThat(newCatSnapshot.getLastName(), is(equalTo(lastName)));
        Assert.assertThat(newCatSnapshot.getAge(), is(equalTo(age)));
        Assert.assertThat(newCatSnapshot.getGender(), is(equalTo(genderType)));
    }

    @Test
    public void shouldDeleteCat(){
        //given

        //when
        catBO.delete(catSnapshot.getId());

        //then
        Assert.assertNull(catRepository.findOne(catSnapshot.getId()));
    }

    @Test
    public void shouldDeleteCatEvenIfIdDoesNotExist(){
        //given
        long id = 1234;
        //when
        catBO.delete(id);

        //then
        Assert.assertNull(catRepository.findOne(id));
    }

    @Test
    public void shouldFindAllCats(){
        //given
        int numberOfCats = 3;
        int numberOfAllCreatedCats = numberOfCats + 1;
        for (int i = 0; i < numberOfCats; i++){
            catBO.add(catNew);
        }

        //when
        List<CatSnapshot> cats = catBO.findAll();

        //then
        Assert.assertThat(cats.size(), is(equalTo(numberOfAllCreatedCats)));
    }

    @Test
    public void shouldRetrunEmptyListIfThereAreNoCats(){
        //given
        catRepository.deleteAll();

        //when
        List<CatSnapshot> cats = catBO.findAll();

        //then
        Assert.assertThat(cats.size(), is(equalTo(0)));
    }

}
