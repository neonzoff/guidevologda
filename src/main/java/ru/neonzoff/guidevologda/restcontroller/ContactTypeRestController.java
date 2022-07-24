package ru.neonzoff.guidevologda.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neonzoff.guidevologda.dto.ContactTypeDto;
import ru.neonzoff.guidevologda.service.ContactTypeService;

import java.util.List;

import static ru.neonzoff.guidevologda.utils.Converter.convertContactTypesToListDto;

/**
 * @author Tseplyaev Dmitry
 */
@RestController
@RequestMapping("contacttypes")
public class ContactTypeRestController {

    private final ContactTypeService contactTypeService;

    public ContactTypeRestController(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    @GetMapping
    public List<ContactTypeDto> getContactTypes() {
        return convertContactTypesToListDto(contactTypeService.findAll());
    }
}
