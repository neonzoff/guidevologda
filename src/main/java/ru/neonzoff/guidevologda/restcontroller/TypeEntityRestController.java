package ru.neonzoff.guidevologda.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neonzoff.guidevologda.dto.TypeEntityDto;
import ru.neonzoff.guidevologda.service.TypeEntityService;

import java.util.List;

import static ru.neonzoff.guidevologda.utils.Converter.convertTypeEntityToDto;

/**
 * @author Tseplyaev Dmitry
 */
@RestController
@RequestMapping("typeentity")
public class TypeEntityRestController {

    private final TypeEntityService typeEntityService;

    public TypeEntityRestController(TypeEntityService typeEntityService) {
        this.typeEntityService = typeEntityService;
    }

    @GetMapping
    public List<TypeEntityDto> getTypesEntity() {
        return typeEntityService.findAll();
    }

    @GetMapping("{id}")
    public TypeEntityDto getById(@PathVariable Long id) {
        return convertTypeEntityToDto(typeEntityService.findById(id));
    }
}
