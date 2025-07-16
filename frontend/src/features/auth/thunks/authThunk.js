import {
    login,
    register,
    logout,
    resetPassword as resetPasswordAPI,
    forgotPassword as forgotPasswordAPI,
    validateResetToken
} from "../services/authenticatoinApi";
import {createAsyncThunk} from "@reduxjs/toolkit";
import Cookies from "js-cookie";
import {fetchUser as fetchUserAPI  } from "../../users/services/usersApi";
import {PASSWORD_RESET_REQUIRED_ERROR} from "../../../shared/constants/error";

export const loginUser = createAsyncThunk('auth/login', async ({ username, password }, { rejectWithValue }) => {
    try {
        const { data, message } = await login({ username, password });
        Cookies.set("access_token", data.accessToken, {
            expires: 7,
            sameSite: "Strict",
        });

        return { token: data.accessToken, message }
    } catch (error) {
        if (error?.error === PASSWORD_RESET_REQUIRED_ERROR) {
            Cookies.set("reset_password_required", "true", {
                expires: 7,
                sameSite: "Strict",
                secure: true,
            });
        }
        return rejectWithValue(error);
    }
});

export const logoutUser = createAsyncThunk('auth/logout', async (_, { getState }) => {
    const token = getState().auth.token;
    if (token) {
        await logout({ token });
    }
    Cookies.remove("access_token");
    Cookies.remove("reset_password_required");
});

export const resetPassword = createAsyncThunk('auth/resetPassword', async (newPassword, { getState, rejectWithValue }) => {
    const token = getState().auth.token;
    try {
        const { message, data } = await resetPasswordAPI({ token, newPassword });
        Cookies.set("access_token", data.accessToken, {
            expires: 7,
            sameSite: "Strict",
        });

        const userData = await fetchUserAPI({ token: data.accessToken });

        Cookies.remove("reset_password_required");
        return { token: data.accessToken, message, user: userData };
    } catch (error) {
        return rejectWithValue(error);
    }
})

export const forgotPassword = createAsyncThunk('auth/forgotPassword', async (email, {rejectWithValue }) => {
    try {
        const { message, data } = await forgotPasswordAPI({ email });
        return { message, data };
    } catch (error) {
        return rejectWithValue(error);
    }
})

export const validateToken = createAsyncThunk('auth/validateToken', async (token, {rejectWithValue }) => {
    try {
        const { message, data } = await validateResetToken({ token });
        return { message, data };
    } catch (error) {
        return rejectWithValue(error);
    }
})

export const registerUser = createAsyncThunk('auth/register', async ({ username, email, password, firstName, lastName }, { rejectWithValue }) => {
    try {
        const { data, message } = await register({
            username,
            email,
            password,
            firstName,
            lastName
        })

        return { data, message };
    } catch (error) {
        return rejectWithValue(error);
    }
})