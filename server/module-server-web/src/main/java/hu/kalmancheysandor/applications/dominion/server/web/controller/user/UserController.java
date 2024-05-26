package hu.kalmancheysandor.applications.dominion.server.web.controller.user;

import hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc.AllowAjaxRequest;
import hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc.AllowStandardRequest;
import hu.kalmancheysandor.applications.dominion.server.web.controller.user.data.UserCreateFormData;
import hu.kalmancheysandor.applications.dominion.server.web.controller.user.data.UserData;
import hu.kalmancheysandor.applications.dominion.server.web.controller.user.data.UserUpdateFormData;
import hu.kalmancheysandor.applications.dominion.server.web.service.user.UserCreateRequest;
import hu.kalmancheysandor.applications.dominion.server.web.service.user.UserResponse;
import hu.kalmancheysandor.applications.dominion.server.web.service.user.UserService;
import hu.kalmancheysandor.applications.dominion.server.web.service.user.UserUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
//@BlockAllRequestByDefault
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    @AllowStandardRequest
    public String showPage() {
        return "user/UserListPage";
    }

    @GetMapping("/add")
    @AllowAjaxRequest
    public String showAddDialog(ModelMap model) {
        model.put("user", new UserCreateFormData());
        return "user/UserAddDialog::dialog";
    }

    @GetMapping("/edit/{id}")
    @AllowAjaxRequest
    public String showEditDialog(@PathVariable("id") long userId, ModelMap model) {
        UserResponse response = userService.findUserById(userId);

        UserCreateFormData formData = convertResponseToFormData(response);
        model.put("user", formData);

        return "user/UserEditDialog::dialog";
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////// DATA METHODS //////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/data/list")
    @ResponseBody
    @AllowAjaxRequest
    public List<UserData> listAllUser() {
        List<UserResponse> response = userService.listAllUser();
        return response.stream()
                       .map(item -> convertResponseToData(item))
                       .collect(Collectors.toList());
    }

    @PostMapping("/data/add")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @AllowAjaxRequest
    public UserData addUser(UserCreateFormData formData) {
        UserResponse response = userService.saveUser(convertFormDataToRequest(formData));
        return convertResponseToData(response);
    }

    @PostMapping("/data/edit/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @AllowAjaxRequest
    public UserData updateUser(@PathVariable("id") long userId, UserUpdateFormData formData) {
        UserResponse response = userService.updateUser(userId, convertFormDataToRequest(formData));
        return convertResponseToData(response);
    }

    @DeleteMapping("/data/delete/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @AllowAjaxRequest
    public void deleteUser(@PathVariable("id") long userId) {
        userService.deleteUserById(userId);
    }

    private UserCreateRequest convertFormDataToRequest(UserCreateFormData formData) {
        return modelMapper.map(formData, UserCreateRequest.class);
    }

    private UserUpdateRequest convertFormDataToRequest(UserUpdateFormData formData) {
        return modelMapper.map(formData, UserUpdateRequest.class);
    }

    private UserData convertResponseToData(UserResponse response) {
        return modelMapper.map(response, UserData.class);
    }

    private UserCreateFormData convertResponseToFormData(UserResponse response) {
        return modelMapper.map(response, UserCreateFormData.class);
    }
}
