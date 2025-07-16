import useSWR from "swr";
import {PRODUCTS_ENDPOINT} from "../../../shared/constants/api";
import {fetchProducts} from "../services/productApi";
import {useSelector} from "react-redux";
import {useMemo} from "react";

export default function useProducts(filters = {}) {
    const reduxFilters = useSelector((state) => state.products.filters);
    const finalFilters = useMemo(() => ({ ...reduxFilters, ...filters}), [filters, reduxFilters]);

    const { data, error, isLoading, mutate } =
        useSWR([PRODUCTS_ENDPOINT, JSON.stringify(finalFilters)], () => fetchProducts(finalFilters),
        {
            keepPreviousData: true,
            revalidateOnFocus: true,
            revalidateOnReconnect: true,
        });
    return {
        products: data?.content || [],
        totalPages: data?.totalPages || 0,
        error,
        isLoading,
        mutate
    }
}