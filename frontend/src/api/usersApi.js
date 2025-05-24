import { USER_ENDPOINT, USER_PASSWORD_ENDPOINT, USERS_ENDPOINT } from "../constants/api";

export const fetchUser = async ({ token }) => {
  const response = await fetch(USER_ENDPOINT, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw await response.json()
  }

  return response.json();
};

export const fetchUsers = async ({ username, email, role, page = 0, size = 10, search, token }) => {
  const queryParams = new URLSearchParams({
    ...(username && { username }),
    ...(email && { email }),
    ...(role && { role }),
    ...(search && { search }),
    page,
    size,
  }).toString();
  const url = USERS_ENDPOINT + (queryParams ? `?${queryParams}` : '');

  const response = await fetch(url, {
    method: 'GET',
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw await response.json()
  }

  return response.json();
};

export const createUser = async ({ requestUserDTO, token }) => {
  const response = await fetch(USERS_ENDPOINT, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(requestUserDTO),
  });

  if (!response.ok) {
    throw await response.json()
  }
  return response.json();
};

export const deleteUser = async ({ id, enabled, token }) => {
  const response = await fetch(`${USERS_ENDPOINT}/${id}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      enabled
    })
  });

  if (!response.ok) {
    throw await response.json()
  }
  return response.json();
};

export const changePassword = async ({
  userId,
  oldPassword,
  newPassword,
  token,
}) => {
  const response = await fetch(USER_PASSWORD_ENDPOINT, {
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