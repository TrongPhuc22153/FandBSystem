import useSWR from "swr";
import { useAuth } from "../shared/context/AuthContext";
import { ROLES_ENDPOINT } from "../shared/constants/api";
import { fetchRoles } from "../api/roleApi";

export const useRoles = () => {
    const { token } = useAuth();
    return useSWR(ROLES_ENDPOINT, () => fetchRoles(token));
};