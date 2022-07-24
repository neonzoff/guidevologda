package ru.neonzoff.guidevologda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neonzoff.guidevologda.domain.TypeEntity;

import java.util.Optional;

/**
 * @author Tseplyaev Dmitry
 */
public interface TypeEntityRepository extends JpaRepository<TypeEntity, Long> {

    Optional<TypeEntity> findByName(String name);
}
