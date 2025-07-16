import useSWR from "swr";
import {fetchUser} from "../services/usersApi";
import {useDispatch, useSelector} from "react-redux";
import {USER_ENDPOINT} from "../../../shared/constants/api";
import {useEffect} from "react";
import {setUser} from "../../auth/slices/authSlice";

export const useSignedUser = () => {
    const token = useSelector((state) => state.auth.token);
    const dispatch = useDispatch();
    const { data, isLoading, error, mutate } =
        useSWR(token ? [USER_ENDPOINT, token]: null, () => fetchUser({ token }))

    useEffect(() => {
        dispatch(setUser(data));
    }, [dispatch, data])

    return {
        user: data,
        isLoading,
        error,
        refetch: mutate,
    };
}
export default useSignedUser;