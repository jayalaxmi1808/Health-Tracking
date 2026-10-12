
package com.healthtracker.demo;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.healthtracker.demo.model.HealthActivity;
import com.healthtracker.demo.repository.HealthActivityRepository;

@Controller
public class HealthActivityController {

    private final HealthActivityRepository repository;

    public HealthActivityController(
            HealthActivityRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/activities")
    public String showActivities(
            Authentication authentication, Model model) {

        String username = authentication.getName();

        List<HealthActivity> activities =
                repository.findByUsername(username);

        model.addAttribute("activities", activities);
        return "activities";
    }

    @PostMapping("/activities/{id}/complete")
    public String completeActivity(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String username = authentication.getName();

        HealthActivity activity = repository
                .findByIdAndUsername(id, username)
                .orElse(null);

        if (activity != null) {
            activity.setCompleted(true);
            repository.save(activity);
            redirectAttributes.addFlashAttribute(
                    "success", "Activity completed!");
        }

        return "redirect:/activities";
    }

    @PostMapping("/activities/add")
    public String addActivity(
            @RequestParam String activityName,
            @RequestParam String scheduledTime,
            Authentication authentication) {

        if (activityName == null || activityName.isBlank()
                || scheduledTime == null
                || scheduledTime.isBlank()) {
            return "redirect:/activities";
        }

        HealthActivity activity = new HealthActivity();
        activity.setUsername(authentication.getName());
        activity.setActivityName(activityName.trim());
        activity.setScheduledTime(scheduledTime.trim());
        activity.setCompleted(false);

        repository.save(activity);

        return "redirect:/activities";
    }
}
