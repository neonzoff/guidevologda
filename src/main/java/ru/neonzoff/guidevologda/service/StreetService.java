package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.neonzoff.guidevologda.domain.Street;
import ru.neonzoff.guidevologda.dto.StreetDto;

import java.util.List;

/**
 * @author Tseplyaev Dmitry
 */
public interface StreetService {
    List<StreetDto> findAll();

    List<StreetDto> findAll(String sortColumn);

    StreetDto findByName(String name);

    Street findById(Long id);

    boolean saveStreet(StreetDto streetDto);

    boolean updateStreet(StreetDto streetDto);

    boolean deleteStreet(StreetDto streetDto);

    Page<StreetDto> findPaginated(Pageable pageable);
}
