package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.neonzoff.guidevologda.domain.Track;
import ru.neonzoff.guidevologda.dto.TrackDto;
import ru.neonzoff.guidevologda.dto.TrackModel;

import java.util.List;

public interface TrackService {
    List<TrackDto> findAll();

    List<TrackDto> findAll(String sortColumn);

    Track findByName(String name);

    Track findById(Long id);

    boolean saveTrack(TrackModel trackModel);

    boolean updateTrack(TrackModel trackModel);

    boolean deleteTrack(Track track);

    Page<TrackDto> findPaginated(Pageable pageable);
}
