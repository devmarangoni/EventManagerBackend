package backend.fatec.dtos;

import jakarta.validation.constraints.NotBlank;

public record SuccessResponseRecordDto(
    @NotBlank String message
){}