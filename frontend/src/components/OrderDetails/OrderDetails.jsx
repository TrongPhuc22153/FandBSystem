import { Link } from "react-router-dom";
import { getImageSrc } from "../../utils/imageUtils";
import styles from "./OrderDetails.module.css";
import { SHOP_URI } from "../../constants/routes";

const OrderDetail = ({
  orderDate,
  orderNumber,
  orderStatus,
  orderItems,
  shippingCost,
  total,
  table,
  shippingAddress,
}) => {
  return (
    <div className="container py-3" style={{ maxWidth: "800px" }}>
      <div
        className={`d-flex flex-column justify-content-center align-items-center ${styles.orderHeading}`}
        id={styles["order-heading"]}
      >
        <div className={`text-uppercase ${styles.textUppercase}`}>
          <p>Order detail</p>
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
                <th scope="col">product</th>
                <th scope="col" className="text-end">
                  total
                </th>
              </tr>
            </thead>
            <tbody>
              {orderItems &&
                orderItems.map((item) => (
                  <tr key={item.id}>
                    <th scope="row">
                      <div
                        key={item.id}
                        className="d-flex justify-content-start align-items-center list py-1"
                      >
                        <div style={{ minWidth: "50px" }}>
                          <b>{item.quantity}px</b>
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
        {!table && (
          <div className="d-flex justify-content-start align-items-center py-1 pl-3">
            <div className="text-muted">Shipping: </div>
            <div className="ms-auto">
              <label className={styles.price}>${shippingCost.toFixed(2)}</label>
              {shippingCost === 0 && <label> (Free)</label>}
            </div>
          </div>
        )}
        <div className="d-flex justify-content-start align-items-center pl-3 py-3 mb-4 border-bottom">
          <div className="text-muted"> Today's Total </div>
          <div className={`ms-auto h5 ${styles.price}`}>
            ${total.toFixed(2)}
          </div>
        </div>
        {table ? (
          <div className="row border rounded p-1 my-3">
            <div className="col-md-6 py-3">
              <div className="d-flex flex-column align-items-start">
                <b>Table Information</b>
                {table && (
                  <>
                    <p className="text-justify pt-2">
                      Table Number: {table.tableNumber}
                    </p>
                    <p className="text-justify">Location: {table.location}</p>
                    <p className="text-justify">Capacity: {table.capacity}</p>
                  </>
                )}
              </div>
            </div>
          </div>
        ) : (
          <div className="row border rounded p-1 my-3">
            <div className="col-md-6 py-3">
              <div className="d-flex flex-column align-items start">
                <b>Shipping Address</b>
                {shippingAddress && (
                  <>
                    <p className="text-justify pt-2">
                      {shippingAddress.shipName},
                    </p>
                    <p className="text-justify">
                      {shippingAddress.shipAddress},
                    </p>
                    <p className="text-justify">
                      {shippingAddress.shipCity}, {shippingAddress.shipDistrict}
                      ,{shippingAddress.shipWard}
                    </p>
                    <p className="text-justify">
                      Phone: {shippingAddress.phone}
                    </p>
                  </>
                )}
              </div>
            </div>
            <div className="col-md-6 py-3"></div>
          </div>
        )}
      </div>
    </div>
  );
};

export default OrderDetail;
