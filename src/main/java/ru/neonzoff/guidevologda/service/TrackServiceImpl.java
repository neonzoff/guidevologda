package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neonzoff.guidevologda.dao.TrackRepository;
import ru.neonzoff.guidevologda.domain.BaseEntity;
import ru.neonzoff.guidevologda.domain.Track;
import ru.neonzoff.guidevologda.dto.TrackDto;
import ru.neonzoff.guidevologda.dto.TrackModel;

import java.util.Collections;
import java.util.List;

import static java.lang.Long.parseLong;
import static ru.neonzoff.guidevologda.utils.Converter.convertTracksToListDto;

@Service
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;

    private final ImageService imageService;

    private final BaseEntityService baseEntityService;


    public TrackServiceImpl(TrackRepository trackRepository, ImageService imageService,
                            BaseEntityService baseEntityService) {
        this.trackRepository = trackRepository;
        this.imageService = imageService;
        this.baseEntityService = baseEntityService;
    }

    @Override
    public List<TrackDto> findAll() {
        return convertTracksToListDto(trackRepository.findAll());
    }

    @Override
    public List<TrackDto> findAll(String sortColumn) {
        return convertTracksToListDto(trackRepository.findAll(Sort.by(sortColumn)));
    }

    @Override
    public Track findByName(String name) {
        return trackRepository.findByName(name);
    }

    @Override
    public Track findById(Long id) {
        return trackRepository.findById(id).get();
    }

    @Override
    public boolean saveTrack(TrackModel trackModel) {
        if (trackRepository.findByName(trackModel.getName()) == null) {
            Track track = new Track();
            track.setName(trackModel.getName());
            track.setNameEn(trackModel.getNameEn());
            track.setDescription(trackModel.getDescription());
            track.setDescriptionEn(trackModel.getDescriptionEn());
            track.setImage(imageService.saveImage(trackModel.getImage()));
            if (trackModel.getEntities().length() > 1) {
                for (String entityId : trackModel.getEntities().split(","))
                    track.addEntity(baseEntityService.findById(parseLong(entityId)));
            } else if (trackModel.getEntities().length() == 1) {
                track.addEntity(baseEntityService.findById(parseLong(trackModel.getEntities())));
            }
            trackRepository.save(track);
        }
        return false;
    }

    @Override
    public boolean updateTrack(TrackModel trackModel) {
        return false;
    }

    @Transactional
    @Override
    public boolean deleteTrack(Track track) {
        List<BaseEntity> entities = track.getEntities();
        for (int i = 0; i < track.getEntities().size(); i++) {
            entities.get(i).removeTrack(track);
        }
        trackRepository.delete(track);
        imageService.deleteImage(track.getImage());

        return true;
    }

    @Override
    public Page<TrackDto> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Track> tracks = trackRepository.findAll(pageable.getSort());
        List<TrackDto> tracksDto;

        if (tracks.size() < startItem) {
            tracksDto = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tracks.size());
            tracksDto = convertTracksToListDto(tracks.subList(startItem, toIndex));
        }

        return new PageImpl<>(tracksDto, PageRequest.of(currentPage, pageSize), tracks.size());
    }
}
