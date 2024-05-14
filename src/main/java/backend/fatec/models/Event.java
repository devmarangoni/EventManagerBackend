package backend.fatec.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "event")
public class Event {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")

    private UUID eventId;
    private String length; /* P - M - G */
    private String address; /* Endereço da festa */
    private String theme; /* Decoração da festa */
    private String birthdayPerson; /* nome do aniversariante */
    private String description; /* Descrição da festa */
    private Boolean finished; /* Default false */
    private Boolean isBudget; /* É orçamento */
    private Float value; /* preço da festa */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public UUID getEventId(){
        return eventId;
    }
    public void setEventId(UUID eventId){
        this.eventId = eventId;
    }

    public String getLength(){
        return length;
    }
    public void setLength(String length){
        this.length = length;
    }

    public String getTheme(){
        return theme;
    }
    public void setTheme(String theme){
        this.theme = theme;
    }

    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public Customer getCustomer(){
        return customer;
    }
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public Boolean getFinished(){
        return finished;
    }
    public void setFinished(Boolean finished){
        this.finished = finished;
    }

    public Schedule getSchedule(){
        return schedule;
    }
    public void setSchedule(Schedule schedule){
        this.schedule = schedule;
    }

    public Boolean getIsBudget(){
        return isBudget;
    }
    public void setIsBudget(Boolean isBudget){
        this.isBudget = isBudget;
    }

    public Float getValue(){
        return value;
    }
    public void setValue(Float value){
        this.value = value;
    }

    public String getBirthdayPerson(){
        return birthdayPerson;
    }
    public void setBirthdayPerson(String birthdayPerson){
        this.birthdayPerson = birthdayPerson;
    }
}