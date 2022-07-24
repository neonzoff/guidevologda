package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neonzoff.guidevologda.dao.StreetRepository;
import ru.neonzoff.guidevologda.domain.Street;
import ru.neonzoff.guidevologda.dto.StreetDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.neonzoff.guidevologda.utils.Converter.convertStreetToDto;
import static ru.neonzoff.guidevologda.utils.Converter.convertStreetsToListDto;

/**
 * @author Tseplyaev Dmitry
 */
@Service
public class StreetServiceImpl implements StreetService {
    private final StreetRepository streetRepository;


    public StreetServiceImpl(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    @Override
    public List<StreetDto> findAll() {
        return convertStreetsToListDto(streetRepository.findAll());
    }

    @Override
    public List<StreetDto> findAll(String sortColumn) {
        return convertStreetsToListDto(streetRepository.findAll(Sort.by(sortColumn).ascending()));
    }

    @Override
    public StreetDto findByName(String name) {
//                обработать если не найден
        return convertStreetToDto(streetRepository.findByName(name).get());
    }

    @Transactional
    @Override
    public boolean saveStreet(StreetDto streetDto) {
        Street newStreet = new Street();
        newStreet.setName(streetDto.getName());
        newStreet.setNameEn(streetDto.getNameEn());
        if (streetRepository.findByName(streetDto.getName()).isPresent()) {
            return false;
        }
        streetRepository.save(newStreet);
        return true;
    }

    @Transactional
    @Override
    public boolean updateStreet(StreetDto streetDto) {
        Street oldStreet = streetRepository.getById(streetDto.getId());
        if (oldStreet.getName().equals(streetDto.getName())) {
            return false;
        }
//        todo: а сохранился ли он измененный? Проверить.
        oldStreet.setName(streetDto.getName());
        oldStreet.setNameEn(streetDto.getNameEn());
        return true;
    }

    @Transactional
    @Override
    public boolean deleteStreet(StreetDto streetDto) {
        Optional<Street> oldStreet = streetRepository.findById(streetDto.getId());
        if (oldStreet.isPresent() && oldStreet.get().getEntities().isEmpty()) {
            streetRepository.delete(oldStreet.get());
            return true;
        }
        return false;
    }

    @Override
    public Street findById(Long id) {
//        ifPresent
        return streetRepository.findById(id).get();
    }

    @Override
    public Page<StreetDto> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Street> streets = streetRepository.findAll(pageable.getSort());
        List<StreetDto> streetsDto;

        if (streets.size() < startItem) {
            streetsDto = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, streets.size());
            streetsDto = convertStreetsToListDto(streets.subList(startItem, toIndex));
        }

        return new PageImpl<>(streetsDto, PageRequest.of(currentPage, pageSize), streets.size());
    }
}
