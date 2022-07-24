package ru.neonzoff.guidevologda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neonzoff.guidevologda.domain.Contact;

import java.util.List;

/**
 * @author Tseplyaev Dmitry
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByValue(String value);
}
