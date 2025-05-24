package com.phucx.phucxfandb.constant;

public class ValidationGroups {
    private ValidationGroups(){}

    public interface Default {}
    public interface ValidateToken extends Default {}
    public interface ResetPassword extends Default {}
    public interface ForgetPassword extends Default {}

    public interface UpdateUserEnabledStatus extends Default {}

    public interface OrderPayment extends Default {}
    public interface ReservationPayment extends Default {}

}
