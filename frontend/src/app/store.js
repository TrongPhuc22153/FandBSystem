import {configureStore} from "@reduxjs/toolkit";
import authReducers from "../features/auth/slices/authSlice";
import productReducers from "../features/products/slices/productSlice";
import categoryReducers from "../features/categories/slices/categorySlice";
import reviewsReducers from "../features/reviews/slices/reviewSlice";
import cartReducers from "../features/cart/slices/cartSlice";

const store = configureStore({
    reducer: {
        auth: authReducers,
        products: productReducers,
        categories: categoryReducers,
        reviews: reviewsReducers,
        cart: cartReducers,
    }
})

export default store;