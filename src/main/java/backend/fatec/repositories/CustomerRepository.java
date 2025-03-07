package backend.fatec.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.fatec.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>{}