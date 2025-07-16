import {createAsyncThunk} from "@reduxjs/toolkit";
import {addCartItem} from "../../../api/cartApi";

export const addToCart = createAsyncThunk("cart/addToCart",
    async({ productId, quantity }, { getState, rejectWithValue }) => {
        const token = getState().auth.token;
        try {
            const { message, data } = await addCartItem({ token, requestCartItemDTO: { productId, quantity } });
            return { message, data };
        }catch (error) {
            return rejectWithValue(error);
        }
    })