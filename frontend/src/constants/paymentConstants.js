import { CANCEL_PAYMENT_URI, SUCCESS_PAYMENT_URI } from "./routes";

const FRONT_END_DOMAIN = "http://localhost:3000"
export const SUCCESS_PAYMENT_URL = FRONT_END_DOMAIN + SUCCESS_PAYMENT_URI;
export const CANCEL_PAYMENT_URL = FRONT_END_DOMAIN + CANCEL_PAYMENT_URI;
export const PAYMENT_METHODS = {
    COD: "cod",
    PAYPAL: "paypal",
    CASH: "cash"
}
export const PAYMENT_TYPES = {
    RESERVATION: "reservation",
    DINE_IN: "dine_in",
    TAKE_AWAY: "take_away"
}