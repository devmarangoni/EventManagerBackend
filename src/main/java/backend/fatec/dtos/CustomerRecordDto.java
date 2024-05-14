package backend.fatec.dtos;

import jakarta.validation.constraints.NotBlank;

public record CustomerRecordDto(
    @NotBlank String name,
    @NotBlank String mobile,
    String phone
) {}