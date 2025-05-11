import moment from "moment";
import "moment/locale/vi";

export const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    return moment(dateString).locale("en").format("LLL");
};