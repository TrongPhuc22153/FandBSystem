import useSWR from "swr";
import {CATEGORIES_ENDPOINT} from "../../../shared/constants/api";
import {fetchCategories} from "../services/categoryApi";
import {useSelector} from "react-redux";

export default function useCategories(filters = {}) {
    const reduxFilters = useSelector((state) => state.categories.filters);
    const finalFilters = { ...reduxFilters, ...filters}

    const { data, error, isLoading, mutate } =
        useSWR([CATEGORIES_ENDPOINT, JSON.stringify(finalFilters)], () => fetchCategories(finalFilters),
            {
                keepPreviousData: true,
                revalidateOnFocus: true,
                revalidateOnReconnect: true,
            })

    return {
        categories: data?.content || [],
        totalPages: data?.totalPages || 0,
        error,
        isLoading,
        mutate,
    }
}