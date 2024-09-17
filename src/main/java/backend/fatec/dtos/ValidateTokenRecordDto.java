package backend.fatec.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ValidateTokenRecordDto(
    @NotNull @NotBlank String token
){}