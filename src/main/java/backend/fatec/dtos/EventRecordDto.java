package backend.fatec.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import backend.fatec.models.Customer;
import backend.fatec.models.Schedule;

public record EventRecordDto(
    UUID eventId,
    @NotBlank String length,
    @NotBlank String address,
    @NotNull Customer customer,
    Schedule schedule,
    String theme,
    String description,
    String birthdayPerson,
    @NotNull float value,
    @NotNull Boolean isBudget,
    Boolean finished
){}