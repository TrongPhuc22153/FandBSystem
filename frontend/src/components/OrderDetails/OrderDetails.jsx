import { Link } from "react-router-dom";
import { getImageSrc } from "../../utils/imageUtils";
import styles from "./OrderDetails.module.css";
import { SHOP_URI } from "../../constants/routes";
import {
  ORDER_TYPES,
} from "../../constants/webConstant";

const OrderDetail = ({
  orderDate,
  orderNumber,
  orderStatus,
  orderItems,
  shippingCost,
  total,
  orderType,
  shippingAddress,
  waitList,
  customer,
  paymentMethod,
}) => {
  return (
    <div className="container py-3" style={{ maxWidth: "800px" }}>
      <div
        className={`d-flex flex-column justify-content-center align-items-center ${styles.orderHeading}`}
        id={styles["order-heading"]}
      >
        <div className={`text-uppercase ${styles.textUppercase}`}>
          <p>Order Detail</p>
        </div>
        <div className={`h4 ${styles.h4}`}>{orderDate}</div>
        <div className="pt-1">
          <p>
            Order #{orderNumber} is currently
            <b className="ms-1 text-dark">{orderStatus}</b>
          </p>
        </div>
      </div>
      <div className={`wrapper bg-white ${styles.wrapper}`}>
        <div className="table-responsive">
          <table className="table table-borderless">
            <thead>
              <tr className="text-uppercase text-muted">
                <th scope="col">Product</th>
                <th scope="col" className="text-end">
                  Total
                </th>
              </tr>
            </thead>
            <tbody>
              {orderItems &&
                orderItems.map((item) => (
                  <tr key={item.id}>
                    <th scope="row">
                      <div className="d-flex justify-content-start align-items-center list py-1">
                        <div style={{ minWidth: "50px" }}>
                          <b>{item.quantity}x</b>
                        </div>
                        <div className="mx-3">
                          <img
                            src={item.product.picture || getImageSrc()}
                            alt={item.product.productName}
                            className="rounded-circle"
                            width="30"
                            height="30"
                          />
                        </div>
                        <div className="order-item">
                          <Link
                            to={`${SHOP_URI}/${item.product.productName}?id=${item.product.productId}`}
                            className={styles.productLink}
                            aria-label={`View ${item.product.productName}`}
                          >
                            {item.product.productName}
                          </Link>
                        </div>
                      </div>
                    </th>
                    <td className="text-end">
                      <b className={styles.price}>
                        ${(item.unitPrice * item.quantity).toFixed(2)}
                      </b>
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
        <div className="pt-2 border-bottom mb-3"></div>
        <div className="d-flex justify-content-start align-items-center pl-3">
          <div className="text-muted">Payment Method</div>
          <div className="ms-auto">
            <label className={styles.label}>
              {paymentMethod.toUpperCase()}
            </label>{" "}
          </div>
        </div>
        {orderType === ORDER_TYPES.TAKE_AWAY && ( 
          <div className="d-flex justify-content-start align-items-center py-1 pl-3">
            <div className="text-muted">Shipping: </div>
            <div className="ms-auto">
              <label className={styles.price}>${shippingCost.toFixed(2)}</label>
              {shippingCost === 0 && <label> (Free)</label>}
            </div>
          </div>
        )}
        <div className="d-flex justify-content-start align-items-center pl-3 py-3 my-4 border-top border-bottom">
          <div className="text-muted">Today's Total</div>
          <div className={`ms-auto h5 ${styles.price}`}>
            ${total.toFixed(2)}
          </div>
        </div>
        <div className="row border rounded p-1 my-3">
          <div className="col-md-6 py-3">
            {orderType === ORDER_TYPES.DINE_IN && waitList ? (
              <div className="d-flex flex-column align-items-start">
                <b>Dine-in Details</b>
                <p className="text-justify pt-2">
                  Table Number: {waitList.table.tableNumber}
                </p>
                <p className="text-justify">
                  Location: {waitList.table.location}
                </p>
                <p className="text-justify">Capacity: {waitList.table.capacity}</p>
              </div>
            ) : orderType === ORDER_TYPES.TAKE_AWAY && shippingAddress ? (
              <div className="d-flex flex-column align-items-start">
                <b>Shipping Address</b>
                <p className="text-justify pt-2">
                  {shippingAddress.shipName},
                </p>
                <p className="text-justify">{shippingAddress.shipAddress},</p>
                <p className="text-justify">
                  {shippingAddress.shipCity}, {shippingAddress.shipDistrict},{" "}
                  {shippingAddress.shipWard}
                </p>
                <p className="text-justify">Phone: {shippingAddress.phone}</p>
              </div>
            ) : (
              <div className="d-flex flex-column align-items-start">
                <b>Order Details</b>
                <p className="text-justify pt-2">
                  No specific details available for this order type.
                </p>
              </div>
            )}
          </div>
          <div className="col-md-6 py-3">
            <div className="d-flex flex-column align-items-start">
              <b>Customer Information</b>
              {orderType === ORDER_TYPES.TAKE_AWAY && customer &&
                <>
                  <p className="text-justify pt-2">
                    Name: {customer.contactName}
                  </p>
                  <p className="text-justify">
                    Email: {customer.profile.user.email}
                  </p>
                  <p className="text-justify">
                    Phone: {customer.profile.phone}
                  </p>
                </>
              }
              {orderType === ORDER_TYPES.DINE_IN && waitList && 
                <>
                  <p className="text-justify pt-2">
                    Name: {waitList.contactName}
                  </p>
                  <p className="text-justify">
                    Phone: {waitList.phone}
                  </p>
                  <p className="text-justify">
                    Party size: {waitList.partySize}
                  </p>
                </>
              }
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default OrderDetail;