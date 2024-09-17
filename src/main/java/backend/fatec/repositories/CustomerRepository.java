package backend.fatec.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.fatec.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>{
    /**
     *  Realiza uma consulta no banco de dados, buscando um cliente pelo userId
     * 
     * @param UUID ; Id do usuário.
     * @return Customer ; Cliente encontrado ou null
     * 
    */
    @Query("SELECT c FROM Customer c WHERE c.user.userId = :USER_ID")
    Optional<Customer> getCustomerByUserId(@Param("USER_ID") UUID userId);
}