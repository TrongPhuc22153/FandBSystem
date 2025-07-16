import moment from "moment";
import "moment/locale/vi";

export const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    return moment(dateString).locale("en").format("LLL");
};
export const formatTime = (timeString) => {
    if (!timeString) return "N/A";
    return moment(timeString, 'HH:mm:ss').locale('en').format('hh:mm A');
};