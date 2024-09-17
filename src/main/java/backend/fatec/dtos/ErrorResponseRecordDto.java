package backend.fatec.dtos;

import jakarta.validation.constraints.NotBlank;

public record ErrorResponseRecordDto(
    @NotBlank String message
){}