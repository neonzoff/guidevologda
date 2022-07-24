package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.neonzoff.guidevologda.domain.BaseEntity;
import ru.neonzoff.guidevologda.domain.Tag;
import ru.neonzoff.guidevologda.domain.Track;
import ru.neonzoff.guidevologda.domain.TypeEntity;
import ru.neonzoff.guidevologda.dto.BaseEntityDto;
import ru.neonzoff.guidevologda.dto.BaseEntityModel;

import java.util.List;

/**
 * @author Tseplyaev Dmitry
 */
public interface BaseEntityService {
    List<BaseEntityDto> findAll();

    List<BaseEntityDto> findAll(String sortColumn);

    List<BaseEntityDto> findByActive(Boolean active);

    List<BaseEntityDto> findByTag(Tag tag);

    List<BaseEntityDto> findByTypeEntity(TypeEntity typeEntity);

    BaseEntityDto findByName(String name);

    BaseEntity findById(Long id);

    boolean addImagesToEntity(Long id, List<MultipartFile> images);

    boolean saveBaseEntity(BaseEntityDto baseEntityDto);

    boolean updateBaseEntity(BaseEntityDto baseEntityDto);

    boolean deleteBaseEntity(BaseEntityDto baseEntityDto);

    boolean removeTrack(BaseEntityDto baseEntityDto, Track track);

    Page<BaseEntityModel> findPaginated(Pageable pageable);
}
