package backend.fatec.repositories;

import java.util.List;
import java.util.Optional;
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

    /**
     *  Realiza uma consulta no banco de dados, buscando um Agendamento(Schedule) pelo id do cliente.
     * 
     * @param UUID ; Id do evento.
     * @return Schedule ; Agendamento encontrado ou NULL.
     * 
    */
    @Query("SELECT s FROM Schedule s JOIN s.events e WHERE e.customer.customerId = :CUSTOMER_ID AND e.finished = FALSE")
    Optional<Schedule> getActiveEventScheduleByCustomer(@Param("CUSTOMER_ID") UUID customerId);

    /**
     *  Realiza uma consulta no banco de dados, buscando todos agendamentos do mês da requisição para frente, que não são orçamentos, ou seja, reais. 
     * Que ainda não foram finalizados.
     * 
     * @return List<Schedule> ; Agendamentos encontrados ou array vazio.
     * 
    */
    @Query("SELECT s FROM Schedule s JOIN s.events e WHERE e.isBudget = TRUE AND e.finished = FALSE AND e.customer.customerId != :CUSTOMER_ID")
    List<Schedule> getNextEventsScheduled(@Param("CUSTOMER_ID") UUID customerId);
}