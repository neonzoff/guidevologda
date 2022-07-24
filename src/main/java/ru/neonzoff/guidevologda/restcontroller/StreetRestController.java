package ru.neonzoff.guidevologda.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neonzoff.guidevologda.dto.StreetDto;
import ru.neonzoff.guidevologda.service.StreetService;

import java.util.List;

import static ru.neonzoff.guidevologda.utils.Converter.convertStreetToDto;

/**
 * @author Tseplyaev Dmitry
 */
@RestController
@RequestMapping("streets")
public class StreetRestController {

    private final StreetService streetService;

    public StreetRestController(StreetService streetService) {
        this.streetService = streetService;
    }

    @GetMapping
    public List<StreetDto> getStreets() {
        return streetService.findAll();
    }

    @GetMapping ("{id}")
    public StreetDto getStreetById(@PathVariable Long id) {
        return convertStreetToDto(streetService.findById(id));
    }

}
