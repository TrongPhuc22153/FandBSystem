import {createSlice} from "@reduxjs/toolkit";
import {addToCart} from "../thunks/cartThunk";

const initialState = {
    cart: {
        items: [],

    },
    add: {
        success: null,
        error: null,
        isLoading: false,
    }
}

const cartSlice = createSlice({
    name: "cart",
    initialState,
    reducers: {
        resetAddState: (state) => {
            state.add.success = null;
            state.add.error = null;
        }
    },
    extraReducers: (builder) => builder
        .addCase(addToCart.pending, (state) => {
            state.add.success = null;
            state.add.error = null;
            state.add.isLoading = true;
        })
        .addCase(addToCart.fulfilled, (state, action) => {
            state.add.success = action.payload.message;
            state.add.isLoading = false;
        })
        .addCase(addToCart.rejected, (state, action) => {
            state.add.error = action.payload;
            state.add.isLoading = false;
        })
})

export const {
    resetAddState
} = cartSlice.actions;
export default cartSlice.reducer;