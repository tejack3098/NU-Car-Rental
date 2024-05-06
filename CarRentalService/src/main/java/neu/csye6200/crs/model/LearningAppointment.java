package neu.csye6200.crs.model;

import java.time.LocalDateTime;

public class LearningAppointment {

    private int appointmentId;
    private Customer rentedBy;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    private int carNumber;
    private boolean isOccupied;

    public LearningAppointment(int appointmentId, int carNumber) {
        this.appointmentId = appointmentId;
        this.rentedBy = null;
        this.fromDateTime = null;
        this.toDateTime = null;
        this.carNumber = carNumber;
        this.isOccupied = false;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Customer getRentedBy() {
        return rentedBy;
    }

    public void setRentedBy(Customer rentedBy) {
        this.rentedBy = rentedBy;
    }

    public LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(LocalDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public LocalDateTime getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(LocalDateTime toDateTime) {
        this.toDateTime = toDateTime;
    }

    public int getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    @Override
    public String toString(){
        return "Car Number: " + carNumber + ", Occupied: " + isOccupied + "By: " + rentedBy;
    }

}
