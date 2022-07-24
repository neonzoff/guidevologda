package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neonzoff.guidevologda.dao.TypeEntityRepository;
import ru.neonzoff.guidevologda.domain.TypeEntity;
import ru.neonzoff.guidevologda.dto.TypeEntityDto;
import ru.neonzoff.guidevologda.dto.TypeEntityModel;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.neonzoff.guidevologda.utils.Converter.convertFileToMultipartFile;
import static ru.neonzoff.guidevologda.utils.Converter.convertTypeEntitiesToListDto;
import static ru.neonzoff.guidevologda.utils.Converter.convertTypeEntityToDto;
import static ru.neonzoff.guidevologda.utils.Converter.getFileFromURL;

/**
 * @author Tseplyaev Dmitry
 */
@Service
public class TypeEntityServiceImpl implements TypeEntityService {
    private final TypeEntityRepository repository;

    private final ImageService imageService;


    public TypeEntityServiceImpl(TypeEntityRepository repository, ImageService imageService) {
        this.repository = repository;
        this.imageService = imageService;
    }

    @Override
    public List<TypeEntityDto> findAll() {
        return convertTypeEntitiesToListDto(repository.findAll());
    }

    @Override
    public List<TypeEntityDto> findAll(String sortColumn) {
        return convertTypeEntitiesToListDto(repository.findAll(Sort.by(sortColumn).ascending()));
    }

    @Override
    public TypeEntityDto findByName(String name) {
//        обработать если не найден
        return convertTypeEntityToDto(repository.findByName(name).get());
    }

    @Override
    public TypeEntity findById(Long id) {
//        ifPresent
        return repository.findById(id).get();
    }


    @Override
    @Transactional
    public boolean saveTypeEntity(TypeEntityModel typeEntityModel) {
        TypeEntity typeEntity = new TypeEntity();
        typeEntity.setName(typeEntityModel.getName());
        typeEntity.setNameEn(typeEntityModel.getNameEn());
        typeEntity.setDescription(typeEntityModel.getDescription());
        typeEntity.setDescriptionEn(typeEntityModel.getDescriptionEn());
        typeEntity.setImage(imageService.saveImage(typeEntityModel.getImage()));
        if (repository.findByName(typeEntityModel.getName()).isPresent())
            return false;
        repository.save(typeEntity);

        return true;
    }

    @Override
    @Transactional
    public boolean updateTypeEntity(TypeEntityDto typeEntityDto) {
        TypeEntity typeEntity = repository.getById(typeEntityDto.getId());
        if (typeEntity.getName().equals(typeEntityDto.getName()) && typeEntity.getNameEn().equals(typeEntityDto.getNameEn()))
            return false;
//                todo: а сохранился ли он измененный? Проверить.
//        для обоих меняем
        typeEntity.setName(typeEntityDto.getName());
        typeEntity.setNameEn(typeEntityDto.getNameEn());
        return true;
    }

    @Override
    @Transactional
    public boolean deleteTypeEntity(TypeEntityDto typeEntityDto) {
        Optional<TypeEntity> typeEntity = repository.findById(typeEntityDto.getId());
        if (typeEntity.isPresent() && typeEntity.get().getEntities().isEmpty()) {
            repository.delete(typeEntity.get());
            return true;
        }
        return false;
    }

    @Override
    public Page<TypeEntityDto> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<TypeEntity> typeEntities = repository.findAll(pageable.getSort());
        List<TypeEntityDto> typeEntitiesDto;

        if (typeEntities.size() < startItem) {
            typeEntitiesDto = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, typeEntities.size());
            typeEntitiesDto = convertTypeEntitiesToListDto(typeEntities.subList(startItem, toIndex));
        }

        return new PageImpl<>(typeEntitiesDto, PageRequest.of(currentPage, pageSize), typeEntities.size());
    }

    @Transactional
    public void createDefaultTypes() throws IOException {
        if (repository.findAll().isEmpty()) {
            TypeEntity typeCafe = new TypeEntity();
            typeCafe.setName("Кафе");
            typeCafe.setNameEn("Cafe");
            typeCafe.setDescription("Тестовое описание типа кафе");
            typeCafe.setDescriptionEn("Тестовое описание типа кафе EN");
            typeCafe.setImage(imageService.saveImage(convertFileToMultipartFile(
                    getFileFromURL(
                            "https://storage.yandexcloud.net/guidevologda/hotel_icon.png",
                            "",
                            "hotel",
                            ".png"
                    ))
            ));
            repository.save(typeCafe);

            TypeEntity typeHotel = new TypeEntity();
            typeHotel.setName("Отель");
            typeHotel.setNameEn("Hotel");
            typeHotel.setDescription("Тестовое описание типа отель");
            typeHotel.setDescriptionEn("Тестовое описание типа отель EN");
            typeHotel.setImage(imageService.saveImage(convertFileToMultipartFile(
                    getFileFromURL(
                            "https://storage.yandexcloud.net/guidevologda/cafe_icon.png",
                            "",
                            "cafe",
                            ".png"
                    ))
            ));
            repository.save(typeHotel);
        }
    }
}
