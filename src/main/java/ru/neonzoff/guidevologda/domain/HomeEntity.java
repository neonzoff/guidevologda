package ru.neonzoff.guidevologda.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "home_entity")
public class HomeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 512)
    @Column(name = "name", nullable = false, length = 512)
    private String name;

    @NotNull
    @Size(min = 2, max = 512)
    @Column(name = "name_en", nullable = false, length = 512)
    private String nameEn;

    @NotNull
    @Size(min = 10, max = 8192)
    @Column(name = "description", nullable = false, length = 8192)
    private String description;

    @NotNull
    @Size(min = 10, max = 8192)
    @Column(name = "description_en", nullable = false, length = 8192)
    private String descriptionEn;

    @OneToMany
    private List<Image> images = new ArrayList<>();
}




