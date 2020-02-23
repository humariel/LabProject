package Application;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/flights")
    public String flights(Model model){
        Object[] f = ScheduledTasks.getRecentFlights();
        model.addAttribute("flights", f);
        return "flights";
    }
}
