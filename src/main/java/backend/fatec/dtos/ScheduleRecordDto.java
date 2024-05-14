package backend.fatec.dtos;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ScheduleRecordDto(
    @NotBlank Date eventDateTime,
    @NotNull List<UUID> events
){

    /**
     *  GET para obter os Ids dos eventos recebidos do JSON Schedule recebido na rota.
     * 
     * @param Void ; Vazio.
     * @return List<Event> ; Lista com os Ids dos eventos do DTO.
     * 
    */
    public List<UUID> getEvents() {
        return events;
    }

    /**
     *  GET para obter a data e horario do evento recebido no JSON da rota.
     * 
     * @param Void ; Vazio.
     * @return Date ; Data e horario do evento.
     * 
    */
    public Date getEventDateTime() {
        return eventDateTime;
    }
}