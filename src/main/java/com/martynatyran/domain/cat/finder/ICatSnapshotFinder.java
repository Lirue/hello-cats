package com.martynatyran.domain.cat.finder;

import com.martynatyran.domain.cat.dto.CatSnapshot;

import java.util.List;
import java.util.Optional;

public interface ICatSnapshotFinder {
    Optional<CatSnapshot> findById(Long id);

    List<CatSnapshot> findAll();
}
