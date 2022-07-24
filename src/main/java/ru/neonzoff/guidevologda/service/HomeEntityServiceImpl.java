package ru.neonzoff.guidevologda.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.neonzoff.guidevologda.dao.HomeEntityRepository;
import ru.neonzoff.guidevologda.domain.HomeEntity;
import ru.neonzoff.guidevologda.domain.Image;
import ru.neonzoff.guidevologda.dto.HomeEntityDto;
import ru.neonzoff.guidevologda.dto.HomeEntityModel;

import java.io.IOException;
import java.util.List;

import static ru.neonzoff.guidevologda.utils.Converter.convertFileToMultipartFile;
import static ru.neonzoff.guidevologda.utils.Converter.getFileFromURL;

@Service
public class HomeEntityServiceImpl implements HomeEntityService {

    private final HomeEntityRepository homeEntityRepository;

    private final ImageService imageService;

    public HomeEntityServiceImpl(HomeEntityRepository homeEntityRepository, ImageService imageService) {
        this.homeEntityRepository = homeEntityRepository;
        this.imageService = imageService;
    }


    @Override
    public HomeEntity get() {
        return homeEntityRepository.findAll().get(0);
    }

    @Override
    public boolean save(HomeEntityModel homeEntityModel) {
        if (homeEntityRepository.findAll().size() != 0)
            return false;
        HomeEntity homeEntity = new HomeEntity();
        homeEntity.setName(homeEntityModel.getName());
        homeEntity.setNameEn(homeEntityModel.getNameEn());
        homeEntity.setDescription(homeEntityModel.getDescription());
        homeEntity.setDescriptionEn(homeEntityModel.getDescriptionEn());
        homeEntity.setImages(imageService.saveImages(homeEntityModel.getImages()));
        homeEntityRepository.save(homeEntity);
        return true;
    }

    @Override
    public boolean updateWithoutImages(HomeEntityDto homeEntityDto) {
        HomeEntity oldEntity = homeEntityRepository.findAll().get(0);
        if (!homeEntityDto.getName().equals(oldEntity.getName()))
            oldEntity.setName(homeEntityDto.getName());
        if (!homeEntityDto.getNameEn().equals(oldEntity.getNameEn()))
            oldEntity.setNameEn(homeEntityDto.getNameEn());
        if (!homeEntityDto.getDescription().equals(oldEntity.getDescription()))
            oldEntity.setDescription(homeEntityDto.getDescription());
        if (!homeEntityDto.getDescriptionEn().equals(oldEntity.getDescriptionEn()))
            oldEntity.setDescriptionEn(homeEntityDto.getDescriptionEn());
        homeEntityRepository.save(oldEntity);
        return true;
    }

    @Override
    public boolean update(HomeEntityModel homeEntityModel) {
        HomeEntity oldEntity = homeEntityRepository.findAll().get(0);
        List<Image> oldImages = oldEntity.getImages();
        oldEntity.setImages(imageService.saveImages(homeEntityModel.getImages()));
        imageService.deleteImages(oldImages);
        if (!homeEntityModel.getName().equals(oldEntity.getName()))
            oldEntity.setName(homeEntityModel.getName());
        if (!homeEntityModel.getNameEn().equals(oldEntity.getNameEn()))
            oldEntity.setNameEn(homeEntityModel.getNameEn());
        if (!homeEntityModel.getDescription().equals(oldEntity.getDescription()))
            oldEntity.setDescription(homeEntityModel.getDescription());
        if (!homeEntityModel.getDescriptionEn().equals(oldEntity.getDescriptionEn()))
            oldEntity.setDescriptionEn(homeEntityModel.getDescriptionEn());
        homeEntityRepository.save(oldEntity);
        return true;
    }

    @Override
    public boolean uploadImages(List<MultipartFile> images) {
        HomeEntity oldEntity = homeEntityRepository.findAll().get(0);
        List<Image> oldImages = oldEntity.getImages();
        oldEntity.setImages(imageService.saveImages(images));
        imageService.deleteImages(oldImages);
        homeEntityRepository.save(oldEntity);
        return true;
    }

    @Override
    public boolean delete(HomeEntityDto homeEntityDto) {
        return false;
    }

    @Override
    public void createHomeEntity() throws IOException {
        if (homeEntityRepository.findAll().size() == 0) {
            HomeEntity homeEntity = new HomeEntity();
            homeEntity.setName("Вологда");
            homeEntity.setNameEn("Vologda");
            homeEntity.setDescription("Описание города Вологда для мобильного гида-справочника");
            homeEntity.setDescriptionEn("Description of Vologda for a mobile guide-directory");
            homeEntity.setImages(imageService.saveImages(
                    List.of(
                            convertFileToMultipartFile(
                                    getFileFromURL(
                                            "https://storage.yandexcloud.net/guidevologda/vologda1.jpg",
                                            "",
                                            "vologda1",
                                            ".jpg"
                                    )),
                            convertFileToMultipartFile(
                                    getFileFromURL(
                                            "https://storage.yandexcloud.net/guidevologda/vologda2.jpg",
                                            "",
                                            "vologda2",
                                            ".jpg"
                                    ))
                    )
            ));
            homeEntityRepository.save(homeEntity);
        }
    }
}
