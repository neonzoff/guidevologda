package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.neonzoff.guidevologda.domain.BaseEntity;
import ru.neonzoff.guidevologda.domain.Contact;
import ru.neonzoff.guidevologda.domain.ContactType;
import ru.neonzoff.guidevologda.dto.ContactDto;

import java.util.List;

/**
 * @author Tseplyaev Dmitry
 */
public interface ContactService {
    List<Contact> findAll();

    List<Contact> findAll(String sortColumn);

    Contact findById(Long id);

    List<Contact> findByValue(String value);

    List<Contact> findByBaseEntity(BaseEntity baseEntity);

    List<Contact> findByType(ContactType contactType);

    Long saveContact(ContactDto contactDto);

    boolean updateContact(ContactDto contactDto);

    boolean deleteContact(Contact contact);

    boolean deleteContacts(List<Contact> contacts);

    Page<ContactDto> findPaginated(Pageable pageable);
}
