package ru.neonzoff.guidevologda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neonzoff.guidevologda.domain.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    Track findByName(String name);
}
