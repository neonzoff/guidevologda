package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.neonzoff.guidevologda.dao.ImageRepository;
import ru.neonzoff.guidevologda.domain.Image;
import ru.neonzoff.guidevologda.dto.ImageDto;

import java.util.ArrayList;
import java.util.List;

import static ru.neonzoff.guidevologda.utils.Converter.convertImagesToListDto;

/**
 * @author Tseplyaev Dmitry
 */
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository repository;

    private final StorageService storageService;


    public ImageServiceImpl(ImageRepository repository, StorageService storageService) {
        this.repository = repository;
        this.storageService = storageService;
    }

    @Override
    public List<ImageDto> findAll() {
        return convertImagesToListDto(repository.findAll());
    }

    @Override
    public List<ImageDto> findAll(String sortColumn) {
        return null;
    }

    @Override
    public ImageDto findByName(String name) {
        return null;
    }

    @Override
    @Transactional
    public Image saveImage(MultipartFile file) {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        saveImg(file);
        if (!repository.findByName(image.getName()).isPresent()) {
            repository.save(image);
            return image;
        }
        return repository.findByName(image.getName()).get();
    }

    @Override
    @Transactional
    public List<Image> saveImages(List<MultipartFile> files) {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            images.add(saveImage(file));
        }
        return images;
    }

    @Override
    @Transactional
    public boolean saveImg(MultipartFile file) {
        storageService.uploadFile(file);
        return true;
    }

    @Override
    public boolean saveImgs(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            saveImage(file);
        }
        return true;
    }

    @Override
    public boolean deleteImage(Image image) {
        if (repository.findByName(image.getName()).isPresent()) {
            repository.delete(image);
            storageService.deleteFile(image.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteImages(List<Image> images) {
        for (Image image : images) {
            deleteImage(image);
        }
        return true;
    }

    @Override
    public Page<ImageDto> findPaginated(Pageable pageable) {
        return null;
    }
}
