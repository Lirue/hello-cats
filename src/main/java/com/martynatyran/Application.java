package com.martynatyran;

import com.martynatyran.domain.cat.entity.Cat;
import com.martynatyran.domain.cat.entity.GenderType;
import com.martynatyran.domain.cat.repo.ICatRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * This is the main class of a helloCats project.
 */
@SpringBootApplication
@EnableJpaRepositories("com.martynatyran.domain.cat.repo")
@EntityScan("com.martynatyran.domain.cat.entity")
@EnableJpaAuditing
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Prepares data and saves in the database for demo purposes
     * @param repository cat's repository
     * @return
     */
    @Bean
    public ApplicationRunner demo(ICatRepository repository) {
        return (args) -> {
            // save a couple of cats
            repository.save(new Cat("Kitty", "Poppins", 2, GenderType.FEMALE));
            repository.save(new Cat("Duchess", "Flufferton",10, GenderType.FEMALE));
            repository.save(new Cat("The Great", "Catsby",7, GenderType.MALE));
        };
    }
}
