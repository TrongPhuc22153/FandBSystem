import {createSlice} from "@reduxjs/toolkit";
import {createUserReview} from "../thunks/reviewThunk";

const initialState = {
    create: {
        isLoading: false,
        success: null,
        error: null,
    }
}

const reviewSlice = createSlice({
    name: 'reviews',
    initialState,
    reducers: {
        resetCreateState: (state) => {
            state.create.success = null;
            state.create.error = null;
        }
    },
    extraReducers: (builder) => builder
        .addCase(createUserReview.pending, (state) => {
            state.create.isLoading = true;
            state.create.error = null;
            state.create.success = null;
        })
        .addCase(createUserReview.fulfilled, (state, action) => {
            state.create.isLoading = false;
            state.create.success = action.payload.message;
        })
        .addCase(createUserReview.rejected, (state, action) => {
            state.create.isLoading = false;
            state.create.error = action.payload;
        })
})

export const {
    resetCreateState
} = reviewSlice.actions;
export default reviewSlice.reducer;