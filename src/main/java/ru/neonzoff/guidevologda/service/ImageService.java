package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.neonzoff.guidevologda.domain.Image;
import ru.neonzoff.guidevologda.dto.ImageDto;

import java.util.List;

/**
 * @author Tseplyaev Dmitry
 */
public interface ImageService {
    List<ImageDto> findAll();

    List<ImageDto> findAll(String sortColumn);

    ImageDto findByName(String name);

    boolean saveImg(MultipartFile file);

    Image saveImage(MultipartFile file);

    boolean saveImgs(List<MultipartFile> files);

    List<Image> saveImages(List<MultipartFile> files);

    boolean deleteImage(Image image);

    boolean deleteImages(List<Image> images);

    Page<ImageDto> findPaginated(Pageable pageable);
}
