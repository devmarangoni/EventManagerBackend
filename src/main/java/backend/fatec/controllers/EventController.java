package backend.fatec.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import backend.fatec.dtos.ErrorResponseRecordDto;
import backend.fatec.dtos.EventRecordDto;
import backend.fatec.dtos.SuccessResponseRecordDto;
import backend.fatec.models.Customer;
import backend.fatec.models.Event;
import backend.fatec.repositories.EventRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
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
    @GetMapping("/admin/event")
    public ResponseEntity<?> getAllEvents(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(eventRepository.findAll());
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao obter todos eventos"));
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
    public ResponseEntity<?> getActiveEventByCustomer(@PathVariable UUID customer) {
        try{
            Optional<Event> event = eventRepository.getCustomerActiveEvent(customer);
            if(event.isPresent()){
                System.out.println("evento diferente de nulo");
                return ResponseEntity.ok(event);
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseRecordDto("Erro ao obter o evento ativo do cliente"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao obter o evento ativo do cliente"));
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
    public ResponseEntity<?> getAllCustomerEvents(@PathVariable UUID customer) {
        try{
            List<Event> customerEvents = eventRepository.getAllCustomerEvents(customer);
            if(customerEvents.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("Cliente não possui nenhum evento"));
            }
            
            return ResponseEntity.ok(customerEvents);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao obter todos eventos do cliente"));
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
     * @NotNull float value,
     * @NotNull Boolean isBudget,
     * Boolean finished
     * 
     * @return Event ; evento criado no banco em formato de JSON.
     * 
    */
    @PostMapping("/event")
    public ResponseEntity<?> setParty(@RequestBody @Valid EventRecordDto eventRecordDto) {
        try{
            var event = new Event();
            BeanUtils.copyProperties(eventRecordDto, event);

            Optional<Event> activeEvent = eventRepository.getCustomerActiveEvent((event.getCustomer().getCustomerId()));
            if(activeEvent.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("Existe um evento ativo pendente para esse cliente"));
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(eventRepository.save(event));  
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao criar o evento"));
        }
    }

    /**
     *  Atualiza um evento
     * 
     * @param EventRecordDto ; 
     * @return EventRecordDto updated
     * 
    */
    @PutMapping("/event")
    public ResponseEntity<?> updateParty(@RequestBody @Valid EventRecordDto eventRecordDto){
        try{
            if(eventRecordDto.eventId() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("O ID do evento é obrigatório"));
            }

            Optional<Event> existingEvent = eventRepository.findById(eventRecordDto.eventId());
            if(existingEvent.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseRecordDto("Evento não encontrado"));
            }

            Event event = existingEvent.get();
            BeanUtils.copyProperties(eventRecordDto, event, "eventId");
            
            return ResponseEntity.ok(eventRepository.save(event));
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao atualizar o evento"));
        }
    }
}
