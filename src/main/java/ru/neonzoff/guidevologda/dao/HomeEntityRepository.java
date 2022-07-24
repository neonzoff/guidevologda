package ru.neonzoff.guidevologda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neonzoff.guidevologda.domain.HomeEntity;

@Repository
public interface HomeEntityRepository extends JpaRepository<HomeEntity, Long> {
}
