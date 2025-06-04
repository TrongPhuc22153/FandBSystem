import { Link } from "react-router-dom";
import { Button, Badge, Modal } from "react-bootstrap";
import { getImageSrc } from "../../utils/imageUtils";
import styles from "./OrderDetails.module.css";
import { SHOP_URI } from "../../constants/routes";
import { ORDER_ACTIONS, ORDER_STATUSES, ORDER_TYPES, PAYMENT_STATUS_CLASSES } from "../../constants/webConstant";
import Loading from "../Loading/Loading";
import { useRefundActions } from "../../hooks/refundHooks";
import { useOrderActions } from "../../hooks/orderHooks";
import { useEffect, useState } from "react";
import { useAlert } from "../../context/AlertContext";
import RefundPreviewModal from "../RefundPreviewModal/RefundPreviewModal";

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
  const { fetchOrderPreview } = useRefundActions();
  const [refundPreview, setRefundPreview] = useState(null);
  const [showRefundModal, setShowRefundModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  const {
    handleProcessOrder,
    processError,
    processLoading,
    resetProcess,
  } = useOrderActions();

  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (processError?.message) {
      showNewAlert({
        message: processError.message,
        variant: 'danger',
        action: resetProcess,
      });
    }
  }, [processError, showNewAlert, resetProcess]);

  const canCancelStatuses = [ORDER_STATUSES.PENDING, ORDER_STATUSES.CONFIRMED, ORDER_STATUSES.PREPARING, ORDER_STATUSES.PREPARED];

  const handleCancelClick = async () => {
    try {
      if (paymentMethod.toUpperCase() === 'PAYPAL') {
        const preview = await fetchOrderPreview(orderNumber);
        if (preview) {
          setRefundPreview(preview);
          setShowRefundModal(true);
        }
      } else {
        const result = await handleProcessOrder(orderNumber, ORDER_ACTIONS.CANCEL, orderType);
        if (result) {
          setShowSuccessModal(true);
        } else {
          showNewAlert({
            message: 'Failed to cancel order. Please try again.',
            variant: 'danger',
          });
        }
      }
    } catch (err) {
      console.error('Error handling cancellation:', err);
      showNewAlert({
        message: 'Failed to process cancellation. Please try again.',
        variant: 'danger',
      });
    }
  };

  const handleConfirmRefund = async () => {
    setShowRefundModal(false);
    try {
      const result = await handleProcessOrder(orderNumber, ORDER_ACTIONS.CANCEL, orderType);
      if (result) {
        setShowSuccessModal(true);
      } else {
        showNewAlert({
          message: 'Failed to initiate refund. Please try again.',
          variant: 'danger',
        });
      }
    } catch (err) {
      console.error('Error processing refund:', err);
      showNewAlert({
        message: 'Failed to initiate refund. Please try again.',
        variant: 'danger',
      });
    }
  };

  if (processLoading) {
    return <Loading/>
  }

  return (
    <div className="container py-3" style={{ maxWidth: '800px' }}>
      <div
        className={`d-flex flex-column justify-content-center align-items-center ${styles.orderHeading}`}
        id={styles['order-heading']}
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
                        <div style={{ minWidth: '50px' }}>
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
              {paymentMethod?.toUpperCase?.() || <Badge bg={PAYMENT_STATUS_CLASSES['PENDING']}>Pending</Badge>}
            </label>
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
                <p className="text-justify">
                  Capacity: {waitList.table.capacity}
                </p>
              </div>
            ) : orderType === ORDER_TYPES.TAKE_AWAY && shippingAddress ? (
              <div className="d-flex flex-column align-items-start">
                <b>Shipping Address</b>
                <p className="text-justify pt-2">{shippingAddress.shipName},</p>
                <p className="text-justify">{shippingAddress.shipAddress},</p>
                <p className="text-justify">
                  {shippingAddress.shipCity}, {shippingAddress.shipDistrict},{' '}
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
              {orderType === ORDER_TYPES.TAKE_AWAY && customer && (
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
              )}
              {orderType === ORDER_TYPES.DINE_IN && waitList && (
                <>
                  <p className="text-justify pt-2">
                    Name: {waitList.contactName}
                  </p>
                  <p className="text-justify">Phone: {waitList.phone}</p>
                  <p className="text-justify">
                    Party size: {waitList.partySize}
                  </p>
                </>
              )}
            </div>
          </div>
        </div>
        {canCancelStatuses.includes(orderStatus.toUpperCase()) && (
          <div className="d-flex justify-content-end mt-3">
            <Button variant="danger" onClick={handleCancelClick} disabled={processLoading}>
              Cancel Order
            </Button>
          </div>
        )}

        <RefundPreviewModal
          show={showRefundModal}
          onHide={() => setShowRefundModal(false)}
          preview={refundPreview}
          onConfirm={handleConfirmRefund}
        />

        <Modal show={showSuccessModal} onHide={() => setShowSuccessModal(false)} centered>
          <Modal.Header closeButton>
            <Modal.Title>Order Cancelled</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p>Your order #{orderNumber} has been successfully cancelled.</p>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={() => setShowSuccessModal(false)}>
              Close
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
};
export default OrderDetail;