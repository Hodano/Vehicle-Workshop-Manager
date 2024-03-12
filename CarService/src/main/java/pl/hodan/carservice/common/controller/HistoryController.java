package pl.hodan.carservice.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hodan.carservice.auth.services.AuthenticationService;
import pl.hodan.carservice.common.entity.History;
import pl.hodan.carservice.common.service.HistoriesService;

import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@RestController
@RequestMapping("/cars-service")
public class HistoryController {
    private final HistoriesService historiesService;
    private final AuthenticationService authenticationService;

    public HistoryController(HistoriesService historiesService, AuthenticationService authenticationService) {
        this.historiesService = historiesService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/histories")
    public ResponseEntity<List<History>> getHistoriesByCarId(@RequestParam Long carId) {
        Long userId = authenticationService.getCurrentUserId();

        List<History> histories = historiesService.getHistoriesByCarId(userId, carId);
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/history")
    public ResponseEntity<History> getHistoryByHistoryId(@RequestParam Long historyId) {
        Long userId = authenticationService.getCurrentUserId();

        History history = historiesService.getHistoryByHistoryId(userId, historyId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/add-history")
    public ResponseEntity<String> addHistory(@RequestParam Long carId, @RequestBody History history) {
        Long userId = authenticationService.getCurrentUserId();


        if (historiesService.addHistory(userId, carId, history))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>("History could not be added", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/modify-history")
    public ResponseEntity<String> modifyHistory(@RequestParam Long historyId, @RequestBody History history) {
        Long userId = authenticationService.getCurrentUserId();

        if (historiesService.modifyHistoryWithCarIdByHistoryId(userId, historyId, history))
            return new ResponseEntity<>("History modified", HttpStatus.OK);
        return new ResponseEntity<>("History not found or could not be modified", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-history")
    public ResponseEntity<String> deleteHistory(@RequestParam Long historyId) {
        Long userId = authenticationService.getCurrentUserId();

        if (historiesService.deleteHistoryByHistoryId(userId, historyId))
            return new ResponseEntity<>("History deleted", HttpStatus.OK);
        return new ResponseEntity<>("History not found or could not be deleted", HttpStatus.NOT_FOUND);
    }
}
