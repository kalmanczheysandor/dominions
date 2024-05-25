package hu.kalmancheysandor.applications.dominion.server.web.service.game;

import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameCreateResponse;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameSessionItemResponse;
import hu.kalmancheysandor.applications.dominion.server.web.entity.User;
import hu.kalmancheysandor.applications.dominion.server.web.entity.UserRole;
import hu.kalmancheysandor.applications.dominion.server.web.exception.UserNotFoundException;
import hu.kalmancheysandor.applications.dominion.server.web.proxy.game.GameServerProxy;
import hu.kalmancheysandor.applications.dominion.server.web.service.user.UserResponse;
import hu.kalmancheysandor.applications.dominion.server.web.service.user.UserUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GameService {

//    @Autowired
//    private UserRepository gameRepository;
//

    @Autowired
    private GameServerProxy gameServerProxy;

    @Autowired
    private ModelMapper modelMapper;


    public GameCreateResponse create() {
        return gameServerProxy.create();
    }


    public List<GameSessionItemResponse> listAllSession() {
        return gameServerProxy.listAll();
    }


}
