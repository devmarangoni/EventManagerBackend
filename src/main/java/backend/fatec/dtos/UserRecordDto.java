package backend.fatec.dtos;
import jakarta.validation.constraints.NotBlank;

public record UserRecordDto(
    @NotBlank String username,
    @NotBlank String password,
    @NotBlank String email,
    String photo
){}