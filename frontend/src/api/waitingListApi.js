import { WAITLISTS_ENDPOINT } from "../constants/api";
import { SORTING_DIRECTIONS } from "../constants/webConstant";

export const fetchWaitLists = async ({ page = 0, size = 10, direction = SORTING_DIRECTIONS.ASC, field = "createdAt", status, token }) => {
    const queryParams = new URLSearchParams({
        ...(status && { status }),
        page,
        size,
        direction,
        field
    }).toString();

    const url = `${WAITLISTS_ENDPOINT}${queryParams ? `?${queryParams}` : ''}`;

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

export const fetchWaitListById = async ({ id, token }) => {
    const response = await fetch(`${WAITLISTS_ENDPOINT}/${id}`, {
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

export const updateWaitList = async ({ id, requestWaitListDTO, token }) => {
    const response = await fetch(`${WAITLISTS_ENDPOINT}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(requestWaitListDTO),
    });

    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

export const createWaitList = async ({ contactName, phone, partySize, notes, token }) => {
    const response = await fetch(WAITLISTS_ENDPOINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
            contactName,
            phone,
            partySize,
            notes
        }),
    });

    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

export const updateWaitListStatus = async ({ id, status, tableId, token }) => {
    const response = await fetch(`${WAITLISTS_ENDPOINT}/${id}`, {
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