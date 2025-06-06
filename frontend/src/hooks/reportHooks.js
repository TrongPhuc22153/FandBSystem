import useSWR from "swr";
import { fetchMetrics, fetchReport } from "../api/reportApi";
import { REPORTS_ENDPOINT, REPORTS_METRICS_ENDPOINT } from "../constants/api";
import { useAuth } from "../context/AuthContext";

export const useReport = ({ startDate, endDate }) => {
    const { token } = useAuth();

    const swrKey = token ? [REPORTS_ENDPOINT, startDate, endDate] : null;

    return useSWR(swrKey, () => fetchReport(token, startDate, endDate));
};

export const useMetrics = ({
    startDate,
    endDate
}) => {
    const { token } = useAuth();

    const swrKey = token ? [REPORTS_METRICS_ENDPOINT, token, startDate, endDate] : null;

    return useSWR(swrKey, () => fetchMetrics(token, startDate, endDate), {
        revalidateOnFocus: false,
        revalidateOnReconnect: false,
        refreshInterval: 0,
    });
};