import { TABLE_OCCUPANCIES_ENDPOINT } from "../constants/api";
import { SORTING_DIRECTIONS } from "../constants/webConstant";

export const fetchTableOccupancies = async ({ page = 0, size = 10, direction = SORTING_DIRECTIONS.ASC, field = "createdAt", status, token }) => {
    const queryParams = new URLSearchParams({
        ...(status && { status }),
        page,
        size,
        direction,
        field
    }).toString();

    const url = `${TABLE_OCCUPANCIES_ENDPOINT}${queryParams ? `?${queryParams}` : ''}`;

    const response = await fetch(url, {
        method: 'GET',
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

export const fetchTableOccupancy = async ({ id, token }) => {
    const response = await fetch(`${TABLE_OCCUPANCIES_ENDPOINT}/${id}`, {
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

export const updateTableOccupancy = async ({ id, data, token }) => {
    const response = await fetch(`${TABLE_OCCUPANCIES_ENDPOINT}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
    });

    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

export const createTableOccupanncy = async ({ contactName, phone, partySize, notes, reservationId, type, token }) => {
    const response = await fetch(TABLE_OCCUPANCIES_ENDPOINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
            contactName,
            phone,
            partySize,
            notes,
            reservationId,
            type
        }),
    });

    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

export const updateTableOccupancyStatus = async ({ id, status, tableId, token }) => {
    const response = await fetch(`${TABLE_OCCUPANCIES_ENDPOINT}/${id}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
            status,
            tableId
        }),
    });

    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};