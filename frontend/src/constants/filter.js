import { ORDER_STATUSES, RESERVATION_STATUSES } from "./webConstant";
import moment from "moment";
import "moment/locale/vi";

export const ORDER_FILTER_MAPPING = [
  {
    label: "All",
    statuses: [
      ORDER_STATUSES.PENDING,
      ORDER_STATUSES.CONFIRMED,
      ORDER_STATUSES.PREPARING,
      ORDER_STATUSES.PREPARED,
    ],
    activeClass: "btn-primary",
    inactiveClass: "btn-outline-primary",
  },
  {
    label: ORDER_STATUSES.PENDING,
    statuses: [ORDER_STATUSES.PENDING],
    activeClass: "btn-primary",
    inactiveClass: "btn-outline-primary",
  },
  {
    label: ORDER_STATUSES.PREPARING,
    statuses: [ORDER_STATUSES.PREPARING],
    activeClass: "btn-primary",
    inactiveClass: "btn-outline-primary",
  },
  {
    label: ORDER_STATUSES.PREPARED,
    statuses: [ORDER_STATUSES.PREPARED],
    activeClass: "btn-primary",
    inactiveClass: "btn-outline-primary",
  },
];

export const RESERVATION_FILTER_MAPPING = [
  {
    label: "All",
    statuses: [
      RESERVATION_STATUSES.PENDING,
      RESERVATION_STATUSES.CONFIRMED,
      RESERVATION_STATUSES.PREPARING,
      RESERVATION_STATUSES.PREPARED,
    ],
    activeClass: "btn-primary",
    inactiveClass: "btn-outline-primary",
  },
  {
    label: RESERVATION_STATUSES.PENDING,
    statuses: [RESERVATION_STATUSES.PENDING],
    activeClass: "btn-primary",
    inactiveClass: "btn-outline-primary",
  },
  {
    label: RESERVATION_STATUSES.PREPARING,
    statuses: [RESERVATION_STATUSES.PREPARING],
    activeClass: "btn-primary",
    inactiveClass: "btn-outline-primary",
  },
  {
    label: RESERVATION_STATUSES.PREPARED,
    statuses: [RESERVATION_STATUSES.PREPARED],
    activeClass: "btn-primary",
    inactiveClass: "btn-outline-primary",
  },
];

export const DATE_FILTER = [
  {
    value: "today",
    label: "Today",
    getRange: () => {
      const now = moment();
      return {
        start: now.startOf("day").format("YYYY-MM-DD"),
        end: now.endOf("day").format("YYYY-MM-DD"),
      };
    },
  },
  {
    value: "week",
    label: "This Week",
    getRange: () => {
      const now = moment();
      return {
        start: now.startOf("week").format("YYYY-MM-DD"),
        end: now.endOf("week").format("YYYY-MM-DD"),
      };
    },
  },
  {
    value: "month",
    label: "This Month",
    getRange: () => {
      const now = moment();
      return {
        start: now.startOf("month").format("YYYY-MM-DD"),
        end: now.endOf("month").format("YYYY-MM-DD"),
      };
    },
  },
];