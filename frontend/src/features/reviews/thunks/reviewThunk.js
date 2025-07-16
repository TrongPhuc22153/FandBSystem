import {createAsyncThunk} from "@reduxjs/toolkit";
import {createRating} from "../../../api/ratingApi";

export const createUserReview = createAsyncThunk("reviews/createUserReview",
    async({ productId, score, comment }, { getState, rejectWithValue }) =>{
        const token = getState().auth.token;
        try {
            const { message, data } = await createRating({
                productId,
                score,
                comment,
                token
            })
            return { message, data };
        }catch (e) {
            return rejectWithValue(e);
        }
    })