import moment from "moment";
import "moment/locale/vi";

export function computePercentages(data) {
  const total = data.values.reduce((sum, val) => sum + val, 0);
  const percentages = data.values.map(val => (val / total) * 100);
  return {
    labels: data.labels,
    values: data.values,
    data: percentages.map(p => p.toFixed(2))
  };
}

export const getDateRange = (filter) => {
  const now = moment();

  switch (filter) {
    case "today":
      return {
        startDate: now.clone().startOf("day").format("YYYY-MM-DD"),
        endDate: now.clone().endOf("day").format("YYYY-MM-DD"),
      };
    case "week":
      return {
        startDate: now.clone().startOf("week").format("YYYY-MM-DD"),
        endDate: now.clone().endOf("week").format("YYYY-MM-DD"),
      };
    case "month":
      return {
        startDate: now.clone().startOf("month").format("YYYY-MM-DD"),
        endDate: now.clone().endOf("month").format("YYYY-MM-DD"),
      };
    default:
      return {
        startDate: null,
        endDate: null,
      };
  }
};