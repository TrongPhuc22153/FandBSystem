import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faAddressCard,
  faReceipt,
  faTruckFast,
} from "@fortawesome/free-solid-svg-icons";
import config from "../../../../../config/WebConfig";

const dummyData = {
  orderId: "ORD123456",
  customer: {
    name: "John Doe",
    email: "johndoe@example.com",
    phone: "+1234567890",
    address: {
      street: "123 Main St",
      city: "New York",
      state: "NY",
      zip: "10001",
      country: "USA",
    },
  },
  items: [
    {
      productId: "P001",
      name: "Wireless Mouse",
      quantity: 1,
      price: 25.99,
    },
    {
      productId: "P002",
      name: "Mechanical Keyboard",
      quantity: 1,
      price: 79.99,
    },
  ],
  subtotal: 105.98,
  shipping: 8.48,
  total: 114.46,
  paymentMethod: "Credit Card",
  orderStatus: "Shipped",
  orderDate: "2025-04-01T10:30:00Z",
  delivery: {
    method: "Standard Shipping",
    trackingNumber: "TRACK123456",
    estimatedDelivery: "2025-04-05",
  },
};

export default function OrderDetailsAdmin() {
  const [order, setOrder] = useState(dummyData);

  // Determine which stages should be active
  const getStatusClass = (stage) => {
    const stages = config.orderTrackingStatuses;
    const currentIndex = stages.indexOf(order.orderStatus);
    const stageIndex = stages.indexOf(stage);
    return stageIndex <= currentIndex ? "active step0" : "step0";
  };

  return (
    <div className="container-fluid">
      <h1 className="h3 my-3">
        <strong>Order Details</strong>
      </h1>
      <div className="mb-5">
        <div className="container px-1 px-md-4 py-5 mx-auto">
          <div className="card">
            <div className="row d-flex justify-content-between px-3 top">
              <div className="d-flex">
                <h5>
                  ORDER{" "}
                  <span className="text-primary font-weight-bold">
                    #{order.orderId}
                  </span>
                </h5>
              </div>
              <div className="d-flex flex-column text-sm-right">
                <p className="mb-0">
                  Expected Arrival{" "}
                  <span>{order.delivery.estimatedDelivery}</span>
                </p>
              </div>
            </div>
            <div className="row d-flex justify-content-center">
              <div className="col-12">
                <ul id="progressbar" className="text-center">
                  {config.orderTrackingStatuses.map((stage, index) => (
                    <li key={index} className={getStatusClass(stage)}>
                      <span>{stage}</span>
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          </div>

          <div className="row">
            <div className="col-md-8 d-flex">
              <div className="card flex-fill">
                <div className="card-header">
                  <h5 className="card-title mb-0">
                    ITEMS FROM ORDER #{order.orderId}
                  </h5>
                </div>
                <div className="card-body">
                  <table className="table table-hover my-0">
                    <thead>
                      <tr>
                        <th>Item Name</th>
                        <th className="d-none d-xl-table-cell">Quantity</th>
                        <th>Price</th>
                        <th>Total Price</th>
                      </tr>
                    </thead>
                    <tbody>
                      {order.items.map((item, index) => (
                        <tr key={index}>
                          <td>{item.name}</td>
                          <td className="d-none d-xl-table-cell">
                            {item.quantity}
                          </td>
                          <td>{item.price}</td>
                          <td>{item.quantity * item.price}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <div className="col-md-4 d-flex">
              <div className="card flex-fill">
                <div className="card-header">
                  <h5 className="card-title mb-0">ORDER SUMMARY</h5>
                </div>
                <div className="card-body">
                  <table className="table table-hover my-0">
                    <thead>
                      <tr>
                        <th>Description</th>
                        <th>Price</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>Grand Total</td>
                        <td>{order.subtotal}</td>
                      </tr>
                      <tr>
                        <td>Shipping Charge</td>
                        <td>{order.shipping}</td>
                      </tr>
                      <tr>
                        <td>
                          <b>Total</b>{" "}
                        </td>
                        <td>{order.total}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <div className="row">
            <div className="col-md-4 d-flex">
              <div className="card flex-fill">
                <div className="card-header">
                  <h5 className="card-title mb-0">SHIPPING INFORMATION</h5>
                </div>
                <div className="card-body">
                  <span className="d-block my-3 fs-2 text-center">
                    <FontAwesomeIcon icon={faAddressCard} />
                  </span>
                  <h4 className="my-2 p-0">{order.customer.name}</h4>
                  <address>
                    {order.customer.address.street}
                    <br />
                    {order.customer.address.city},{" "}
                    {order.customer.address.state} {order.customer.address.zip}
                    <br />
                    <abbr title="Mobile">M:</abbr> {order.customer.phone}
                    <br />
                    <abbr title="Email">E:</abbr> {order.customer.email}
                  </address>
                </div>
              </div>
            </div>
            <div className="col-md-4 d-flex">
              <div className="card flex-fill">
                <div className="card-header">
                  <h5 className="card-title mb-0">BILLING INFORMATION</h5>
                </div>
                <div className="card-body">
                  <span className="d-block my-3 fs-2 text-center">
                    <FontAwesomeIcon icon={faReceipt} />
                  </span>
                  <p>
                    <b>Payment Method:</b> {order.paymentMethod}
                  </p>
                  <p>
                    <b>Order Status:</b> {order.orderStatus}
                  </p>
                  <p>
                    <b>Order Date:</b> {order.orderDate}
                  </p>
                </div>
              </div>
            </div>
            <div className="col-md-4 d-flex">
              <div className="card flex-fill">
                <div className="card-header">
                  <h5 className="card-title mb-0">DELIVERY INFORMATION</h5>
                </div>
                <div className="card-body text-center">
                  <span className="d-block my-3 fs-2">
                    <FontAwesomeIcon icon={faTruckFast} />
                  </span>
                  <p>
                    <b>{order.delivery.method}</b>
                  </p>
                  <p>
                    <b>Tracking Number:</b> {order.delivery.trackingNumber}
                  </p>
                  <p>
                    <b>Estimated Delivery:</b>{" "}
                    {order.delivery.estimatedDelivery}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
