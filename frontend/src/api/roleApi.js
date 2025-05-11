import { ROLES_ENDPOINT } from "../constants/api";

export const fetchRoles = async (token) => {
    const response = await fetch(ROLES_ENDPOINT, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`
        },
    });

    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};