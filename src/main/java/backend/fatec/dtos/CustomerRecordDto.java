package backend.fatec.dtos;

import backend.fatec.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRecordDto(
    @NotBlank String name,
    @NotBlank String mobile,
    String phone,
    @NotNull User user
) {}