package backend.fatec.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import backend.fatec.dtos.ErrorResponseRecordDto;
import backend.fatec.dtos.ScheduleRecordDto;
import backend.fatec.dtos.SuccessResponseRecordDto;
import backend.fatec.models.Event;
import backend.fatec.models.Schedule;
import backend.fatec.repositories.EventRepository;
import backend.fatec.repositories.ScheduleRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping
public class ScheduleController {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    EventRepository eventRepository;
    
    /**
     *  Busca todos os agendamentos do cliente criados no banco.
     * 
     * @param Void ; Vazio.
     * @return List<Schedule> ; Lista de todos agendamentos criados no banco.
     * 
    */
    @GetMapping("/admin/schedule")
    public ResponseEntity<?> getAllSchedule() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(scheduleRepository.findAll());
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao obter todos os agendamentos"));
        }
    }

    /**
     *  Busca um agendamento que esteja ligado ao Id do evento enviado como parâmetro.
     * 
     * @param UUID ; Id do evento.
     * @return Schedule ; Agendamento econtrado ou mensagem de não encontrado.
     * 
    */
    @GetMapping("/schedule/event/{event}")
    public ResponseEntity<?> getScheduleByActiveEvent(@PathVariable UUID event) {
        try{
            Schedule schedule = scheduleRepository.getActiveEventSchedule(event);
            if(schedule != null){
                return ResponseEntity.ok(schedule);
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("Agendamento não encontrado"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao obter o agendamento do evento ativo"));
        }
    }

    /**
     *  Busca um agendamento que esteja ligado ao Id do cliente enviado como parâmetro.
     * 
     * @param UUID ; Id do cliente.
     * @return Schedule ; Agendamento encontrado ou mensagem de não encontrado.
     * 
    */
    @GetMapping("/schedule/customer/{customerId}")
    public ResponseEntity<?> getActiveEventScheduleByCustomer(@PathVariable UUID customerId) {
        try{
            Optional<Schedule> schedule = scheduleRepository.getActiveEventScheduleByCustomer(customerId);
            if(schedule.isPresent()){
                return ResponseEntity.ok(schedule.get());
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseRecordDto("Agendamento não encontrado"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao obter o agendamento do evento ativo"));
        }
    }
    
    /**
     *  Cria um agendamento novo no banco de dados e retorna os dados do agendamento.
     * 
     * @param ScheduleRecordDto ; Recebe um objeto JSON com os dados do agendamento.
     * Formato esperado pelo DTO do agendamento:
     * @NotBlank Date eventDateTime,
     * @NotNull List<UUID> events
     * 
     * @return Schedule; agendamento criado no banco em formato de JSON.
     * 
    */
    @PostMapping("/schedule")
    public ResponseEntity<?> setSchedule(@RequestBody @Valid ScheduleRecordDto scheduleRecordDto) {
        System.out.println("entrei em schedule");
        System.out.println(scheduleRecordDto);
        try {
            Schedule schedule = new Schedule();
            schedule.setEventDateTime(scheduleRecordDto.getEventDateTime());

            for(UUID eventId : scheduleRecordDto.getEvents()){
                Optional<Event> optionalEvent = eventRepository.findById(eventId);
                if(optionalEvent.isPresent()){
                    Event event = optionalEvent.get();
                    schedule.addEvent(event);
                }
            }

            Schedule savedSchedule = scheduleRepository.save(schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao criar o agendamento"));
        }
    }

    /**
     *  Deleta o agendamento (caso possua apenas aquela festa agendada) OU remove a festa enviada no parametro do agendamento (caso o agendamento possua mais de uma festa).
     * 
     * @param UUID ; Id do agendamento.
     * @param UUID ; Id do evento.
     * @return Boolean ; TRUE (Caso tudo ocorra sem erros), FALSE (Caso ocorra algum ERRO Exception)
     * 
    */
    @DeleteMapping("/admin/schedule/{scheduleId}/event/{eventId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable UUID scheduleId, @PathVariable UUID eventId){
        try{
            Boolean canDeleteSchedule = false;
            
            var schedule = scheduleRepository.findById(scheduleId);
            Schedule scheduleToVerify = new Schedule();
            if(schedule != null){
                BeanUtils.copyProperties(schedule, scheduleToVerify);
                canDeleteSchedule = scheduleToVerify.hasLessThanTwoEvents();
            }
            
            if(canDeleteSchedule){
                scheduleRepository.deleteById(scheduleId);
            }

            var event = eventRepository.findById(eventId);
            Event eventToRemove = new Event();
            if(event != null){
                BeanUtils.copyProperties(event, eventToRemove);
                scheduleToVerify.removeEvent(eventToRemove);  
            }

            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseRecordDto("Agendamento deletado do evento"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao deletar o agendamento"));
        }
    }

    @DeleteMapping("/admin/schedule/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable UUID scheduleId){
        try{    
            var schedule = scheduleRepository.findById(scheduleId);
            Schedule scheduleToVerify = new Schedule();
            if(schedule != null){
                BeanUtils.copyProperties(schedule, scheduleToVerify);
                scheduleRepository.deleteById(scheduleId);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseRecordDto("Agendamento deletado"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto("Erro ao deletar o agendamento"));
        }
    }
    /* Criar uma rota (PUT) para adicionar um evento */

    /* Criar uma rota (PUT) para remover um evento */
} 
