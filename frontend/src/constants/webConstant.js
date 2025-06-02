export const ROLE_CLASSES = {
    ADMIN: "success",
    CUSTOMER: "info"
}
export const TABLE_STATUS_CLASSES = {
    OCCUPIED: "danger",
    UNOCCUPIED: "success",
}
export const TABLE_STATUSES = {
    OCCUPIED: "Occupied",
    UNOCCUPIED: "Unoccupied",
    RESERVED: "Reserved",
    CLEANING: "Cleaning",
}
export const ORDER_TYPES = {
    DINE_IN: "DINE_IN",
    TAKE_AWAY: "TAKE_AWAY"
}
export const ORDER_TYPE_CLASSES = {
    DINE_IN: "info",
    TAKE_AWAY: "success",
    DEFAULT: "secondary"
}

export const ORDER_ITEM_STATUS_CLASSES = {
    CANCELLED: "danger",
    PENDING: "danger",
    PREPARING: "warning",
    PREPARED: "success",
    SERVED: "secondary",
    DEFAULT: "primary"
}

export const ORDER_ITEM_STATUSES = {
    CANCELLED: "CANCELLED",
    PENDING: "PENDING",
    PREPARING: "PREPARING",
    PREPARED: "PREPARED",
    SERVED: "SERVED",
}

export const ORDER_ACTIONS = {
    CONFIRM: "CONFIRM",
    CANCEL: "CANCEL",
    PREPARING: "PREPARING",
    PREPARED: "PREPARED",
    READY: "READY",
    SERVED: "SERVED",
    COMPLETE: "COMPLETE"
}
export const ORDER_STATUSES = {
    CANCELLED: "CANCELLED",
    PENDING: "PENDING",
    CONFIRMED: "CONFIRMED",
    PREPARING: "PREPARING",
    PREPARED: "PREPARED",
    READY_TO_SERVE: "READY_TO_SERVE",
    READY_TO_PICKUP: "READY_TO_PICKUP",
    PARTIALLY_SERVED: "PARTIALLY_SERVED",
    SERVED: "SERVED",
    COMPLETED: "COMPLETED",
}
export const ORDER_STATUS_CLASSES = {
    CANCELLED: "danger",
    PENDING: "danger",
    PREPARING: "warning",
    PREPARED: "success",
    READY_TO_SERVE: "success",
    READY_TO_PICKUP: "success",
    PARTIALLY_SERVED: "info",
    SERVED: "success",
    COMPLETED: "secondary",
    DEFAULT: "primary"
}

export const RESERVATION_ACTIONS = {
    CONFIRM: "CONFIRM",
    CANCEL: "CANCEL",
    PREPARING: "PREPARING",
    PREPARED: "PREPARED",
    READY: "READY",
    SERVED: "SERVED",
    COMPLETE: "COMPLETE"
}
export const RESERVATION_STATUSES = {
    CANCELLED: "CANCELLED",
    PENDING: "PENDING",
    CONFIRMED: "CONFIRMED",
    PREPARING: "PREPARING",
    PREPARED: "PREPARED",
    READY_TO_SERVE: "READY_TO_SERVE",
    PARTIALLY_SERVED: "PARTIALLY_SERVED",
    SERVED: "SERVED",
    COMPLETED: "COMPLETED",
}
export const RESERVATION_STATUS_CLASSES = {
    CANCELLED: "danger",
    PENDING: "danger",
    PREPARING: "warning",
    PREPARED: "success",
    COMPLETED: "secondary",
    DEFAULT: "primary"
}

export const RESERVATION_ITEM_STATUS_CLASSES = {
    CANCELLED: "danger",
    PENDING: "danger",
    PREPARING: "warning",
    PREPARED: "success",
    SERVED: "secondary",
    DEFAULT: "primary"
}

export const RESERVATION_ITEM_STATUSES = {
    CANCELLED: "CANCELLED",
    PENDING: "PENDING",
    PREPARING: "PREPARING",
    PREPARED: "PREPARED",
    SERVED: "SERVED",
}

export const RESERVATION_TIME = {
    DEFAULT_DURATION_HOURS: 2,
    MIN_TIME: "10:00",
    MAX_TIME: "21:00"
}

export const DEFAULT_RESERVATION_DURATION_HOURS = 2;

export const PAYMENT_STATUS_CLASSES = {
    PENDING: "info",
    SUCCESSFUL: "success",
    CANCELLED: "danger",
    FAILED: "danger",
    DEFAULT: "primary"
}

export const PAYMENT_STATUSES = {
    PENDING: "PENDING",
    SUCCESSFUL: "SUCCESSFUL",
    CANCELLED: "CANCELLED",
    FAILED: "FAILED",
}

export const SORTING_DIRECTIONS = {
    ASC: "ASC",
    DESC: "DESC"
}
export const CHECKOUT_ITEMS = "checkoutItems";

export const TABLE_OCCUPANCY_STATUSES = {
    SEATED: "SEATED",
    COMPLETED: "COMPLETED",
    CANCELLED: "CANCELLED",
    CLEANING: "CLEANING",
    WAITING: "WAITING"
}
export const TABLE_OCCUPANCY_TYPES = {
    RESERVATION: "RESERVATION",
    WALK_IN: "WALK_IN"
}