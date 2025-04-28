import axios from "axios";
import { CATEGORIES_URL } from "../constants/ApiEndpoints";

export function getCategories(){
    return axios.get(CATEGORIES_URL, {
        headers: {
            "Content-Type": "application/json"
        }
    })
}