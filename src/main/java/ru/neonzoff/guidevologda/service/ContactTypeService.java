package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.neonzoff.guidevologda.domain.Contact;
import ru.neonzoff.guidevologda.domain.ContactType;
import ru.neonzoff.guidevologda.dto.ContactTypeDto;

import java.util.List;

/**
 * @author Tseplyaev Dmitry
 */
public interface ContactTypeService {
    List<ContactType> findAll();

    List<ContactType> findAll(String sortColumn);

    ContactType findById(Long id);

    ContactType findByName(String name);

    ContactType findByContacts(Contact contact);

    boolean addContact(String name, Contact contact);

    boolean removeContact(ContactType contactType, Contact contact);

    List<Contact> getAllContacts(String name);

    boolean saveContactType(ContactTypeDto contactTypeDto);

    boolean updateContactType(ContactTypeDto contactTypeDto);

    boolean deleteContactType(ContactTypeDto contactTypeDto);

    Page<ContactTypeDto> findPaginated(Pageable pageable);

    public void createDefaultTypes();
}
