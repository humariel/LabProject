package Application;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/quote")
    public String quote(Model model){
        Quote q = ScheduledTasks.getRecentQuote();
        Value v = q.getValue();
        model.addAttribute("quoteID", v.getId());
        model.addAttribute("quote", v.getQuote());
        return "quote";
    }
}
