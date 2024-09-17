package backend.fatec.dtos;
import jakarta.validation.constraints.NotBlank;

public record UserRecordDto(
    String username,
    @NotBlank String password,
    @NotBlank String email
){}