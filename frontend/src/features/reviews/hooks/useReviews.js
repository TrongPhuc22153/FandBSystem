import useSWR from "swr";
import {PRODUCT_RATING_ENDPOINT} from "../../../shared/constants/api";
import {fetchRating} from "../../../api/ratingApi";

export default function useReviews({ productId }) {
    const { data, error, isLoading, mutate } =
        useSWR([PRODUCT_RATING_ENDPOINT, productId], () =>
            fetchRating({ productId })
    );

    return {
        reviews: data?.ratings?.content || [],
        totalPages: data?.ratings?.totalPages || 0,
        totalReviews: data?.ratings?.totalElements || 0,
        averageScore: data?.averageScore || 0,
        error,
        isLoading,
        mutate,
    }
}