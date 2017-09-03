package com.martynatyran.domain.cat.repo;

import com.martynatyran.domain.cat.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICatRepository extends JpaRepository<Cat, Long> {
    /**
     * Finds a cat touple by providing it's last name
     * @param lastName last name
     * @return a Cat entity
     */
    Cat findByLastName(String lastName);
}