package neu.csye6200.crs.controller;

import jakarta.servlet.http.HttpSession;
import neu.csye6200.crs.model.*;
import neu.csye6200.crs.service.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AgentController {

    @Autowired
    private final Store storeService;

    public AgentController(Store storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/applyAgent")
    public String applyAgent(Model model) {

        Application application = new Application();

        model.addAttribute("application", application);
        return "apply_Agent";
    }

    @PostMapping("/applyAgent")
    public String createAgent(@ModelAttribute("application") Application application, Model model) {

        storeService.addApplication(application);

        for (Application app : storeService.getAllApplications()) {

            System.out.println(app.getId());

        }

        model.addAttribute("message", "Application submitted successfully!");

        return "apply_Agent";

    }

    @GetMapping("/AgentLogin")
    public String agentLogin(Model model, HttpSession session) {

        if (session.isNew()) {
            return "agent_login";
        }
        else {
            if (session.getAttribute("agent") != null) {
                return loginCheckAgent(((Agent)session.getAttribute("agent")).getUsername(),((Agent)session.getAttribute("agent")).getPassword(),model, session );
            }
            session.invalidate();
            return "agent_login";
        }

    }

    @GetMapping("/AgentLogout")
    public String removePerson(Model model,  HttpSession session) {

        session.removeAttribute("agent");

        return "redirect:/AgentLogin";

    }

    @PostMapping("/AgentLogin")
    public String loginCheckAgent(@RequestParam String username, @RequestParam String password, Model model,
                                      HttpSession session) {

        if (storeService.authenticateAgent(username, password)) {

            Agent agent = storeService.getAgentByusername(username);

            session.setAttribute("agent", agent);

            List<Car> cars 	      = storeService.getAllCars("");
            List<Car> returnRequests = storeService.getReturnRequests(agent);
            List<Car> borrowRequests = storeService.getBorrowRequests(agent);

            model.addAttribute("cars", cars);
            model.addAttribute("borrowRequests", borrowRequests);
            model.addAttribute("returnRequests", returnRequests);
            model.addAttribute("agent", agent);

            return "agent_home";

        }

        model.addAttribute("errorMessage", "Invalid credentials");
        return "agent_login";

    }

    @PostMapping("/acceptBorrowRequest")
    public String acceptBorrowRequest(@RequestParam("carID") int carID, HttpSession session, Model model) {

        Car car = storeService.getCarByid(carID);

        Customer customer = car.getRentedBy();

        System.out.println("borrowed by" + customer.getFirstName());

        car.setRented(true);

        storeService.getBorrowedCars().add(car);

        customer.getCarsRented().add(car);

        System.out.println("borrow request size" + customer.getRentalRequests().size());

        storeService.deleteCar(customer.getRentalRequests(), carID);

        storeService.deleteCar(storeService.getAllCars(""), carID);

        Agent agent = (Agent) session.getAttribute("agent");

        System.out.println("agent" + agent.getFirstName());

        System.out.println("lib borrow request size" + agent.getRentalRequests().size());

        storeService.deleteCar(agent.getRentalRequests(), carID);

        model.addAttribute("message", "borrow request accepted");

        model.addAttribute("agent", agent);

        List<Car> cars = storeService.getAllCars("");
        model.addAttribute("cars",cars );


        List<Car> borrowRequests = agent.getRentalRequests();

        List<Car> returnRequests = agent.getReturnRequests();

        model.addAttribute("returnRequests", returnRequests);
        model.addAttribute("borrowRequests", borrowRequests);

        return "agent_home";

    }

    @PostMapping("/updateHoursWorked")
    public String updateHoursWorked(
            @RequestParam double totalHours,
            HttpSession session,
            Model model) {

        // Retrieve the agent object from the session or by username
        Agent agent = (Agent) session.getAttribute("agent");

        // Check if the agent object exists
        if (agent != null) {
            // Update the hoursWorked attribute with the total hours
            agent.setHoursWorked(totalHours);

            // Add success message to model and return to the agent home page
            model.addAttribute("agent", agent);
            return "agent_home";
        } else {
            // Handle the case where the agent object is not found
            model.addAttribute("errorMessage", "Agent not found.");
            return "error_page";
        }
    }

    @GetMapping("/updateHoursWorked")
    public String updateHours(Model model) {

        Application application = new Application();

        model.addAttribute("application", application);
        return "agent_time_sheet";
    }
}
