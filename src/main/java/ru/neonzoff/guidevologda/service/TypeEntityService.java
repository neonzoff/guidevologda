package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.neonzoff.guidevologda.domain.TypeEntity;
import ru.neonzoff.guidevologda.dto.TypeEntityDto;
import ru.neonzoff.guidevologda.dto.TypeEntityModel;

import java.io.IOException;
import java.util.List;

/**
 * @author Tseplyaev Dmitry
 */
public interface TypeEntityService {
    List<TypeEntityDto> findAll();

    List<TypeEntityDto> findAll(String sortColumn);

    TypeEntityDto findByName(String name);

    TypeEntity findById(Long id);

    boolean saveTypeEntity(TypeEntityModel typeEntityModel);

    boolean updateTypeEntity(TypeEntityDto typeEntityDto);

    boolean deleteTypeEntity(TypeEntityDto typeEntityDto);

    Page<TypeEntityDto> findPaginated(Pageable pageable);

    public void createDefaultTypes() throws IOException;
}
