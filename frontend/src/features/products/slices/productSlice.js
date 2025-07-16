import {createSlice} from "@reduxjs/toolkit";
import {SORTING_DIRECTIONS} from "../../../shared/constants/webConstant";

const initialState = {
    filters: {
        page: 0,
        size: 10,
        categoryId: null,
        search: "",
        isFeatured: null,
        discontinued: null,
        isDeleted: false,
        direction: SORTING_DIRECTIONS.ASC,
        sortBy: "productName",
    }
}

const productSlice = createSlice({
    name: 'products',
    initialState,
    reducers: {
        setFilters: (state, action) => {
            state.filters = { ...state.filters, ...action.payload };
        }
    }
})

export const {
    setFilters
} = productSlice.actions;
export default productSlice.reducer;