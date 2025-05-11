import { RESERVATION_TABLES_ENDPOINT } from "../constants/api";
import { SORTING_DIRECTIONS } from "../constants/webConstant";

// Get all reservation tables with pagination
export const fetchReservationTables = async ({ page = 0, size = 10, direction = SORTING_DIRECTIONS.ASC, field = "tableNumber", isDeleted = false, status, tableNumber }) => {
    const params = new URLSearchParams();
    params.append("page", page.toString())
    params.append("size", size.toString())
    params.append("direction", direction.toString())
    params.append("field", field.toString())
    if (isDeleted != null && isDeleted != undefined) {
        params.append("isDeleted", isDeleted.toString())
    }
    if (status != null && status != undefined) {
        params.append("status", status.toString())
    }
    if (tableNumber != null && tableNumber != undefined) {
        params.append("tableNumber", tableNumber.toString())
    }
    const response = await fetch(
        `${RESERVATION_TABLES_ENDPOINT}?${params.toString()}`,);
    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

// Get reservation table by ID
export const fetchReservationTableById = async ({ id }) => {
    const response = await fetch(`${RESERVATION_TABLES_ENDPOINT}/table?id=${id}`);
    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

// Get reservation table by name
export const fetchReservationTableByName = async ({ name }) => {
    const response = await fetch(`${RESERVATION_TABLES_ENDPOINT}/table?name=${name}`);
    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

// Update reservation table by ID
export const updateReservationTable = async ({ id, tableData, token }) => {
    const response = await fetch(`${RESERVATION_TABLES_ENDPOINT}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(tableData),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

// Update table status
export const updateReservationTableStatus = async ({ id, status, isDeleted, token }) => {
    const requestBody = {};
    if (status !== undefined && status !== null) {
        requestBody.status = status;
    }
    if (isDeleted !== undefined && isDeleted !== null) {
        requestBody.isDeleted = isDeleted;
    }

    // Ensure at least one of status or isDeleted is being updated
    if (Object.keys(requestBody).length === 0) {
        throw { message: "Missing Status or IsDeleted"}
    }

    const response = await fetch(`${RESERVATION_TABLES_ENDPOINT}/${id}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(requestBody)
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

// Create new reservation table
export const createReservationTable = async ({ tableData, token }) => {
    const response = await fetch(RESERVATION_TABLES_ENDPOINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(tableData),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

// Create multiple reservation tables
export const createReservationTablesBulk = async ({ tablesData, token }) => {
    const response = await fetch(`${RESERVATION_TABLES_ENDPOINT}/bulk`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(tablesData),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};

// Delete reservation table by ID
export const deleteReservationTable = async ({ tableId, token }) => {
    const response = await fetch(`${RESERVATION_TABLES_ENDPOINT}/${tableId}`, {
        method: 'DELETE',
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};
