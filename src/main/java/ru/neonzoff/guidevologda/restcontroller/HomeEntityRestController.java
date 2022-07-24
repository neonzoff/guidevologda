package ru.neonzoff.guidevologda.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neonzoff.guidevologda.dto.HomeEntityDto;
import ru.neonzoff.guidevologda.service.HomeEntityService;

import static ru.neonzoff.guidevologda.utils.Converter.convertHomeEntityToDto;

@RestController
@RequestMapping("homeentity")
public class HomeEntityRestController {

    private final HomeEntityService homeEntityService;

    public HomeEntityRestController(HomeEntityService homeEntityService) {
        this.homeEntityService = homeEntityService;
    }

    @GetMapping
    public HomeEntityDto get() {
        return convertHomeEntityToDto(homeEntityService.get());
    }
}
