package com.phucx.phucxfoodshop.service.employee.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.NotificationStatus;
import com.phucx.phucxfoodshop.constant.NotificationTitle;
import com.phucx.phucxfoodshop.constant.NotificationTopic;
import com.phucx.phucxfoodshop.constant.UserSearch;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.model.dto.EmployeeAdminDetails;
import com.phucx.phucxfoodshop.model.dto.UserNotificationDTO;
import com.phucx.phucxfoodshop.model.entity.EmployeeDetail;
import com.phucx.phucxfoodshop.model.entity.User;
import com.phucx.phucxfoodshop.model.entity.UserDetails;
import com.phucx.phucxfoodshop.repository.EmployeeDetailRepostiory;
import com.phucx.phucxfoodshop.service.employee.EmployeeAdminService;
import com.phucx.phucxfoodshop.service.image.EmployeeImageService;
import com.phucx.phucxfoodshop.service.notification.SendUserNotificationService;
import com.phucx.phucxfoodshop.service.user.UserService;
import com.phucx.phucxfoodshop.utils.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeAdminServiceImp implements EmployeeAdminService {
    @Autowired
    private EmployeeDetailRepostiory employeeDetailRepostiory;
    @Autowired
    private EmployeeImageService employeeImageService;
    @Autowired
    private SendUserNotificationService sendUserNotificationService;
    @Autowired
    private UserService userService;

    private final String EMPLOYEE = "EMPLOYEE";

    @Override
    public EmployeeAdminDetails updateAdminEmployeeInfo(EmployeeAdminDetails employee) {
        log.info("updateAdminEmployeeInfo({})", employee.toString());
        EmployeeDetail fetchedEmployee = employeeDetailRepostiory.findById(employee.getEmployeeID())
                .orElseThrow(
                        () -> new EmployeeNotFoundException(
                                "Employee " + employee.getEmployeeID() + " does not found"));
        // get image
        String picture = ImageUtils.getImageName(employee.getPicture());
        // update employee's information
        Boolean status = employeeDetailRepostiory.updateAdminEmployeeInfo(
                fetchedEmployee.getEmployeeID(), employee.getHireDate(),
                employee.getBirthDate(), employee.getAddress(), employee.getCity(),
                employee.getDistrict(), employee.getWard(), employee.getPhone(),
                employee.getTitle(), picture, employee.getNotes(), employee.getEnabled());
        // set employee picture
        employee.setPicture(picture);
        String employeepicture = picture != null ? employeeImageService.getImageUrl(picture) : null;
        employee.setPicture(employeepicture);
        // create a notification
        UserNotificationDTO notification = new UserNotificationDTO();
        notification.setTitle(NotificationTitle.USER_INFO_UPDATE);
        notification.setTopic(NotificationTopic.Account);
        notification.setUserID(fetchedEmployee.getUserID());
        notification.setPicture(employee.getPicture());
        if (status) {
            notification.setMessage("Your information has been updated by Admin");
            notification.setStatus(NotificationStatus.SUCCESSFUL);
            notification.setReceiverID(fetchedEmployee.getUserID());
        } else {
            notification.setMessage("Error: Your information can not be updated by Admin");
            notification.setStatus(NotificationStatus.ERROR);
            notification.setReceiverID(fetchedEmployee.getUserID());
        }
        // send notification
        this.sendUserNotificationService.sendEmployeeNotification(notification);
        if (!status)
            throw new RuntimeException("Employee " + employee.getEmployeeID() + " can not be updated!");

        return employee;
    }

    @Override
    public EmployeeAdminDetails getEmployeeAdminDetails(String userID) {
        log.info("getEmployeeAdminDetails(userID={})", userID);
        User user = userService.getUserById(userID);
        EmployeeDetail employeeDetail = employeeDetailRepostiory.findByUserID(userID).orElseThrow(
                () -> new EmployeeNotFoundException("Employee with userId " + userID + " does not found!"));
        String picture = employeeDetail.getPicture() != null
                ? employeeImageService.getImageUrl(employeeDetail.getPicture())
                : null;
        return EmployeeAdminDetails.builder()
                .employeeID(employeeDetail.getEmployeeID())
                .birthDate(employeeDetail.getBirthDate())
                .hireDate(employeeDetail.getHireDate())
                .phone(employeeDetail.getPhone())
                .picture(picture)
                .title(employeeDetail.getTitle())
                .address(employeeDetail.getAddress())
                .city(employeeDetail.getCity())
                .district(employeeDetail.getDistrict())
                .ward(employeeDetail.getWard())
                .notes(employeeDetail.getNotes())
                .userID(user.getUserID())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .emailVerified(user.getEmailVerified())
                .build();
    }

    @Override
    public Page<UserDetails> getByUsernameLike(String username, Integer pageNumber) {
        log.info("getByUsernameLike(username={}, pageNumber={})", username, pageNumber);
        Page<UserDetails> users = userService
                .getUsersByRoleAndUsernameLike(EMPLOYEE, username, pageNumber);
        users.stream().forEach(user -> {
            String picture = user.getPicture() != null ? employeeImageService.getImageUrl(user.getPicture()) : null;
            user.setPicture(picture);
        });
        return users;
    }

    @Override
    public Page<UserDetails> getByFirstnameLike(String firstname, Integer pageNumber) {
        log.info("getByFirstnameLike(firstname={}, pageNumber={})", firstname, pageNumber);
        Page<UserDetails> users = userService
                .getUsersByRoleAndFirstNameLike(EMPLOYEE, firstname, pageNumber);
        users.stream().forEach(user -> {
            String picture = user.getPicture() != null ? employeeImageService.getImageUrl(user.getPicture()) : null;
            user.setPicture(picture);
        });
        return users;
    }

    @Override
    public Page<UserDetails> getByLastnameLike(String lastname, Integer pageNumber) {
        log.info("getByLastnameLike(lastname={}, pageNumber={})", lastname, pageNumber);
        Page<UserDetails> users = userService.getUsersByRoleAndLastNameLike(
                EMPLOYEE, lastname, pageNumber);
        users.stream().forEach(user -> {
            String picture = user.getPicture() != null ? employeeImageService.getImageUrl(user.getPicture()) : null;
            user.setPicture(picture);
        });
        return users;
    }

    @Override
    public Page<UserDetails> getByEmailLike(String email, Integer pageNumber) {
        log.info("getByEmailLike(email={}, pageNumber={})", email, pageNumber);
        Page<UserDetails> users = userService.getUsersByRoleAndEmailLike(
                EMPLOYEE, email, pageNumber);
        users.stream().forEach(user -> {
            String picture = user.getPicture() != null ? employeeImageService.getImageUrl(user.getPicture()) : null;
            user.setPicture(picture);
        });
        return users;
    }

    @Override
    public Page<UserDetails> getUsers(UserSearch searchParam, String searchValue, Integer pageNumber) {
        log.info("getUsers(searchParam={}, searchValue={}, pageNumber={})", searchParam, searchValue, pageNumber);
        switch (searchParam) {
            case USERNAME:
                return this.getByUsernameLike(searchValue, pageNumber);
            case EMAIL:
                return this.getByEmailLike(searchValue, pageNumber);
            case FIRSTNAME:
                return this.getByFirstnameLike(searchValue, pageNumber);
            case LASTNAME:
                return this.getByLastnameLike(searchValue, pageNumber);

            default:
                return null;
        }
    }
}
