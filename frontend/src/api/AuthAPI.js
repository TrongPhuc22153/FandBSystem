import axios from "axios";
import { LOGIN_URL, LOGOUT_URL } from "../constants/ApiEndpoints"

export async function Login(data) {
    return await axios.post(LOGIN_URL, data, {
        headers: {
            'Content-Type': 'application/json',
        },
    });
}

export async function Logout() {
    return await axios.post(LOGOUT_URL, {}, {
        headers: {
            'Content-Type': 'application/json',
        },
    });
}