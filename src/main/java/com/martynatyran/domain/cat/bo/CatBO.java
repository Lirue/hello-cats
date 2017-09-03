package com.martynatyran.domain.cat.bo;

import com.martynatyran.domain.cat.controller.CatNew;
import com.martynatyran.domain.cat.dto.CatSnapshot;
import com.martynatyran.domain.cat.entity.Cat;
import com.martynatyran.domain.cat.exception.CatNotFoundException;
import com.martynatyran.domain.cat.finder.ICatSnapshotFinder;
import com.martynatyran.domain.cat.repo.ICatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Data Access Object for Cat - facilitates accessing and
 * making operations on Cat Entity without risking unwanted
 * operations on the entity itself. Contains business logic for
 * this entity.
 */
@Service
@Transactional
@AllArgsConstructor
public class CatBO implements ICatBO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatBO.class);

    /**
     * An instance of cat repository
     */
    private final ICatRepository catRepository;

    /**
     * An instance of finder of CatSnapshots
     */
    private final ICatSnapshotFinder catSnapshotFinder;

    /**
     * Adds a new cat to the database
     * @param catNew an object representing a new cat
     * @return a snapshot of created cat
     */
    @Override
    public Long add(CatNew catNew){
        Cat cat = new Cat(catNew.getFirstName(), catNew.getLastName(), catNew.getAge(), catNew.getGender());

        catRepository.saveAndFlush(cat);

        CatSnapshot catSnapshot = cat.toSnapshot();

        LOGGER.info("Add Cat <{}> <{}> <{}> <{}> <{}>",
                catSnapshot.getId(), catSnapshot.getFirstName(), catSnapshot.getLastName(), catSnapshot.getAge(), catSnapshot.getGender()
                );

        return catSnapshot.getId();
    }

    /**
     * Edits cat in a database
     * @param id id of a cat to be edited
     * @param catNew an object representing a new cat
     */
    @Override
    public void edit(Long id, CatNew catNew){
        Cat cat = catRepository.findOne(id);

        if(cat == null){
            throw new CatNotFoundException();
        }

        cat = updateAllFields(catNew, cat);

        CatSnapshot catSnapshot = cat.toSnapshot();

        LOGGER.info("Edit Cat <{}> <{}> <{}> <{}> <{}>",
                catSnapshot.getId(), catSnapshot.getFirstName(), catSnapshot.getLastName(), catSnapshot.getAge(), catSnapshot.getGender()
        );
    }

    /**
     * Deletes cat in a database
     * @param id id of a cat to be deleted
     */
    @Override
    public void delete(Long id){
        Cat cat = catRepository.findOne(id);
        if (cat != null) {
            CatSnapshot catSnapshot = cat.toSnapshot();
            catRepository.delete(id);

            LOGGER.info("Delete Cat <{}> <{}> <{}> <{}> <{}>",
                    catSnapshot.getId(), catSnapshot.getFirstName(), catSnapshot.getLastName(), catSnapshot.getAge(), catSnapshot.getGender());
        }
    }

    /**
     * Returns a list of all cats in a database
     * @return a list of cats' snapshots
     */
    @Override
    public List<CatSnapshot> findAll() {
        return catSnapshotFinder.findAll();
    }

    private Cat updateAllFields(CatNew catNew, Cat cat){
        cat.setFirstName(catNew.getFirstName());
        cat.setLastName(catNew.getLastName());
        cat.setAge(catNew.getAge());
        cat.setGender(catNew.getGender());

        return cat;
    }
}
