package ru.neonzoff.guidevologda.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neonzoff.guidevologda.dto.BaseEntityDto;
import ru.neonzoff.guidevologda.service.BaseEntityService;
import ru.neonzoff.guidevologda.service.TagService;
import ru.neonzoff.guidevologda.service.TypeEntityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.neonzoff.guidevologda.utils.Converter.convertBaseEntityToDto;

/**
 * @author Tseplyaev Dmitry
 */
@RestController
@RequestMapping("entities")
public class EntityRestController {

    private final BaseEntityService baseEntityService;

    private final TypeEntityService typeEntityService;

    private final TagService tagService;


    public EntityRestController(BaseEntityService baseEntityService, TagService tagService,
                                TypeEntityService typeEntityService
    ) {
        this.baseEntityService = baseEntityService;
        this.tagService = tagService;
        this.typeEntityService = typeEntityService;
    }

    @GetMapping
    public List<BaseEntityDto> getEntities(@RequestParam(required = false) List<String> tags, @RequestParam(required = false) Long typeEntityId) {
        Set<BaseEntityDto> set = baseEntityService.findAll().stream()
                .sequential()
                .filter(BaseEntityDto::getActive)
                .collect(Collectors.toSet());
        if (tags != null) {
            tags.forEach(tag -> {
                var foundedTag = tagService.findByName(tag);
                set.retainAll(baseEntityService.findByTag(foundedTag));
            });
        }
        if (typeEntityId != null) {
            var typeEntity = typeEntityService.findById(typeEntityId);
            set.retainAll(baseEntityService.findByTypeEntity(typeEntity));
        }
        if (tags == null && typeEntityId == null) {
            return baseEntityService.findAll();
        }
        return new ArrayList<>(set);
    }

    @GetMapping("{id}")
    public BaseEntityDto getById(@PathVariable Long id) {
        return convertBaseEntityToDto(baseEntityService.findById(id));
    }

}
