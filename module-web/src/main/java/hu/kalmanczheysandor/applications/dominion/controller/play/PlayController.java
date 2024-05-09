package hu.kalmanczheysandor.applications.dominion.controller.play;

import hu.kalmanczheysandor.applications.dominion.configuration.mvc.AllowStandardRequest;
import hu.kalmanczheysandor.applications.dominion.configuration.mvc.BlockAllRequestByDefault;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/play/{key}")
@BlockAllRequestByDefault
public class PlayController {
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    @AllowStandardRequest
    public String showPage(@PathVariable String key) {
        return "play/PlayPage";
    }

}
