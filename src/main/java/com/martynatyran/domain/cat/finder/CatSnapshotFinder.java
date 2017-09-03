package com.martynatyran.domain.cat.finder;

import com.martynatyran.domain.cat.dto.CatSnapshot;
import com.martynatyran.domain.cat.entity.Cat;
import com.martynatyran.domain.cat.repo.ICatRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A class that looks for given cat touples in the database
 * and returns CatSnapshot to avoid unwanted modification in the
 * database.
 */
@Component
@AllArgsConstructor
public class CatSnapshotFinder implements ICatSnapshotFinder {
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final Sort DEFAULT_SORT = new Sort(LASTNAME, FIRSTNAME);
    private final ICatRepository catRepository;

    private List<CatSnapshot> convert(List<Cat> cats) {
        return cats.stream()
                .map(Cat::toSnapshot)
                .collect(Collectors.toList());
    }

    /**
     * Looks for a Cat touple by the given id.
     * @param id Cat's id
     * @return an optional of cat's snapshot in case when
     * the cat is not found
     */
    @Override
    public Optional<CatSnapshot> findById(Long id) {
        Cat cat = catRepository.findOne(id); //findOne [jezeli nie ma] - zwroci nulla; getOne [jezeli nie ma] - rzuci wyjatek
        return Optional.of(cat.toSnapshot());
    }

    /**
     * Looks for all cats in the database and returns a list
     * of cat's snapshots.
     * @return a list of cat snapshots
     */
    @Override
    public List<CatSnapshot> findAll() {
        List<Cat> cats = catRepository.findAll(DEFAULT_SORT);

        return convert(cats);
    }
}
