import {
  FORGOT_PASSWORD_ENDPOINT,
  LOGIN_ENDPOINT,
  LOGOUT_ENDPOINT,
  REGISTER_ENDPOINT,
  RESET_PASSWORD_ENDPOINT,
  VALIDATE_TOKEN_ENDPOINT,
} from "../constants/api";

export const login = async ({ username, password }) => {
  const response = await fetch(LOGIN_ENDPOINT, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

export const register = async ({
  username,
  password,
  email,
  firstName,
  lastName,
}) => {
  const response = await fetch(REGISTER_ENDPOINT, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      username: username,
      password: password,
      email: email,
      firstName: firstName,
      lastName: lastName,
    }),
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

export const logout = async ({ token }) => {
  const response = await fetch(LOGOUT_ENDPOINT, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};

export const forgotPassword = async ({ email }) => {
  const response = await fetch(FORGOT_PASSWORD_ENDPOINT, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      email: email,
    }),
  });

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  return response.json();
};

export const validateResetToken = async ({ token }) => {
  const response = await fetch(VALIDATE_TOKEN_ENDPOINT, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ token }),
  });

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  return response.json();
};

export const resetPassword = async ({ token, newPassword }) => {
  const response = await fetch(RESET_PASSWORD_ENDPOINT, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      token,
      newPassword,
    }),
  });

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  return response.json();
};
