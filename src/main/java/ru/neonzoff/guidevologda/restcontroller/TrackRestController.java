package ru.neonzoff.guidevologda.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neonzoff.guidevologda.dto.TrackDto;
import ru.neonzoff.guidevologda.service.TrackService;

import java.util.List;

import static ru.neonzoff.guidevologda.utils.Converter.convertTrackToDto;

@RestController
@RequestMapping("tracks")
public class TrackRestController {

    private final TrackService trackService;

    public TrackRestController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping
    public List<TrackDto> getAll() {
        return trackService.findAll();
    }

    @GetMapping("{id}")
    public TrackDto getById(@PathVariable Long id) {
        return convertTrackToDto(trackService.findById(id));
    }
}
