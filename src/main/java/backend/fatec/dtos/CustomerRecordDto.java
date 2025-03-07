package backend.fatec.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRecordDto(
    @NotBlank String name,
    @NotBlank String mobile,
    String phone,
    @NotBlank String email
) {}