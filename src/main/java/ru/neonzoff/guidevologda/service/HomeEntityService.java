package ru.neonzoff.guidevologda.service;

import org.springframework.web.multipart.MultipartFile;
import ru.neonzoff.guidevologda.domain.HomeEntity;
import ru.neonzoff.guidevologda.dto.HomeEntityDto;
import ru.neonzoff.guidevologda.dto.HomeEntityModel;

import java.io.IOException;
import java.util.List;

public interface HomeEntityService {
    HomeEntity get();

    boolean save(HomeEntityModel homeEntityModel);

    boolean update(HomeEntityModel homeEntityModel);

    boolean updateWithoutImages(HomeEntityDto homeEntityDto);

    boolean delete(HomeEntityDto homeEntityDto);

    boolean uploadImages(List<MultipartFile> images);

    void createHomeEntity() throws IOException;
}
