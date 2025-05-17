import { USER_NOTIFICATION_ENDPOINT, USER_NOTIFICATIONS_ENDPOINT } from "../constants/api";
import { SORTING_DIRECTIONS } from "../constants/webConstant";

export const fetchNotifications = async ({ page = 0, size = 10, field = "createdAt", direction = SORTING_DIRECTIONS.DESC, isRead, token }) => {
    const params = new URLSearchParams();
    params.append("page", page.toString());
    params.append("size", size.toString());
    params.append("field", field.toString());
    params.append("direction", direction.toString());
    if(isRead != null && isRead != undefined){
        params.append("isRead", isRead.toString());
    }

    const response = await fetch(`${USER_NOTIFICATIONS_ENDPOINT}?${params.toString()}`,
        {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        }
    );

    if (!response.ok) {
        throw await response.json();
    }
    return response.json();
};

export const updateNotificationIsReadStatus = async ({ notificationId, isRead, token }) => {
    const response = await fetch(USER_NOTIFICATION_ENDPOINT(notificationId), {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ isRead }),
    });

    if (!response.ok) {
        throw await response.json();
    }

    return response.json();
};