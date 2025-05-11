import { USER_PROFILE_ENDPOINT, USER_PROFILES_ENDPOINT } from "../constants/api";

export const fetchUserProfile = async ({ userId, token }) => {
  const response = await fetch(`${USER_PROFILES_ENDPOINT}/${userId}`, {
    method: "GET",
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

export const fetchAuthenticatedUserProfile = async ({ token }) => {
  const response = await fetch(USER_PROFILE_ENDPOINT, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};

export const updateUserProfile = async (token, requestUserProfileDTO) => {
  const response = await fetch(USER_PROFILE_ENDPOINT, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(requestUserProfileDTO),
  });

  if (!response.ok) {
    throw await response.json();
  }
  return response.json();
};