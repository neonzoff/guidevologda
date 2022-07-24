package ru.neonzoff.guidevologda.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neonzoff.guidevologda.dto.ImageDto;
import ru.neonzoff.guidevologda.service.ImageService;

import java.util.List;

/**
 * @author Tseplyaev Dmitry
 */
@RestController
@RequestMapping("images")
public class ImageRestController {

    private final ImageService imageService;

    public ImageRestController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public List<ImageDto> getImages() {
        return imageService.findAll();
    }

}
