package backend.fatec.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import backend.fatec.dtos.CustomerRecordDto;
import backend.fatec.models.Customer;
import backend.fatec.repositories.CustomerRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
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
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(customerRepository.findAll());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    /**
     *  Cria um cliente novo no banco de dados e retorna os dados do cliente.
     * 
     * @param CustomerRecordDto ; Recebe um objeto JSON com os dados do cliente.
     * Formato esperado pelo DTO do cliente:
     * @NotBlank String name,
     * @NotBlank String mobile,
     * String phone
     * 
     * @return Customer; Cliente criado no banco em formato de JSON.
     * 
    */
    @PostMapping("/customer")
    public ResponseEntity<Customer> setCustomer(@RequestBody @Valid CustomerRecordDto customerRecordDto) {
        try{
            var customer = new Customer();
            BeanUtils.copyProperties(customerRecordDto, customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customer));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
