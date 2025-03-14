package backend.fatec.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.fatec.models.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>{
    /**
     *  Realiza uma consulta no banco de dados, buscando um Evento do cliente enviado no parâmetro que não esteja finalizado E não seja um orçamento.
     * 
     * @param UUID ; Id do cliente.
     * @return Event ; Evento encontrado ou NULL.
     * 
    */
    @Query("SELECT e FROM Event e WHERE e.customer.customerId = :CUSTOMER_ID AND e.finished = FALSE")
    Optional<Event> getCustomerActiveEvent(@Param("CUSTOMER_ID") UUID customerId);

    /**
     *  Realiza uma consulta no banco de dados, buscando TODOS eventos do cliente enviado no parâmetro.
     * 
     * @param UUID ; Id do cliente.
     * @return List<Event> ; Lista com todos os eventos do cliente OU NULL.
     * 
    */
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.schedule WHERE e.customer.customerId = :CUSTOMER_ID")
    List<Event> getAllCustomerEvents(@Param("CUSTOMER_ID") UUID customerId);
}