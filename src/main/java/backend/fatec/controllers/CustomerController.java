package backend.fatec.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import backend.fatec.dtos.CustomerRecordDto;
import backend.fatec.dtos.ErrorResponseRecordDto;
import backend.fatec.dtos.EventRecordDto;
import backend.fatec.dtos.SuccessResponseRecordDto;
import backend.fatec.models.Customer;
import backend.fatec.models.Event;
import backend.fatec.models.Schedule;
import backend.fatec.repositories.CustomerRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    /**
     *  Deleta um cliente
     * 
     * @param customerId ; 
     * @return message
     * 
    */
    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable UUID customerId){
        try{    
            var customer = customerRepository.findById(customerId);
            Customer customerToVerify = new Customer();
            if(customer != null){
                BeanUtils.copyProperties(customer, customerToVerify);
                customerRepository.deleteById(customerId);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseRecordDto("Cliente excluido com sucesso!"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao deletar o cliente"));
        }
    }

    /**
     *  Atualiza os dados do cliente
     * 
     * @param CustomerRecordDto; 
     * @return updatedCustomer
     * 
    */
    @PutMapping("/customer")
    public ResponseEntity<?> updateCustomer(@RequestBody @Valid CustomerRecordDto customerRecordDto){
        try{
            if(customerRecordDto.customerId() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("O ID do cliente é obrigatório"));
            }

            Optional<Customer> existingCustomer = customerRepository.findById(customerRecordDto.customerId());
            if(existingCustomer.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseRecordDto("Cliente não encontrado"));
            }

            Customer customer = existingCustomer.get();
            BeanUtils.copyProperties(customerRecordDto, customer, "customerId");
            
            return ResponseEntity.ok(customerRepository.save(customer));
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao atualizar o cliente"));
        }
    }
}
