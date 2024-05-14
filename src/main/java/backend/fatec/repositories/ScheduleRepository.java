package backend.fatec.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.fatec.models.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID>{
    /**
     *  Realiza uma consulta no banco de dados, buscando um Agendamento(Schedule) que possua o evento enviado como parâmetro e seja um Evento que ainda não foi finalizado.
     * 
     * @param UUID ; Id do evento.
     * @return Schedule ; Agendamento encontrado ou NULL.
     * 
    */
    @Query("SELECT s FROM Schedule s JOIN s.events e WHERE e.eventId = :EVENT_ID AND e.finished = FALSE")
    Schedule getActiveEventSchedule(@Param("EVENT_ID") UUID eventId);
}