import useSWR from "swr";
import {PRODUCTS_ENDPOINT} from "../../../shared/constants/api";
import {fetchProduct} from "../services/productApi";

export default function useProduct({ productId, isDeleted = false }) {
    const { data, error, isLoading, mutate } =
        useSWR([PRODUCTS_ENDPOINT, productId, isDeleted], () =>
            fetchProduct({ productId, isDeleted })
    );

    return {
        product: data,
        error,
        isLoading,
        mutate
    }
}