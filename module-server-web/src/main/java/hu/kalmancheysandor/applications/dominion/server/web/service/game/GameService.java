package hu.kalmancheysandor.applications.dominion.server.web.service.game;

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

//    public User saveAsPlayer(User player) {
//        String encodedPassword = passwordEncoder.encode(player.getPassword());
//
//        UserRole role = new UserRole("ROLE_PLAYER", player);
//
//
//        player.setPassword(encodedPassword);
//        player.getAuthorities().add(role);
//
//        return userRepository.save(player);
//    }
//
//
//    public UserResponse saveUser(UserCreateRequest request) {
//        String encodedPassword = passwordEncoder.encode(request.getPassword());
//
//        // Set simple properties
//        User userToSave = new User();
//        userToSave.setIdentifier(request.getIdentifier());
//        userToSave.setPassword(encodedPassword);
//        userToSave.setName(request.getName());
//
//        // Add roles
//        Set<UserRole> authorities = new HashSet<>();
//        if (request.getRolePlayer()) {
//            authorities.add(new UserRole("ROLE_PLAYER", userToSave));
//        }
//        if (request.getRoleAdmin()) {
//            authorities.add(new UserRole("ROLE_ADMIN", userToSave));
//        }
//        if (request.getRoleEngineer()) {
//            authorities.add(new UserRole("ROLE_ENGINEER", userToSave));
//        }
//        userToSave.setAuthorities(authorities);
//
//        // Save
//        User userSaved = userRepository.save(userToSave);
//
//        return convertEntityToResponse(userSaved);
//    }
//
//
//
//    public UserResponse updateUser(long userId, UserUpdateRequest request) {
//        try {
//            // Request mapping
//            User newUser = convertRequestToEntity(request);
//
//            // Find the existing record
//            if(!userRepository.existsById(userId)) {
//                throw new UserNotFoundException(userId);
//            }
//            User existingRecord = userRepository.findById(userId);
//
//            // Creating and overwriting objects
//            modelMapper.map(newUser,existingRecord);
//            existingRecord.setId(userId);
//
//            // Providing persistence update
//            User modifiedRecord = userRepository.save(existingRecord);
//
//            return convertEntityToResponse(modifiedRecord);
//        } catch (EntityNotFoundException e) {
//            throw new UserNotFoundException(userId);
//        }
//    }


    public List<GameSessionItemResponse> listAllSession() {
        return gameServerProxy.listAll();
    }


//    public UserResponse findUserById(long userId) {
//        try {
//            User user = userRepository.findById(userId);
//            return convertEntityToResponse(user);
//        } catch (EntityNotFoundException e) {
//
//            throw new UserNotFoundException(userId);
//        }
//    }
//
//    public void deleteUserById(long userId) {
//        try {
//            userRoleRepository.deleteAllByUserId(userId);
//            userRepository.deleteById(userId);
//        } catch (EntityNotFoundException e) {
//            throw new UserNotFoundException(userId);
//        }
//    }
//
//    private UserResponse convertEntityToResponse(User user) {
//        UserResponse response = new UserResponse();
//
//        response.setId(user.getId());
//        response.setIdentifier(user.getIdentifier());
//        response.setName(user.getName());
//
//        for (UserRole role : user.getAuthorities()) {
//            if ("ROLE_PLAYER".equals(role.getAuthority())) {
//                response.setRolePlayer(true);
//            }
//            else if ("ROLE_ADMIN".equals(role.getAuthority())) {
//                response.setRoleAdmin(true);
//            }
//            else if ("ROLE_ENGINEER".equals(role.getAuthority())) {
//                response.setRoleEngineer(true);
//            }
//        }
//        return response;
//    }
//
//    private User convertRequestToEntity(UserUpdateRequest request) {
//
//        User user = new User();
//        user.setId(request.getId());
//        user.setIdentifier(request.getIdentifier());
//        user.setName(request.getName());
//
//        // Add roles
//        Set<UserRole> authorities = new HashSet<>();
//        if (request.getRolePlayer()) {
//            authorities.add(new UserRole("ROLE_PLAYER", user));
//        }
//        if (request.getRoleAdmin()) {
//            authorities.add(new UserRole("ROLE_ADMIN", user));
//        }
//        if (request.getRoleEngineer()) {
//            authorities.add(new UserRole("ROLE_ENGINEER", user));
//        }
//        user.setAuthorities(authorities);
//
//        return user;
//    }

}
