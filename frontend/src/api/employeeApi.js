import { EMPLOYEE_PROFILE_ENDPOINT } from "../constants/api";

// --- Fetching Employee Information ---
export const fetchEmployeeProfile = async (token) => {
  const response = await fetch(EMPLOYEE_PROFILE_ENDPOINT, {
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

// --- Updating Employee Data ---
export const updateEmployeeProfile = async (token, requestEmployeeDTO) => {
  const response = await fetch(EMPLOYEE_PROFILE_ENDPOINT, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(requestEmployeeDTO),
  });

  if (!response.ok) {
    throw await response.json();
  }

  return response.json();
};