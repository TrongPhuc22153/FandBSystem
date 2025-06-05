import { useOrder, useOrderActions } from "../../hooks/orderHooks";
import { useParams } from "react-router-dom";
import { formatDate } from "../../utils/datetimeUtils";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import OrderDetail from "../../components/OrderDetails/OrderDetails";
import { Button, Modal } from "react-bootstrap";
import { useRefundActions } from "../../hooks/refundHooks";
import { useAlert } from "../../context/AlertContext";
import { useModal } from "../../context/ModalContext";
import { ORDER_ACTIONS } from "../../constants/webConstant";
import { useCallback, useEffect, useState } from "react";
import RefundPreviewModal from "../../components/RefundPreviewModal/RefundPreviewModal";
import { PAYMENT_METHODS } from "../../constants/paymentConstants";

function UserOrderDetailsPage() {
  const { id } = useParams();

  const [refundPreview, setRefundPreview] = useState(null);
  const [showRefundModal, setShowRefundModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  const {
    data: orderData,
    isLoading: loadingOrder,
    error: orderError,
    mutate: refetchOrder,
  } = useOrder({ orderId: id });

  const orderDate = formatDate(orderData?.orderDate);
  const orderNumber = orderData?.orderId;
  const orderStatus = orderData?.status?.toLowerCase();
  const shippingCost = orderData?.shippingCost || 0;
  const shippingAddress = orderData?.shippingAddress;
  const table = orderData?.table || null;
  const orderItems = orderData?.orderDetails || [];
  const orderType = orderData?.type;
  const customer = orderData?.customer;
  const total = orderData?.totalPrice || 0;
  const waitList = orderData?.waitList;
  const paymentMethod = orderData?.payment.method;
  const paymentStatus = orderData?.payment.status;

  const { showNewAlert } = useAlert();

  const {
    handleProcessOrder,
    processError,
    processSuccess,
    processLoading,
    resetProcess,
  } = useOrderActions();

  const { fetchOrderPreview } = useRefundActions();
  const { onOpen } = useModal();

  useEffect(() => {
    if (processError?.message) {
      showNewAlert({
        message: processError.message,
        variant: "danger",
        action: resetProcess,
      });
    }
  }, [processError, showNewAlert, resetProcess]);

  useEffect(() => {
    if (processSuccess) {
      showNewAlert({
        message: processSuccess || "Reservation processed successfully.",
        variant: "success",
        action: () => {
          setShowSuccessModal(true);
          resetProcess();
        },
      });
    }
  }, [processSuccess, showNewAlert, resetProcess]);

  const handleCancelOrder = useCallback(async () => {
    const result = await handleProcessOrder(
        orderNumber,
        ORDER_ACTIONS.CANCEL,
        orderType
      );
      if (result) {
        setShowSuccessModal(true);
        refetchOrder();
      }
  }, [
    handleProcessOrder,
    orderNumber,
    orderType,
    refetchOrder,
  ]);

  const handleCancelClick = useCallback(async () => {
    if (paymentMethod.toLowerCase() === PAYMENT_METHODS.PAYPAL) {
      const preview = await fetchOrderPreview(orderNumber);
      if (preview) {
        setRefundPreview(preview);
        setShowRefundModal(true);
      }
    } else {
      onOpen({
        title: "Cancel Order",
        message: `Are you sure you want to cancel order #${orderNumber}?`,
        onYes: handleCancelOrder,
      });
    }
  }, [
    orderNumber,
    paymentMethod,
    fetchOrderPreview,
    onOpen,
    handleCancelOrder,
  ]);

  const handleConfirmRefund = async () => {
    setShowRefundModal(false);
    const result = await handleProcessOrder(
      orderNumber,
      ORDER_ACTIONS.CANCEL,
      orderType
    );
    if (result) {
      setShowSuccessModal(true);
      refetchOrder();
    }
  };

  if (loadingOrder || processLoading) {
    return <Loading />;
  }

  if (orderError?.message) {
    return <ErrorDisplay message={orderError.message} />;
  }

  return (
    <div>
      <OrderDetail
        processLoading={processLoading}
        onHandleCancel={handleCancelClick}
        orderDate={orderDate}
        orderNumber={orderNumber}
        orderStatus={orderStatus}
        orderItems={orderItems}
        orderType={orderType}
        shippingCost={shippingCost}
        table={table}
        total={total}
        waitList={waitList}
        customer={customer}
        shippingAddress={shippingAddress}
        paymentMethod={paymentMethod}
        paymentStatus={paymentStatus}
      />

      <RefundPreviewModal
        show={showRefundModal}
        onHide={() => setShowRefundModal(false)}
        preview={refundPreview}
        onConfirm={handleConfirmRefund}
      />

      <Modal
        show={showSuccessModal}
        onHide={() => setShowSuccessModal(false)}
        centered
      >
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
  );
}

export default UserOrderDetailsPage;
