package backend.fatec.dtos;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record UserRecordDto(
    UUID userId,
    @NotBlank String username,
    @NotBlank String password,
    @NotBlank String email,
    String photo
){}