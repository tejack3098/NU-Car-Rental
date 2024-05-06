package neu.csye6200.crs.model;

import java.util.Comparator;
import java.util.Date;

public class Car {


    private int carID;
    private String model;
    private String company;
    private String type;
    private String fuelType;
    private boolean isRented;
    private Date rentedDate;
    private Customer rentedBy;

    public static final Comparator<Car> Model_COMPARATOR = Comparator.comparing(Car::getModel);
    public static final Comparator<Car> Company_COMPARATOR = Comparator.comparing(Car::getCompany);
    public static final Comparator<Car> Type_COMPARATOR = Comparator.comparing(Car::getType);
    public static final Comparator<Car>  FuelType_COMPARATOR = Comparator.comparing(Car::getFuelType);
    public static final Comparator<Car> CarId_COMPARATOR = Comparator.comparing(Car::getCarID);

    public Car(int carID, String model, String company, String type, String fuelType) {
        this.carID = carID;
        this.model = model;
        this.company = company;
        this.type = type;
        this.fuelType = fuelType;
        this.isRented = false;
        this.rentedDate = null;
        this.rentedBy = null;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public Date getRentedDate() {
        return rentedDate;
    }

    public void setRentedDate(Date rentedDate) {
        this.rentedDate = rentedDate;
    }

    public Customer getRentedBy() {
        return rentedBy;
    }

    public void setRentedBy(Customer rentedBy) {
        this.rentedBy = rentedBy;
    }
}
