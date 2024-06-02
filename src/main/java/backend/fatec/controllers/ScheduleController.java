package backend.fatec.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import backend.fatec.dtos.ScheduleRecordDto;
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

import java.util.List;
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
    @GetMapping("/schedule")
    public ResponseEntity<List<Schedule>> getAllSchedule() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(scheduleRepository.findAll());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     *  Busca um agendamento que esteja ligado ao Id do evento enviado como parâmetro.
     * 
     * @param UUID ; Id do evento.
     * @return Schedule ; Agendamento econtrado ou mensagem de não encontrado.
     * 
    */
    @GetMapping("/schedule/{event}")
    public ResponseEntity<Schedule> getScheduleByActiveEvent(@PathVariable UUID event) {
        try{
            Schedule schedule = scheduleRepository.getActiveEventSchedule(event);
            if(schedule != null){
                return ResponseEntity.ok(schedule);
            }
            
            return ResponseEntity.notFound().build();
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
    public ResponseEntity<Schedule> setSchedule(@RequestBody @Valid ScheduleRecordDto scheduleRecordDto) {
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
    @DeleteMapping("/schedule/{scheduleId}/event/{eventId}")
    public ResponseEntity<Boolean> deleteSchedule(@PathVariable UUID scheduleId, @PathVariable UUID eventId){
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

            return ResponseEntity.ok().body(true);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @DeleteMapping("/schedule/{scheduleId}")
    public ResponseEntity<Boolean> deleteSchedule(@PathVariable UUID scheduleId){
        try{    
            var schedule = scheduleRepository.findById(scheduleId);
            Schedule scheduleToVerify = new Schedule();
            if(schedule != null){
                BeanUtils.copyProperties(schedule, scheduleToVerify);
                scheduleRepository.deleteById(scheduleId);
            }

            return ResponseEntity.ok().body(true);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
    /* Criar uma rota (PUT) para adicionar um evento */

    /* Criar uma rota (PUT) para remover um evento */
} 
