package neu.csye6200.crs.model;

import java.util.ArrayList;
import java.util.List;

public class Agent extends Person {

    private int id;

    private double hoursWorked;

    private List<Car> rentalRequests;

    private List<Car> returnRequests;


    public Agent() {

        rentalRequests = new ArrayList<>();
        returnRequests = new ArrayList<>();
        hoursWorked = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
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
