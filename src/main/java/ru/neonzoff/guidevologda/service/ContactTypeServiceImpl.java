package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neonzoff.guidevologda.dao.ContactTypeRepository;
import ru.neonzoff.guidevologda.domain.Contact;
import ru.neonzoff.guidevologda.domain.ContactType;
import ru.neonzoff.guidevologda.dto.ContactTypeDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.neonzoff.guidevologda.utils.Converter.convertContactTypesToListDto;
import static ru.neonzoff.guidevologda.utils.Converter.convertContactsDtoToList;

/**
 * @author Tseplyaev Dmitry
 */
@Service
public class ContactTypeServiceImpl implements ContactTypeService {
    private final ContactTypeRepository repository;


    public ContactTypeServiceImpl(ContactTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ContactType> findAll() {
        return repository.findAll();
    }

    @Override
    public ContactType findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public List<ContactType> findAll(String sortColumn) {
        return repository.findAll(Sort.by(sortColumn).ascending());
    }

    @Override
    public ContactType findByName(String name) {
//        обработать если не найден
        return repository.findByName(name).get();
    }

    @Override
    public ContactType findByContacts(Contact contact) {
        return repository.findByContacts(contact);
    }

    @Override
    @Transactional
    public boolean addContact(String name, Contact contact) {
//        ifPresent
        repository.findByName(name).get().getContacts().add(contact);
        return true;
    }

    @Override
    public boolean removeContact(ContactType contactType, Contact contact) {
        repository.findById(contactType.getId()).get().getContacts().remove(contact);
        return true;
    }

    @Override
    public List<Contact> getAllContacts(String name) {
        return findByName(name).getContacts();
    }

    @Override
    @Transactional
    public boolean saveContactType(ContactTypeDto contactTypeDto) {
        ContactType contactType = new ContactType();
        contactType.setName(contactTypeDto.getName());
        contactType.setNameEn(contactTypeDto.getNameEn());
//        nullable
        if (contactTypeDto.getContacts() != null)
            contactType.setContacts(convertContactsDtoToList(contactTypeDto.getContacts()));
        if (repository.findByName(contactTypeDto.getName()).isPresent())
            return false;
        repository.save(contactType);
        return true;
    }

    @Override
    @Transactional
    public boolean updateContactType(ContactTypeDto contactTypeDto) {
        ContactType contactType = repository.getById(contactTypeDto.getId());
        if (contactType.getName().equals(contactTypeDto.getName()) && contactType.getNameEn().equals(contactTypeDto.getNameEn()))
            return false;
//                todo: а сохранился ли он измененный? Проверить.
//        для обоих меняем
        contactType.setName(contactTypeDto.getName());
        contactType.setNameEn(contactTypeDto.getNameEn());
        return true;
    }

    @Override
    @Transactional
    public boolean deleteContactType(ContactTypeDto contactTypeDto) {
        Optional<ContactType> contactType = repository.findById(contactTypeDto.getId());
        if (contactType.isPresent() && contactType.get().getContacts().isEmpty()) {
            repository.delete(contactType.get());
            return true;
        }
        return false;
    }

    @Override
    public Page<ContactTypeDto> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ContactType> contactTypes = repository.findAll(pageable.getSort());
        List<ContactTypeDto> contactTypesDto;

        if (contactTypes.size() < startItem) {
            contactTypesDto = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, contactTypes.size());
            contactTypesDto = convertContactTypesToListDto(contactTypes.subList(startItem, toIndex));
        }

        return new PageImpl<>(contactTypesDto, PageRequest.of(currentPage, pageSize), contactTypes.size());
    }

    @Override
    @Transactional
    public void createDefaultTypes() {
        if (repository.findAll().isEmpty()) {
            ContactType typePhone = new ContactType();
            typePhone.setName("Номер телефона");
            typePhone.setNameEn("Phone number");
            repository.save(typePhone);

            ContactType typeEmail = new ContactType();
            typeEmail.setName("Email");
            typeEmail.setNameEn("Email");
            repository.save(typeEmail);

            ContactType typeVk = new ContactType();
            typeVk.setName("ВК");
            typeVk.setNameEn("VK");
            repository.save(typeVk);

        }
    }
}
