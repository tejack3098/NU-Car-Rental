package neu.csye6200.crs.controller;

import jakarta.servlet.http.HttpSession;
import neu.csye6200.crs.model.*;
import neu.csye6200.crs.service.Store;
import neu.csye6200.crs.util.CsvFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private final Store storeService;

    public AdminController(Store storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/AdminLogin")
    public String createPerson(Model model, HttpSession session) {

        if (session.isNew()) {
            return "admin_login";
        }
        else {
            if (session.getAttribute("admin") != null) {
                return handleAdminLogin(((Person)session.getAttribute("admin")).getUsername(),((Person)session.getAttribute("admin")).getPassword(), model,session );
            }
            session.invalidate();
            return "admin_login";
        }

    }

    @GetMapping("/AdminLogout")
    public String removePerson(Model model,  HttpSession session) {

        session.removeAttribute("admin");

        return "redirect:/AdminLogin";

    }

    @PostMapping("/AdminLogin")
    public String handleAdminLogin(@RequestParam String username,
                                   @RequestParam String password,
                                   Model model, HttpSession session) {
        if (storeService.authenticateAdmin(username, password)) {
            Person admin = new Person();
            admin.setRole("Administrator");
            admin.setUsername(username);
            admin.setPassword(password);

            session.setAttribute("admin", admin);

            List<Car> cars = storeService.getAllCars("Default");
            List<Application> applications = storeService.getAllApplications();
            List<Agent> agents = storeService.getAllAgents();
            List<LearningAppointment> learningAppointments = storeService.getAllAppointments();
            model.addAttribute("cars", cars);
            model.addAttribute("applications", applications);
            model.addAttribute("agents",agents);
            model.addAttribute("learningAppointments", learningAppointments);

            return "admin_dashboard";
        } else {

            model.addAttribute("errorMessage", "Invalid credentials");
            return "admin_login";
        }
    }

    @GetMapping("/AdminLogin/sortcars")
    public String sortCars(@RequestParam(required = false) String sortBy,
                            Model model, HttpSession session) {


        if (validateUser(session)) {
            if (sortBy == null) {
                sortBy = "";
            }
            List<Car> cars = storeService.getAllCars(sortBy);
            List<Application> applications = storeService.getAllApplications();
            List<Agent> agents = storeService.getAllAgents();
            List<LearningAppointment> learningAppointments = storeService.getAllAppointments();
            model.addAttribute("cars", cars);
            model.addAttribute("applications", applications);
            model.addAttribute("agents",agents);
            model.addAttribute("learningAppointments", learningAppointments);

            return "admin_dashboard";
        }
        else {

            model.addAttribute("errorMessage", "Invalid credentials");
            return "redirect:/";
        }
    }

    private boolean validateUser(HttpSession session)
    {
        if (session.isNew()) {
            return false;
        }
        Person admin = (Person)session.getAttribute("admin");
        return storeService.authenticateAdmin(admin.getUsername(), admin.getPassword());
    }

    @PostMapping("/acceptApplication")
    public String acceptApplication(@RequestParam("applicationId") int applicationId, Model model, HttpSession session) {


        Person adminData = (Person)session.getAttribute("admin");
        System.out.println(adminData.getUsername()+" "+adminData.getRole());

        Application acceptedApplication = storeService.getApplicationById(applicationId);


        Agent agent = new Agent();

        agent.setFirstName(acceptedApplication.getFirstName());
        agent.setLastName(acceptedApplication.getLastName());
        agent.setId(applicationId);


        model.addAttribute("agent", agent);
        return "add_agent";
    }



    @PostMapping("/declineApplication")
    public String declineApplication(@RequestParam("applicationId") int applicationId, Model model) {

        Application declinedApplication = storeService.getApplicationById(applicationId);

        storeService.getAllApplications().remove(declinedApplication);

        List<Car> cars = storeService.getAllCars("");
        List<Application> applications = storeService.getAllApplications();
        List<Agent> agents = storeService.getAllAgents();
        model.addAttribute("cars", cars);
        model.addAttribute("applications", applications);
        model.addAttribute("agents",agents);
        return "admin_dashboard";


    }


    @PostMapping("/addCredentials")
    public String createAgent(@RequestParam("applicationId") int applicationId, Model model,@ModelAttribute("agent") Agent agent) {


        Application acceptedApplication = storeService.getApplicationById(applicationId);

        agent.setFirstName(acceptedApplication.getFirstName());

        agent.setLastName(acceptedApplication.getLastName());
        agent.setId(applicationId);


        storeService.getAllAgents().add(agent);


        storeService.getAllApplications().remove(acceptedApplication);


        String filePath = "credentials";


        List<String[]> data = new ArrayList<>();
        data.add(new String[] {
                String.valueOf(agent.getId()),
                agent.getUsername(),
                agent.getPassword(),
                "Lib"
        });


        CsvFileUtil.writeCSV(filePath, data);


        model.addAttribute("message", "Agent can now log in !! ");
        return "add_agent";
    }

    @GetMapping("/admin/searchCars")
    public String searchCars(@RequestParam String keyword, Model model) {
        List<Car> cars = storeService.searchCars(storeService.getAllCars(""), keyword);
        if (cars.isEmpty()) {
            model.addAttribute("errorMessage", "No cars found for the given keyword");
        } else {
            model.addAttribute("cars", cars);
        }
        return "carSearchResults";
    }

    @GetMapping("/admin/searchAgents")
    public String searchAgents(@RequestParam String keyword, Model model) {
        List<Agent> agents = storeService.getAllAgents().stream()
                .filter(agent -> agent.getFirstName().toLowerCase().contains(keyword.toLowerCase())
                        || agent.getLastName().toLowerCase().contains(keyword.toLowerCase())
                        || agent.getUsername().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        if (agents.isEmpty()) {
            model.addAttribute("errorMessage", "No agents found for the given keyword");
        } else {
            model.addAttribute("agents", agents);
        }
        return "agentSearchResults";
    }

    @GetMapping("/admin/searchApplications")
    public String searchApplications(@RequestParam String keyword, Model model) {
        List<Application> applications = storeService.getAllApplications().stream()
                .filter(application -> application.getFirstName().toLowerCase().contains(keyword.toLowerCase())
                        || application.getLastName().toLowerCase().contains(keyword.toLowerCase())
                        || application.getReason().toLowerCase().contains(keyword.toLowerCase())
                        || application.getExperience().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        if (applications.isEmpty()) {
            model.addAttribute("errorMessage", "No applications found for the given keyword");
        } else {
            model.addAttribute("agents", applications);
        }
        return "applicationSearchResults";
    }


}
