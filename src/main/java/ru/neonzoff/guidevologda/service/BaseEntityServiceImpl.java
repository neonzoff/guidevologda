package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.neonzoff.guidevologda.dao.BaseEntityRepository;
import ru.neonzoff.guidevologda.domain.BaseEntity;
import ru.neonzoff.guidevologda.domain.Image;
import ru.neonzoff.guidevologda.domain.Tag;
import ru.neonzoff.guidevologda.domain.Track;
import ru.neonzoff.guidevologda.domain.TypeEntity;
import ru.neonzoff.guidevologda.dto.BaseEntityDto;
import ru.neonzoff.guidevologda.dto.BaseEntityModel;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.neonzoff.guidevologda.utils.Converter.convertBaseEntitiesToList;
import static ru.neonzoff.guidevologda.utils.Converter.convertBaseEntitiesToListDto;
import static ru.neonzoff.guidevologda.utils.Converter.convertBaseEntityToDto;
import static ru.neonzoff.guidevologda.utils.Converter.convertContactsDtoToList;
import static ru.neonzoff.guidevologda.utils.Converter.convertPropertiesDtoToList;

/**
 * @author Tseplyaev Dmitry
 */
@Service
public class BaseEntityServiceImpl implements BaseEntityService {
    private final BaseEntityRepository repository;

    private final TypeEntityService typeEntityService;

    private final StreetService streetService;

    private final ImageService imageService;

    private final TagService tagService;

    private final ContactService contactService;

    private final GeoCoderService geoCoderService;

    public BaseEntityServiceImpl(BaseEntityRepository repository, TypeEntityService typeEntityService,
                                 StreetService streetService, ImageService imageService,
                                 TagService tagService, ContactService contactService,
                                 GeoCoderService geoCoderService) {
        this.repository = repository;
        this.typeEntityService = typeEntityService;
        this.streetService = streetService;
        this.imageService = imageService;
        this.tagService = tagService;
        this.contactService = contactService;
        this.geoCoderService = geoCoderService;
    }

    @Override
    public List<BaseEntityDto> findAll() {
        return convertBaseEntitiesToListDto(repository.findAll());
    }

    @Override
    public List<BaseEntityDto> findAll(String sortColumn) {
        return convertBaseEntitiesToListDto(repository.findAll(Sort.by(sortColumn).ascending()));
    }

    @Override
    public List<BaseEntityDto> findByActive(Boolean active) {
        return convertBaseEntitiesToListDto(repository.findByActive(active));
    }

    @Override
    public List<BaseEntityDto> findByTag(Tag tag) {
        return convertBaseEntitiesToListDto(repository.findByTagsContains(tag));
    }

    @Override
    public List<BaseEntityDto> findByTypeEntity(TypeEntity typeEntity) {
        return convertBaseEntitiesToListDto(repository.findByTypeEntity(typeEntity));
    }

    @Override
    public BaseEntityDto findByName(String name) {
//        обработать если не найден
        return convertBaseEntityToDto(repository.findByName(name).get());
    }

    @Override
    public BaseEntity findById(Long id) {
//        ifPresent
        return repository.findById(id).get();
    }

    @Override
    @Transactional
    public boolean addImagesToEntity(Long id, List<MultipartFile> images) {
        BaseEntity baseEntity = findById(id);
        List<Image> oldImages = baseEntity.getImages();
        baseEntity.setImages(imageService.saveImages(images));
        imageService.deleteImages(oldImages);
        repository.save(baseEntity);
        return true;
    }

    @Override
    public boolean saveBaseEntity(BaseEntityDto baseEntityDto) {
        BaseEntity baseEntity = new BaseEntity();
        if (baseEntityDto.getId() != null) {
            baseEntity = repository.getById(baseEntityDto.getId());
            if (baseEntityDto.getTypeEntity() != null)
                baseEntity.setTypeEntity(typeEntityService.findById(baseEntityDto.getTypeEntity()));
            baseEntity.setName(baseEntityDto.getName());
            baseEntity.setNameEn(baseEntityDto.getNameEn());
            baseEntity.setDescription(baseEntityDto.getDescription());
            baseEntity.setDescriptionEn(baseEntityDto.getDescriptionEn());
            if (baseEntityDto.getStreet() != null && !baseEntityDto.getStreet().toString().isEmpty()) {
                baseEntity.setStreet(streetService.findById(baseEntityDto.getStreet()));
            }
        } else {
            if (baseEntityDto.getTypeEntity() != null) {
                baseEntity.setTypeEntity(typeEntityService.findById(baseEntityDto.getTypeEntity()));
            }
            baseEntity.setName(baseEntityDto.getName());
            baseEntity.setNameEn(baseEntityDto.getNameEn());
            baseEntity.setDescription(baseEntityDto.getDescription());
            baseEntity.setDescriptionEn(baseEntityDto.getDescriptionEn());
            if (baseEntityDto.getStreet() != null) {
                baseEntity.setStreet(streetService.findById(baseEntityDto.getStreet()));
            }
        }
        baseEntity.setHouseNumber(baseEntityDto.getHouseNumber());
        if (baseEntity.getStreet() != null) {
            baseEntity.setPos(geoCoderService.getPos(streetService.findById(baseEntity.getStreet().getId()), baseEntityDto.getHouseNumber()));
        }
        if (baseEntityDto.getContacts() != null) {
            baseEntity.setContacts(convertContactsDtoToList(baseEntityDto.getContacts()));
        }
        if (baseEntityDto.getProperties() != null) {
            baseEntity.setProperties(convertPropertiesDtoToList(baseEntityDto.getProperties()));
        }
        baseEntity.setWorkSchedule(baseEntityDto.getWorkSchedule());
        baseEntity.setWorkScheduleEn(baseEntityDto.getWorkScheduleEn());
        if (baseEntityDto.getTags() != null && !baseEntityDto.getTags().isEmpty()) {
            for (Tag tag : tagService.findById(baseEntityDto.getTags()))
                baseEntity.setTag(tag);
        }
        baseEntity.setActive(baseEntityDto.getActive());
        repository.save(baseEntity);

        return true;
    }

    @Override
    public boolean updateBaseEntity(BaseEntityDto baseEntityDto) {
        return false;
    }

    @Override
    public boolean removeTrack(BaseEntityDto baseEntityDto, Track track) {
        Optional<BaseEntity> baseEntity = repository.findById(baseEntityDto.getId());
        if (baseEntity.isPresent() && baseEntity.get().getTracks().contains(track)) {
            baseEntity.get().removeTrack(track);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteBaseEntity(BaseEntityDto baseEntityDto) {
        Optional<BaseEntity> baseEntity = repository.findById(baseEntityDto.getId());
        if (baseEntity.isPresent() && baseEntity.get().getTracks().isEmpty()) {
            contactService.deleteContacts(baseEntity.get().getContacts());
            repository.delete(baseEntity.get());
            imageService.deleteImages(baseEntity.get().getImages());
            return true;
        }
        return false;
    }

    @Override
    public Page<BaseEntityModel> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<BaseEntity> entities = repository.findAll(pageable.getSort());
        List<BaseEntityModel> entityModels;

        if (entities.size() < startItem) {
            entityModels = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, entities.size());
            entityModels = convertBaseEntitiesToList(entities.subList(startItem, toIndex));
        }

        return new PageImpl<>(entityModels, PageRequest.of(currentPage, pageSize), entities.size());
    }
}
