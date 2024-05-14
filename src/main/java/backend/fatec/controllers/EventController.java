package backend.fatec.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import backend.fatec.dtos.EventRecordDto;
import backend.fatec.models.Event;
import backend.fatec.models.Schedule;
import backend.fatec.repositories.EventRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping
public class EventController {
    @Autowired
    EventRepository eventRepository;
    
    /**
     *  Busca todos os eventos salvos no banco de dados utilizando o método findAll() do repository de event.
     * 
     * @param Void não possui parâmetros
     * @return List<Event>; Lista de todos eventos salvos no banco em formato JSON.
     * 
    */
    @GetMapping("/event")
    public ResponseEntity<List<Event>> getAllEvents(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(eventRepository.findAll());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     *  Busca o evento ativo do cliente solicitado.
     * 
     * @param UUID ; Id do cliente que será buscado o evento ativo.
     * @return Event ; Evento encontrado e caso não encontre um evento ativo para este cliente retorna nada.
     * 
    */
    @GetMapping("/event/{customer}")
    public ResponseEntity<Event> getActiveEventByCustomer(@PathVariable UUID customer) {
        try{
            var event = eventRepository.getCustomerActiveEvent(customer);
            System.out.println("Evento obtido");
            System.out.println(event);
            if(event != null){
                System.out.println("evento diferente de nulo");
                return ResponseEntity.ok(event);
            }
            
            return ResponseEntity.notFound().build();
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     *  Busca todos os eventos do cliente solicitado, sendo eventos finalizados ou não.
     * 
     * @param UUID ; Id do cliente que será buscado o evento ativo.
     * @return List<Event> ; Lista com os eventos encontrados e caso não encontre eventos retorna que não foi encontrado nada.
     * 
    */
    @GetMapping("/events/{customer}")
    public ResponseEntity<List<Event>> getAllCustomerEvents(@PathVariable UUID customer) {
        try{
            List<Event> customerEvents = eventRepository.getAllCustomerEvents(customer);
            if(customerEvents.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(customerEvents);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    /**
     *  Cria um evento novo no banco de dados e retorna os dados do evento.
     * 
     * @param EventRecordDto ; Recebe um objeto JSON com os dados do evento.
     * Formato esperado pelo DTO do evento:
     * @NotBlank String length,
     * @NotBlank String address,
     * @NotNull Customer customer,
     * Schedule schedule,
     * String theme,
     * String description,
     * String birthdayPerson,
     * @NotN ull float value,
     * @NotNull Boolean isBudget,
     * Boolean finished
     * 
     * @return Event ; evento criado no banco em formato de JSON.
     * 
    */
    @PostMapping("/event")
    public ResponseEntity<Event> setParty(@RequestBody @Valid EventRecordDto eventRecordDto) {
        try{
            var event = new Event();
            BeanUtils.copyProperties(eventRecordDto, event);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventRepository.save(event));  
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
