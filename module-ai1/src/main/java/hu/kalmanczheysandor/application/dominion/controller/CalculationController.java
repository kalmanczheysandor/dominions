package hu.kalmanczheysandor.application.dominion.controller;

import hu.kalmanczheysandor.application.dominion.service.CalculationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("ai1")
public class CalculationController {
    @GetMapping("/calculate")
    @ResponseStatus(HttpStatus.OK)
    public CalculationResponse calculate() {
        return new CalculationResponse(1,2,3);
    }
}
