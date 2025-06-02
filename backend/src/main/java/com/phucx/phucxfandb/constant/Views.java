package com.phucx.phucxfandb.constant;

public class Views {
    private Views(){}

    public interface Default {}
    public interface ValidateToken extends Default{}
    public interface ResetPassword extends Default{}
    public interface ForgetPassword extends Default{}

    public interface UpdateUserEnabledStatus extends Default{}

    public interface CreateTableOccupancy extends Default{}
    public interface UpdateTableOccupancy extends Default{}
    public interface UpdateTableOccupancyStatus extends Default{}

    public interface UpdateTableStatus extends Default{}

    public interface UpdateOrderItemStatus extends Default{}
}
