import { LOGIN_ENDPOINT, LOGOUT_ENDPOINT, REGISTER_ENDPOINT } from "../constants/api";

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
