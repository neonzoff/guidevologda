package ru.neonzoff.guidevologda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neonzoff.guidevologda.domain.Image;

import java.util.Optional;


/**
 * @author Tseplyaev Dmitry
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByName(String name);
}
