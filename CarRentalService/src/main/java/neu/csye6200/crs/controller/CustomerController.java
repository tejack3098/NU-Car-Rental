package neu.csye6200.crs.controller;

import jakarta.servlet.http.HttpSession;
import neu.csye6200.crs.model.*;
import neu.csye6200.crs.service.Store;
import neu.csye6200.crs.util.CsvFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {


    @Autowired
    private final Store storeService;

    private static final String ERRORMESSAGE = "No cars found for the given keyword";

    public CustomerController(Store storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/CustomerLogin")
    public String customerLogin(Model model, HttpSession session) {
        if (session.isNew()) {
            return "customer_login";
        }
        else {
            if (session.getAttribute("customer") != null) {
                return loginCheck(((Customer)session.getAttribute("customer")).getUsername(),session,((Customer)session.getAttribute("customer")).getPassword(), model );
            }
            session.invalidate();
            return "customer_login";
        }
    }

    @GetMapping("/CustomerLogout")
    public String removePerson(Model model,  HttpSession session) {

        session.removeAttribute("customer");

        return "redirect:/CustomerLogin";

    }


    @PostMapping("/CustomerLogin")
    public String loginCheck(@RequestParam String username, HttpSession session, @RequestParam String password, Model model) {



        if (storeService.authenticateCustomer(username, password)) {

            Customer customer = storeService.getCustomerByusername(username);


            session.setAttribute("customer", customer);

            System.out.println(customer.getFirstName());

            System.out.println(customer.getCarsRented().size());

            List<Car> returnRequests = customer.getReturnRequests();

            List<Car> borrowRequests = customer.getRentalRequests();

            List<Car> borrowedCars = customer.getCarsRented();


            List<Car> cars = storeService.getAllCars("");

            List<LearningAppointment> appointments = customer.getAppointmentsBooked();

            List<Agent> agents = storeService.getAllAgents();

            List<LearningAppointment> appointmentsAvailable = storeService.getAvailableAppointments();


            model.addAttribute("cars",cars);

            model.addAttribute("borrowRequests",borrowRequests);
            model.addAttribute("returnRequests",returnRequests);

            model.addAttribute("borrowedCars", borrowedCars);

            model.addAttribute("appointments", appointments);


            model.addAttribute("customer",customer);

            model.addAttribute("agents", agents);

            model.addAttribute("availableAppointments", appointmentsAvailable);



            return "customer_home";

        }

        model.addAttribute("errorMessage", "Invalid credentials");
        return "customer_login";


    }


    @GetMapping("/CustomerSignup")
    public String customerSignup( Model model) {

        Customer customer = new Customer();

        model.addAttribute("customer",customer);

        return "customer_signup";

    }


    @PostMapping("/CustomerSignUp")
    public String customerRegister(@ModelAttribute("customer") Customer customer, Model model) {


        storeService.addCustomer(customer);


        String filePath = "credentials";


        List<String[]> data = new ArrayList<>();
        data.add(new String[] {
                String.valueOf(customer.getCustomerId()),
                customer.getUsername(),
                customer.getPassword(),
                "Customer"
        });

        CsvFileUtil.writeCSV(filePath, data);

        model.addAttribute("message","Succefully signed up");

        return "customer_signup";



    }


    @PostMapping("/borrowCar")
    public String borrowCar(@RequestParam("carID") int carID,HttpSession session, @RequestParam("agentId") int agentId,  Model model) {


        Customer customer = (Customer) session.getAttribute("customer");

        Car car = storeService.getCarByid(carID);

        car.setRentedBy(customer);

        Agent agent = storeService.getAgentById(agentId);

        agent.getRentalRequests().add(car);

        customer.getRentalRequests().add(car);


        return "success_page";

    }

    @GetMapping("/customer/searchCars")
    public String searchCars(@RequestParam String keyword, Model model, HttpSession session) {
        List<Car> cars = storeService.searchCars(storeService.getAllCars(""), keyword);
        if (cars.isEmpty()) {
            model.addAttribute("errorMessage", ERRORMESSAGE);
        } else {
            model.addAttribute("cars", cars);
        }
        return "carSearchResults";
    }

    @GetMapping("/customer/searchCarsBorrowed")
    public String searchCarsBorrowed(@RequestParam String keyword, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<Car> cars = storeService.searchCars(customer.getCarsRented(), keyword);
        if (cars.isEmpty()) {
            model.addAttribute("errorMessage", ERRORMESSAGE);
        } else {
            model.addAttribute("cars", cars);
        }
        return "carSearchResults";
    }

    @GetMapping("/customer/searchBorrowRequests")
    public String searchBorrowRequests(@RequestParam String keyword, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<Car> cars = storeService.searchCars(customer.getRentalRequests(), keyword);
        if (cars.isEmpty()) {
            model.addAttribute("errorMessage", ERRORMESSAGE);
        } else {
            model.addAttribute("cars", cars);
        }
        return "carSearchResults";
    }

    @GetMapping("/customer/searchReturnRequests")
    public String searchReturnRequests(@RequestParam String keyword, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<Car> cars = storeService.searchCars(customer.getReturnRequests(), keyword);
        if (cars.isEmpty()) {
            model.addAttribute("errorMessage", ERRORMESSAGE);
        } else {
            model.addAttribute("cars", cars);
        }
        return "carSearchResults";
    }

    @PostMapping("/requestLearningAppointment")
    public String requestLearningAppointment(@RequestParam("appointmentId") int appointmentId, HttpSession session, Model model, @RequestParam("fromDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDateTime, @RequestParam("toDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDateTime)
    {

        Customer customer = (Customer) session.getAttribute("customer");

        LearningAppointment appointment = storeService.getAppointmentById(appointmentId);

        appointment.setRentedBy(customer);
        appointment.setOccupied(true);
        appointment.setFromDateTime(fromDateTime);
        appointment.setToDateTime(toDateTime);

        storeService.removeAppointmentFromAvailablity(storeService.getAvailableAppointments(), appointmentId);

        customer.getAppointmentsBooked().add(appointment);


        return "request_success";
    }

    @PostMapping("/checkoutLearningAppointment")
    public String checkOutLearningAppointment(@RequestParam("appointmentId") int appointmentId, HttpSession session, Model model)
    {
        Customer customer = (Customer) session.getAttribute("customer");
        LearningAppointment appointment = storeService.getAppointmentById(appointmentId);

        appointment.setRentedBy(customer);
        appointment.setToDateTime(null);
        appointment.setFromDateTime(null);
        appointment.setOccupied(false);
        appointment.setRentedBy(null);
        storeService.makeAppointmentAvailable(appointment);
        customer.getAppointmentsBooked().remove(appointment);

        storeService.removeAppointmentFromAvailablity(storeService.getAvailableAppointments(), appointmentId);

        return "appointment_checkout";
    }

    @PostMapping("/returnCar")
    public String returnCars(@RequestParam("carID") int id, HttpSession session, Model model )
    {
        Customer customer = (Customer) session.getAttribute("customer");

        List<Car> carsBorrowed = customer.getCarsRented();
        List<Car> carsToRemove = new ArrayList<>();

        for(Car car : carsBorrowed) {
            if(car.getCarID() == id) {
                storeService.getAllCars("").add(car);
                carsToRemove.add(car);
            }
        }

        for(Car car : carsToRemove) {
            carsBorrowed.remove(car);
        }

        return "success_page";

    }
    
}
