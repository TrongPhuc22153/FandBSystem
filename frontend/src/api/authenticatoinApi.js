import { LOGIN_ENDPOINT, LOGOUT_ENDPOINT, PASSWORD_ENDPOINT, REGISTER_ENDPOINT } from "../constants/api";

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

export const changePassword = async ({ userId, oldPassword, newPassword, token }) => {
  const response = await fetch(PASSWORD_ENDPOINT, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      userId: userId,
      password: oldPassword,
      newPassword: newPassword,
    }),
  });

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  return response.json();
};

export const register = async ({ username, password, email, firstName, lastName }) => {
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
      lastName: lastName
    })
  })

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
}

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
