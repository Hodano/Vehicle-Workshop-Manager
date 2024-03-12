package pl.hodan.carservice.common.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hodan.carservice.auth.services.AuthenticationService;
import pl.hodan.carservice.common.entity.Calendar;
import pl.hodan.carservice.common.service.CalendarsService;

import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@RestController
@RequestMapping(("/cars-service"))
public class CalendarController {
    private final CalendarsService calendarsService;
    private final AuthenticationService authenticationService;

    public CalendarController(CalendarsService calendarsService, AuthenticationService authenticationService) {
        this.calendarsService = calendarsService;
        this.authenticationService = authenticationService;
    }


    @GetMapping("/calendars")
    public ResponseEntity<List<Calendar>> getCalendarsByUserId() {
        Long userId = authenticationService.getCurrentUserId();

        List<Calendar> calendars = calendarsService.getCalendarsByUserId(userId);
        return ResponseEntity.ok(calendars);
    }

    @GetMapping("/calendar")
    public ResponseEntity<Calendar> getCalendarByCalendarId(@RequestParam Long calendarId) {
        Long userId = authenticationService.getCurrentUserId();

        Calendar calendar = calendarsService.getCalendarByUserIdAndCalendarId(userId, calendarId);

        return ResponseEntity.ok(calendar);
    }

    @PostMapping("/add-calendar")
    public ResponseEntity<String> addCalendar(@RequestBody Calendar calendar) {
        Long userId = authenticationService.getCurrentUserId();

        if (calendarsService.addCalendar(userId, calendar))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>("Calendar could not be added", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("modify-calendar")
    public ResponseEntity<String> modifyCalendar(@RequestParam Long calendarId, @RequestBody Calendar newCalendar) {
        Long userId = authenticationService.getCurrentUserId();

        if (calendarsService.modifyCalendarWithUserIdByCalendarId(userId, calendarId, newCalendar))
            return new ResponseEntity<>("Calendar modified", HttpStatus.ACCEPTED);
        return new ResponseEntity<>("Calendar not found or could not be modified", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("delete-calendar")
    public ResponseEntity<String> deleteCalendar(@RequestParam Long calendarId) {
        Long userId = authenticationService.getCurrentUserId();

        if (calendarsService.deleteCalendarWithUserIdByCalendarId(userId, calendarId))
            return new ResponseEntity<>("Calendar deleted", HttpStatus.OK);
        return new ResponseEntity<>("Calendar not found or could not be deleted", HttpStatus.NOT_FOUND);
    }

}
