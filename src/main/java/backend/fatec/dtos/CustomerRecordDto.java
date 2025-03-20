package backend.fatec.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record CustomerRecordDto(
    UUID customerId,
    @NotBlank String name,
    @NotBlank String mobile,
    String phone,
    @NotBlank String email,
    String description
) {}