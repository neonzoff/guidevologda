package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neonzoff.guidevologda.dao.BaseEntityRepository;
import ru.neonzoff.guidevologda.dao.ContactRepository;
import ru.neonzoff.guidevologda.domain.BaseEntity;
import ru.neonzoff.guidevologda.domain.Contact;
import ru.neonzoff.guidevologda.domain.ContactType;
import ru.neonzoff.guidevologda.dto.ContactDto;

import java.util.Collections;
import java.util.List;

import static ru.neonzoff.guidevologda.utils.Converter.convertContactsToListDto;

/**
 * @author Tseplyaev Dmitry
 */
@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    private final BaseEntityRepository baseEntityRepository;

    private final ContactTypeService contactTypeService;

    public ContactServiceImpl(ContactRepository contactRepository, BaseEntityRepository baseEntityRepository,
                              ContactTypeService contactTypeService) {
        this.contactRepository = contactRepository;
        this.baseEntityRepository = baseEntityRepository;
        this.contactTypeService = contactTypeService;
    }

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public List<Contact> findAll(String sortColumn) {
        return contactRepository.findAll(Sort.by(sortColumn).ascending());
    }

    @Override
    public Contact findById(Long id) {
//        ifPresent
        return contactRepository.findById(id).get();
    }

    @Override
    public List<Contact> findByValue(String value) {
//        ifPresent
        return contactRepository.findByValue(value);
    }

    @Override
    public List<Contact> findByBaseEntity(BaseEntity baseEntity) {
//        ifPresent
        return baseEntityRepository.findByName(baseEntity.getName()).get().getContacts();
    }

    @Override
    public List<Contact> findByType(ContactType contactType) {
        return contactTypeService.findByName(contactType.getName()).getContacts();
    }

    @Transactional
    @Override
    public Long saveContact(ContactDto contactDto) {
        Contact contact = new Contact();
        contact.setValue(contactDto.getValue().trim());
        contactRepository.save(contact);
        return contact.getId();
    }

    @Transactional
    @Override
    public boolean updateContact(ContactDto contactDto) {
        return false;
    }

    @Transactional
    @Override
    public boolean deleteContacts(List<Contact> contacts) {
        contacts.forEach(this::deleteContact);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteContact(Contact contact) {
        contactRepository.delete(contact);
        return true;
    }

    @Override
    public Page<ContactDto> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Contact> contacts = contactRepository.findAll(pageable.getSort());
        List<ContactDto> contactsDto;

        if (contacts.size() < startItem) {
            contactsDto = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, contacts.size());
            contactsDto = convertContactsToListDto(contacts.subList(startItem, toIndex));
        }

        return new PageImpl<>(contactsDto, PageRequest.of(currentPage, pageSize), contacts.size());
    }
}
