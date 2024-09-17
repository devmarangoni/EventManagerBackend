package backend.fatec.dtos;

import backend.fatec.models.User;
import jakarta.validation.constraints.NotBlank;

public record LoginResponseRecordDto(
    @NotBlank String email,
    User user,
    String token,
    String message
){}