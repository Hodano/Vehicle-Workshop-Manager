package pl.hodan.carservice.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hodan.carservice.auth.services.AuthenticationService;
import pl.hodan.carservice.common.entity.PriceList;
import pl.hodan.carservice.common.service.PriceListService;

import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@RestController
@RequestMapping("/cars-service")
public class PriceListController {
    private final PriceListService priceListService;
    private final AuthenticationService authenticationService;

    public PriceListController(PriceListService priceListService, AuthenticationService authenticationService) {
        this.priceListService = priceListService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/priceLists")
    public ResponseEntity<List<PriceList>> getPriceListsByUserId() {
        Long userId = authenticationService.getCurrentUserId();

        List<PriceList> priceLists = priceListService.getPriceListsByUserId(userId);
        return ResponseEntity.ok(priceLists);
    }

    @GetMapping("/priceList")
    public ResponseEntity<PriceList> getPriceListById(@RequestParam Long priceListId) {
        Long userId = authenticationService.getCurrentUserId();

        PriceList priceList = priceListService.getPriceListById(userId, priceListId);

        return ResponseEntity.ok(priceList);
    }

    @PostMapping("/add-priceList")
    public ResponseEntity<String> addPriceList(@RequestBody PriceList priceList) {
        Long userId = authenticationService.getCurrentUserId();

        if (priceListService.addPriceList(userId, priceList))
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>("Price list could not be added", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/modify-priceList")
    public ResponseEntity<String> modifyPriceList(@RequestParam Long priceListId, @RequestBody PriceList priceList) {
        Long userId = authenticationService.getCurrentUserId();

        if (priceListService.modifyPriceListWithUserIdByPriceListId(userId, priceListId, priceList))
            return new ResponseEntity<>("Price list modified", HttpStatus.ACCEPTED);
        return new ResponseEntity<>("Price list not found or could not be modified", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-priceList")
    public ResponseEntity<String> deletePriceList(@RequestParam Long priceListId) {
        Long userId = authenticationService.getCurrentUserId();

        if (priceListService.deletePriceListWithUserIdByPriceListId(userId, priceListId))
            return new ResponseEntity<>("Price list deleted", HttpStatus.OK);
        return new ResponseEntity<>("Price list not found or could not be deleted", HttpStatus.NOT_FOUND);
    }
}
