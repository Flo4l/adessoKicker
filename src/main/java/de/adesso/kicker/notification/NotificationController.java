package de.adesso.kicker.notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import de.adesso.kicker.team.TeamService;
import de.adesso.kicker.user.UserService;

@RestController
public class NotificationController {

    private NotificationService notificationService;
    private UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {

        this.notificationService = notificationService;
        this.userService = userService;
    }

    @RequestMapping("/notifications")
    public List<Notification> getNotifications() {

        return notificationService.getAllNotifications();
    }

    @RequestMapping("/{userId}/notifications/send")
    public List<Notification> getUserNotificationsSend(@PathVariable String userId) {

        return notificationService.getAllNotificationsBySender(userService.getUserById(userId));
    }

    @RequestMapping("/{userId}/notifications/received")
    public List<Notification> getUserNotificationsReceived(@PathVariable String userId) {

        return notificationService.getAllNotificationsByReceiver(userService.getUserById(userId));
    }
}
