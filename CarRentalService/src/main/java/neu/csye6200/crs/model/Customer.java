package neu.csye6200.crs.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {

    int customerId;

    List<Car> carsRented;

    List<LearningAppointment> appointmentsBooked;

    long dueAmount;

    List<Car> rentalRequests;

    List<Car> returnRequests;

    public Customer() {

        carsRented = new ArrayList<>();
        appointmentsBooked = new ArrayList<>();
        rentalRequests = new ArrayList<>();
        returnRequests = new ArrayList<>();

        dueAmount = 0;

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Car> getCarsRented() {
        return carsRented;
    }

    public void setCarsRented(List<Car> carsRented) {
        this.carsRented = carsRented;
    }

    public List<LearningAppointment> getAppointmentsBooked() {
        return appointmentsBooked;
    }

    public void setAppointmentsBooked(List<LearningAppointment> appointmentsBooked) {
        this.appointmentsBooked = appointmentsBooked;
    }

    public long getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(long dueAmount) {
        this.dueAmount = dueAmount;
    }

    public List<Car> getRentalRequests() {
        return rentalRequests;
    }

    public void setRentalRequests(List<Car> rentalRequests) {
        this.rentalRequests = rentalRequests;
    }

    public List<Car> getReturnRequests() {
        return returnRequests;
    }

    public void setReturnRequests(List<Car> returnRequests) {
        this.returnRequests = returnRequests;
    }
}
