import useSWR from "swr";
import {PRODUCT_RATING_ENDPOINT} from "../../../shared/constants/api";
import {fetchUserProductRating} from "../../../api/ratingApi";
import {useSelector} from "react-redux";

const useUserProductRating = ({ productId }) => {
    const token = useSelector((state) => state.auth.token);
    const { data, error, isLoading, mutate } =
        useSWR(token ? [PRODUCT_RATING_ENDPOINT, productId, token]: null, () =>
            fetchUserProductRating({ productId, token }),
            {
                revalidateOnFocus: false,
                shouldRetryOnError: false,
            }
    );

    return {
        review: data,
        isLoading,
        error,
        mutate
    }
};
export default useUserProductRating;