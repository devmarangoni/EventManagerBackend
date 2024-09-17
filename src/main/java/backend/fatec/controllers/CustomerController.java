package backend.fatec.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import backend.fatec.dtos.CustomerRecordDto;
import backend.fatec.dtos.ErrorResponseRecordDto;
import backend.fatec.models.Customer;
import backend.fatec.repositories.CustomerRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;
    
    /**
     * Busca todos os clientes salvos no banco de dados utilizando o método findAll() do repository do customer.
     * 
     * @param Void não possui parâmetros
     * @return List<Customer>; Lista de todos clientes salvos no banco em formato JSON.
     * 
    */
    @GetMapping("/customer")
    public ResponseEntity<?> getAllCustomers() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerRepository.findAll());
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao obter todos clientes"));
        }
    }

    /**
     * Busca os dados do cliente pelo Id de usuário
     * 
     * @param Void não possui parâmetros
     * @return Customer; Cliente salvo no banco em formato JSON.
     * 
    */
    @GetMapping("/customer/user/{userId}")
    public ResponseEntity<?> getCustomerByUserId(@PathVariable UUID userId){
        System.out.println(userId);
        try{
            Optional<Customer> findedCustomer = customerRepository.getCustomerByUserId(userId);
            System.out.println(findedCustomer);
            if(findedCustomer.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(findedCustomer.get());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("Usuário não é um cliente"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao obter cliente"));
        }
    }
    
    /**
     *  Cria um cliente novo no banco de dados e retorna os dados do cliente.
     * 
     * @param CustomerRecordDto ; Recebe um objeto JSON com os dados do cliente.
     * Formato esperado pelo DTO do cliente:
     * @NotBlank String name,
     * @NotBlank String mobile,
     * @NotNull User user,
     * String phone
     * 
     * @return Customer; Cliente criado no banco em formato de JSON.
     * 
    */
    @PostMapping("/customer")
    public ResponseEntity<?> setCustomer(@RequestBody @Valid CustomerRecordDto customerRecordDto) {
        System.out.println(customerRecordDto);
        System.out.println("entrei");
        try{
            var customer = new Customer();
            BeanUtils.copyProperties(customerRecordDto, customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customer));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao cadastrar cliente"));
        }
    }
}
