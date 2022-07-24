package ru.neonzoff.guidevologda.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neonzoff.guidevologda.dao.TagRepository;
import ru.neonzoff.guidevologda.domain.Tag;
import ru.neonzoff.guidevologda.dto.TagDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.neonzoff.guidevologda.utils.Converter.convertTagsToListDto;

/**
 * @author Tseplyaev Dmitry
 */
@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;


    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDto> findAll() {
        return convertTagsToListDto(tagRepository.findAll());
    }

    @Override
    public List<TagDto> findAll(String sortColumn) {
        return convertTagsToListDto(tagRepository.findAll(Sort.by(sortColumn).ascending()));
    }

    @Override
    public Tag findByName(String name) {
//        обработать если не найден
        return tagRepository.findByName(name).get();
    }

    @Override
    public List<Tag> findById(List<Long> ids) {
        List<Tag> tags = new ArrayList<>();
        for (Long id : ids) {
//            ifPresent
            tags.add(tagRepository.getById(id));
        }

        return tags;
    }

    @Override
    public Tag findById(Long id) {
//        ifPresent
        return tagRepository.findById(id).get();
    }

    @Override
    public boolean saveTag(TagDto tagDto) {
        Tag newTag = new Tag();
        newTag.setName(tagDto.getName());
        newTag.setNameEn(tagDto.getNameEn());
        if (tagRepository.findByName(tagDto.getName()).isPresent()) {
            return false;
        }
        tagRepository.save(newTag);
        return true;
    }

    @Override
    public boolean updateTag(TagDto tagDto) {
        Tag oldTag = tagRepository.getById(tagDto.getId());
        if (oldTag.getName().equals(tagDto.getName()) && oldTag.getNameEn().equals(tagDto.getNameEn()))
            return false;
//        todo: а сохранился ли он измененный? Проверить.
//        для обоих меняем
        oldTag.setName(tagDto.getName());
        oldTag.setNameEn(tagDto.getNameEn());
        return true;
    }

    @Override
    public boolean deleteTag(TagDto tagDto) {
        Optional<Tag> oldTag = tagRepository.findById(tagDto.getId());
        if (oldTag.isPresent() && oldTag.get().getEntities().isEmpty()) {
            tagRepository.delete(oldTag.get());
            return true;
        }
        return false;
    }

    @Override
    public Page<TagDto> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Tag> tags = tagRepository.findAll(pageable.getSort());
        List<TagDto> tagsDto;

        if (tags.size() < startItem) {
            tagsDto = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tags.size());
            tagsDto = convertTagsToListDto(tags.subList(startItem, toIndex));
        }

        return new PageImpl<>(tagsDto, PageRequest.of(currentPage, pageSize), tags.size());
    }

    @Override
    @Transactional
    public void createDefaultTypes() {
        if (tagRepository.findAll().isEmpty()) {
            Tag tagEda = new Tag();
            tagEda.setName("Еда");
            tagEda.setNameEn("Food");
            tagRepository.save(tagEda);

            Tag tagSleep = new Tag();
            tagSleep.setName("Сон");
            tagSleep.setNameEn("Sleep");
            tagRepository.save(tagSleep);
        }
    }
}
