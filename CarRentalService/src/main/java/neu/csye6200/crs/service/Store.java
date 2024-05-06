package neu.csye6200.crs.service;

import neu.csye6200.crs.model.*;
import neu.csye6200.crs.util.CsvFileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Store {


    private static int applicationIdCounter = 1;

    private static int customerIdCounter = 1;

    private  List<Customer> customers;

    private List<Agent> agents;

    private  List<LearningAppointment> appointments;

    private List<LearningAppointment> availableAppointments;

    private List<LearningAppointment> caredAppointments;

    private  List<Car> cars;

    private  List<Car> borrowedCars;

    private Person admin;

    private  List<Application> applications;


    private static class CarFactory {
        public static Car createCar(String[] data) {
            int carID = Integer.parseInt(data[0]);
            String title = data[1];
            String author = data[2];
            String genre = data[3];
            String language = data[4];
            return new Car(carID, title, author, genre, language);
        }
    }

    private static class AppointmentsFactory {
        public static LearningAppointment createAppointment(String[] data) {
            int appointmentId = Integer.parseInt(data[0]);
            int appointmentNumber = Integer.parseInt(data[1]);
            return new LearningAppointment(appointmentId, appointmentNumber);
        }
    }


    public Store(@Value("${store.cars.csv.path}") String carsCSV,
                   @Value("${store.admin.username}") String username,
                   @Value("${store.admin.password}") String password,
                   @Value("${store.learningAppointments.csv.path}") String appointmentsCSV) {


        cars = new ArrayList<>();
        applications = new ArrayList<>();
        agents = new ArrayList<>();
        customers = new ArrayList<>();
        borrowedCars = new ArrayList<>();
        appointments = new ArrayList<>();
        availableAppointments = new ArrayList<>();
        caredAppointments = new ArrayList<>();

        List<String[]> rawData = CsvFileUtil.readCSV(carsCSV);


        for (int i = 0; i < rawData.size(); i++) {
            Car car = CarFactory.createCar(rawData.get(i));
            cars.add(car);
        }

        List<String[]> appointmentsRawData = CsvFileUtil.readCSV(appointmentsCSV);

        for(int i=0; i< appointmentsRawData.size(); i++)
        {
            LearningAppointment privateAppointment = AppointmentsFactory.createAppointment(appointmentsRawData.get(i));
            appointments.add(privateAppointment);
            availableAppointments.add(privateAppointment);
        }

        admin = new Person();

        admin.setFirstName("Admin");
        admin.setLastName("Admin");

        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole("ADMIN");


    }


    public boolean authenticateAdmin(String username, String password) {
        return admin.getUsername().equals(username) && admin.getPassword().equals(password);
    }


    public List<Car> getAllCars(String sortBy) {

        if (sortBy.equals("Model")) {

            cars.sort(Car.Model_COMPARATOR);
            return cars;
        }
        if (sortBy.equals("Company")) {


            cars.sort(Car.Company_COMPARATOR);
            return cars;
        }
        if (sortBy.equals("Type")) {

            cars.sort(Car.Type_COMPARATOR);
            return cars;
        }
        if (sortBy.equals("FuelType")) {

            cars.sort(Car.FuelType_COMPARATOR);
            return cars;
        }


        cars.sort(Car.CarId_COMPARATOR);

        return cars;
    }


    public List<Application> getAllApplications() {

        return applications;

    }


    public List<Agent> getAllAgents() {

        return agents;

    }



    public void addApplication(Application application) {
        application.setId(applicationIdCounter++);
        applications.add(application);
    }

    public void addCustomer(Customer customer) {
        customer.setCustomerId(customerIdCounter++);
        customers.add(customer);
    }


    public Application getApplicationById(int applicationId) {
        for (Application application : applications) {
            if (application.getId() == applicationId) {
                return application;
            }
        }
        return null;
    }


    public boolean authenticateAgent(String username, String password) {

        String filePath = "credentials";


        List<String[]> users = CsvFileUtil.readCSV(filePath);


        for (String[] userData : users) {

            if (userData.length >= 4) {

                String csvUsername = userData[1];


                String csvPassword = userData[2];
                String role = userData[3];


                System.out.println(csvUsername);
                System.out.println(csvPassword);
                System.out.println(role);


                if (csvUsername.equals(username) && csvPassword.equals(password) && role.equals("Lib")) {

                    return true;
                }
            }

        }

        return false;


    }

    public Agent getAgentByusername(String username) {

        for (Agent agent : agents) {
            if (agent.getUsername().equals(username)) {
                return agent;
            }
        }

        return null;

    }


    public Agent getAgentById(int id) {

        for (Agent agent : agents) {
            if (agent.getId() == id) {
                return agent;
            }
        }

        return null;

    }


    public List<Car> getBorrowedCars() {
        return borrowedCars;
    }


    public void setBorrowedCars(List<Car> borrowedCars) {
        this.borrowedCars = borrowedCars;
    }


    public List<Car> getBorrowRequests(Agent agent) {


        return agent.getRentalRequests();


    }

    public List<Car> getReturnRequests(Agent agent) {

        return agent.getReturnRequests();

    }


    public boolean authenticateCustomer(String username, String password) {


        String filePath = "credentials";

        List<String[]> users = CsvFileUtil.readCSV(filePath);





        for (String[] userData : users) {


            if (userData.length >= 4) {
                String csvUsername = userData[1];


                String csvPassword = userData[2];
                String role = userData[3];


                System.out.println(csvUsername);
                System.out.println(csvPassword);
                System.out.println(role);


                if (csvUsername.equals(username) && csvPassword.equals(password) && role.equals("Customer")) {

                    return true;
                }
            }

        }

        return false;

    }


    public Customer getCustomerByusername(String username) {

        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }

        return null;

    }


    public Car getCarByid(int id) {

        for (Car car : cars) {

            if (car.getCarID()==id) {
                return car;
            }
        }

        return null;

    }

    public void deleteCar(List<Car> cars, int id) {
        cars.removeIf(car -> car.getCarID() == id);
        System.out.println("Removed car with " + id);
    }

    public List<LearningAppointment> getAllAppointments()
    {
        return this.appointments;
    }

    public List<LearningAppointment> getAvailableAppointments()
    {
        return this.availableAppointments;
    }

    public void setAvailableAppointments(List<LearningAppointment> availableAppointments)
    {
        this.availableAppointments=availableAppointments;
    }

    public void removeAppointmentFromAvailablity(List<LearningAppointment> availalbleAppointments, int appointmentId)
    {
        availableAppointments.removeIf(appointment -> appointment.getAppointmentId() == appointmentId);
        addAppointmentsToOccupiedStatus(appointmentId);

    }

    public void makeAppointmentAvailable(LearningAppointment appointment)
    {
        availableAppointments.add(appointment);
    }

    public LearningAppointment getAppointmentById(int appointmentId)
    {
        for (LearningAppointment appointment: appointments)
        {
            if(appointment.getAppointmentId() == appointmentId)
            {
                return appointment;
            }
        }
        return null;
    }

    public List<LearningAppointment> getOcccupiedAppointments()
    {
        return this.caredAppointments;
    }

    public void addAppointmentsToOccupiedStatus(int appointmentId)
    {
        LearningAppointment appointmentDetails = getAppointmentById(appointmentId);
        appointmentDetails.setOccupied(true);
        caredAppointments.add(appointmentDetails);
    }

    public List<Car> searchCars(List<Car> cars, String query) {
        return cars.stream()
                .filter(car -> car.getModel().toLowerCase().contains(query.toLowerCase())
                        || car.getCompany().toLowerCase().contains(query.toLowerCase())
                        || car.getType().toLowerCase().contains(query.toLowerCase())
                        || car.getFuelType().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Agent> searchAgents(List<Agent> agents, String query) {
        return agents.stream()
                .filter(agent -> agent.getFirstName().toLowerCase().contains(query.toLowerCase())
                        || agent.getLastName().toLowerCase().contains(query.toLowerCase())
                        || agent.getUsername().toLowerCase().contains(query.toLowerCase())
                        || agent.getRole().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Application> searchApplications(List<Application> applications, String query) {
        return applications.stream()
                .filter(application -> application.getFirstName().toLowerCase().contains(query.toLowerCase())
                        || application.getLastName().toLowerCase().contains(query.toLowerCase())
                        || application.getReason().toLowerCase().contains(query.toLowerCase())
                        || application.getExperience().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

}
