package com.martynatyran.domain.cat.bo;

import com.martynatyran.domain.cat.controller.CatNew;
import com.martynatyran.domain.cat.dto.CatSnapshot;
import com.martynatyran.domain.cat.entity.GenderType;

import java.util.List;

public interface ICatBO {
    Long add(CatNew catNew);

    void edit(Long id, CatNew catNew);

    void delete(Long id);

    List<CatSnapshot> findAll();
}
