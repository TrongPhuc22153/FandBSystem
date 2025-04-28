const config = {
  TODAY_SPECIAL_SURFING_TIME: 2000,

  order_status: {
    statuses: [
      "Completed",
      "Pending",
      "Cancelled",
      "Delivered",
      "Processing",
      "Shipped",
      "Total"
    ],
    classes: {
      Shipped: "badge bg-primary",
      Completed: "badge bg-success",
      Pending: "badge bg-warning",
      Cancelled: "badge bg-danger",
      Delivered: "badge bg-primary",
      Processing: "badge bg-info",
    },
  },
  productStatusClasses: {
    Active: "badge bg-success",
    Deactive: "badge bg-danger",
  },
  paymentStatusClasses: {
    Paid: "badge bg-success",
    UnPaid: "badge bg-warning",
    "Payment Failed": "badge bg-danger",
    "Awaiting Authorization": "badge bg-primary",
    UnPaid: "badge bg-info",
  },
  orderTrackingStatuses: ["Order placed", "Packed", "Shipped", "Delivered"],
  customerStatusClasses: {
    Active: "badge bg-success",
    Deactive: "badge bg-danger",
  },
  tableColors: {
    "Default": "#3788d8", // Default color for tables
    "Table 1": "#3788d8", // Blue
    "Table 2": "#2ecc71", // Green
    "Table 3": "#e74c3c", // Red
    "Table 4": "#9b59b6", // Purple
    "Table 5": "#f1c40f", // Yellow
  }
};

export default config;
