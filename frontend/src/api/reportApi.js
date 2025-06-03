import { REPORTS_ENDPOINT, REPORTS_METRICS_ENDPOINT } from "../constants/api";

export const fetchReport = async (token, startDate, endDate) => {
    const params = new URLSearchParams();
    params.append("startDate", startDate);
    params.append("endDate", endDate);

    const response = await fetch(`${REPORTS_ENDPOINT}?${params.toString()}`, {
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

export const fetchMetrics = async (token, startDate, endDate) => {
    const params = new URLSearchParams();
    params.append("startDate", startDate);
    params.append("endDate", endDate);

    const response = await fetch(`${REPORTS_METRICS_ENDPOINT}?${params.toString()}`, {
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


