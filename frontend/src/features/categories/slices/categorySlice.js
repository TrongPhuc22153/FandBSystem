import {SORTING_DIRECTIONS} from "../../../shared/constants/webConstant";
import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    filters: {
        page: 0,
        size: 10,
        direction: SORTING_DIRECTIONS.ASC,
        field: "categoryName",
        search: null,
        isDeleted: false,
    }
}

const categorySlice = createSlice({
    name: 'categories',
    initialState,
    reducers: {
        setFilters: (state, action) => {
            state.filters = {...state.filters, ...action.payload };
        }
    }
})

export const {
    setFilters
} =  categorySlice.actions;
export default categorySlice.reducer;