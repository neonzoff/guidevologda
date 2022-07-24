package ru.neonzoff.guidevologda.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neonzoff.guidevologda.dto.ContactDto;
import ru.neonzoff.guidevologda.service.ContactService;

import java.util.List;

import static ru.neonzoff.guidevologda.utils.Converter.convertContactsToListDto;

/**
 * @author Tseplyaev Dmitry
 */
@RestController
@RequestMapping("contacts")
public class ContactRestController {

    private final ContactService contactService;

    public ContactRestController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<ContactDto> getContacts() {
        return convertContactsToListDto(contactService.findAll());
    }
}
