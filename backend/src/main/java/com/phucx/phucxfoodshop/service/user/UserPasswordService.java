package com.phucx.phucxfoodshop.service.user;

import com.phucx.phucxfoodshop.exceptions.InvalidTokenException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserPasswordException;
import com.phucx.phucxfoodshop.model.UserChangePassword;
import com.phucx.phucxfoodshop.model.UserChangePasswordToken;
import com.phucx.phucxfoodshop.model.UserInfo;

public interface UserPasswordService {
    public Boolean resetUserPasswordRandom(String userID);
    public Boolean sendResetPasswordLink(String baseUrl, String email);
    public Boolean changePassword(UserChangePassword userChangePassword) throws UserPasswordException;
    public Boolean resetUserPassword(UserChangePasswordToken userChangePasswordToken) throws UserPasswordException, InvalidTokenException;
    public UserInfo getUserByResetToken(String token) throws NotFoundException, InvalidTokenException;
}
