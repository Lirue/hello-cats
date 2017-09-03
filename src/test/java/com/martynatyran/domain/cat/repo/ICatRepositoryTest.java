package com.martynatyran.domain.cat.repo;

import com.martynatyran.domain.cat.dto.CatSnapshot;
import com.martynatyran.domain.cat.entity.Cat;
import com.martynatyran.domain.cat.entity.CatTest;
import com.martynatyran.domain.cat.entity.GenderType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ICatRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ICatRepository catRepository;

    private static final String CLAZZ = ICatRepositoryTest.class.getSimpleName();
    private static final int AGE = 12;
    private static final GenderType GENDER = GenderType.FEMALE;

    @After
    public void tearDown(){
        catRepository.deleteAll();
    }

    @Test
    public void testExample() throws Exception {
        //given
        Cat cat = new Cat(CLAZZ, CLAZZ, AGE, GENDER);

        //when
        entityManager.persist(cat);
        CatSnapshot catSnapshotBefore = cat.toSnapshot();
        cat = this.catRepository.findByLastName(CLAZZ);
        CatSnapshot catSnapshotAfter = cat.toSnapshot();

        //then
        Assert.assertThat(catSnapshotAfter.getLastName(),is(equalTo(CLAZZ)));
        Assert.assertThat(catSnapshotAfter.getId(), is(equalTo(catSnapshotBefore.getId())));
    }
}
