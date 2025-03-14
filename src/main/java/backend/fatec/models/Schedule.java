package backend.fatec.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "schedule")
public class Schedule {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID scheduleId;

    private Date eventDateTime; /* Horário do agendamento */

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;

    public Schedule(){
        this.events = new ArrayList<>();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public UUID getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(UUID scheduleId){
        this.scheduleId = scheduleId;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }
    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public List<Event> getEvents() {
        return events;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * Adiciona um evento ao agendamento.
     * 
     * @param event O evento a ser adicionado no agendamento.
     * @return void
    */
    public void addEvent(Event event){
        events.add(event);
        event.setSchedule(this);
    }

    /**
     * Remove um evento do agendamento.
     * 
     * @param event O evento a ser removido do agendamento.
     * @return void
    */
    public void removeEvent(Event event) {
        events.remove(event);
        event.setSchedule(null);
    }

    /**
     * Retorna se o agendamento possui menos de dois eventos.
     * 
     * @param void
     * @return boolean
    */
    public Boolean hasLessThanTwoEvents(){
        return events.size() < 2;
    }
}
