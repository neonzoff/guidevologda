package ru.neonzoff.guidevologda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Tseplyaev Dmitry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Long id;

    @NotEmpty
    @NotNull
    @Size(min = 2, max = 255)
    private String name;
}
