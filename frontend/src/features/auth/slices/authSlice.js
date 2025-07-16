import {createSlice} from "@reduxjs/toolkit";
import Cookies from "js-cookie";
import {forgotPassword, loginUser, logoutUser, registerUser, resetPassword, validateToken} from "../thunks/authThunk";

const initialState = {
    user: null,
    token: Cookies.get("access_token") || null,
    isResetPassword: Cookies.get("reset_password_required") === "true",
    login: {
        isLoading: false,
        success: null,
        error: null,
    },
    logout: {
        isLoading: false,
        success: null,
        error: null,
    },
    resetPassword: {
        isLoading: false,
        success: null,
        error: null,
    },
    register: {
        isLoading: false,
        success: null,
        error: null,
    },
    forgotPassword: {
        isLoading: false,
        success: null,
        error: null,
    },
    validateToken: {
        isLoading: false,
        success: null,
        error: null,
    },
}

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        resetLoginState: (state) => {
            state.login.error = null;
            state.login.success = null;
        },
        resetLogoutState: (state) => {
            state.logoutError = null;
            state.logoutSuccess = null;
        },
        resetResetPasswordState: (state) => {
            state.resetPassword.error = null;
            state.resetPassword.success = null;
        },
        resetRegisterState: (state) => {
            state.register.error = null;
            state.register.success = null;
        },
        resetForgotPasswordState: (state) => {
            state.forgotPassword.error = null;
            state.forgotPassword.success = null;
        },
        resetValidateTokenState: (state) => {
            state.validateToken.error = null;
            state.validateToken.success = null;
        },
        setUser: (state, action) => {
            state.user = action.payload;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(loginUser.pending, (state) => {
                state.login.isLoading = true;
                state.login.error = null;
                state.login.success = null;
            })
            .addCase(loginUser.fulfilled, (state, action) => {
                state.login.isLoading = false;
                state.login.success = action.payload.message;
                state.token = action.payload.token;
                state.isResetPassword = false;
            })
            .addCase(loginUser.rejected, (state, action) => {
                state.login.isLoading = false;
                state.login.error = action.payload;
                state.isResetPassword = Cookies.get("reset_password_required") === "true" || false;
            })

            .addCase(logoutUser.pending, (state) => {
                state.logoutLoading = true;
                state.logoutError = null;
                state.logoutSuccess = null;
            })
            .addCase(logoutUser.fulfilled, (state) => {
                state.logoutLoading = false;
                state.logoutSuccess = "Logout successful";
                state.token = null;
                state.isResetPassword = false;
            })
            .addCase(logoutUser.rejected, (state, action) => {
                state.logoutLoading = false;
                state.logoutError = action.payload;
            })

            .addCase(resetPassword.pending, (state) => {
                state.resetPassword.isLoading = true;
                state.resetPassword.error = null;
                state.resetPassword.success = null;
            })
            .addCase(resetPassword.fulfilled, (state, action) => {
                state.resetPassword.isLoading = false;
                state.resetPassword.success = action.payload.message;
                state.token = action.payload.token;
                state.isResetPassword = false;
            })
            .addCase(resetPassword.rejected, (state, action) => {
                state.resetPassword.isLoading = false;
                state.resetPassword.error = action.payload;
                state.isResetPassword = false;
            })

            .addCase(registerUser.pending, (state) => {
                state.register.isLoading = true;
                state.register.error = null;
                state.register.success = null;
            })
            .addCase(registerUser.fulfilled, (state, action) => {
                state.register.isLoading = false;
                state.register.success = action.payload.message;
            })
            .addCase(registerUser.rejected, (state, action) => {
                state.register.isLoading = false;
                state.register.error = action.payload;
            })

            .addCase(forgotPassword.pending, (state) => {
                state.forgotPassword.isLoading = true;
                state.forgotPassword.error = null;
                state.forgotPassword.success = null;
            })
            .addCase(forgotPassword.fulfilled, (state, action) => {
                state.forgotPassword.isLoading = false;
                state.forgotPassword.success = action.payload.message;
            })
            .addCase(forgotPassword.rejected, (state, action) => {
                state.forgotPassword.isLoading = false;
                state.forgotPassword.error = action.payload;
            })

            .addCase(validateToken.pending, (state) => {
                state.validateToken.isLoading = true;
                state.validateToken.error = null;
                state.validateToken.success = null;
            })
            .addCase(validateToken.fulfilled, (state, action) => {
                state.validateToken.isLoading = false;
                state.validateToken.success = action.payload.message;
            })
            .addCase(validateToken.rejected, (state, action) => {
                state.validateToken.isLoading = false;
                state.validateToken.error = action.payload;
            })

    }
})

export const {
    resetLoginState,
    resetLogoutState,
    resetResetPasswordState,
    resetForgotPasswordState,
    resetRegisterState,
    resetValidateTokenState,
    setUser,
} = authSlice.actions;
export default authSlice.reducer;