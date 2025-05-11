import React, { useCallback, useEffect } from "react";
import { useOrder, useOrderActions } from "../../hooks/orderHooks";
import { useParams } from "react-router-dom";
import { formatDate } from "../../utils/datetimeUtils";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useModal } from "../../context/ModalContext";
import { ORDER_ACTIONS, ORDER_STATUSES } from "../../constants/webConstant";
import { useAlert } from "../../context/AlertContext";
import OrderDetails from "../../components/OrderDetails/OrderDetails";

function AdminOrderDetailsPage() {
  const { id } = useParams();
  const {
    data: orderData,
    isLoading: loadingOrder,
    error: orderError,
    mutate,
  } = useOrder({ orderId: id });
  const {
    handleProcessOrder,
    processError,
    processLoading,
    processSuccess,
    resetProcess,
  } = useOrderActions();
  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  useEffect(() => {
    if (processSuccess) {
      showNewAlert({
        message: processSuccess,
        action: resetProcess,
      });
    }
  }, [processSuccess, resetProcess, showNewAlert]);

  const handleConfirm = useCallback(async () => {
    const data = await handleProcessOrder(id, ORDER_ACTIONS.CONFIRM);
    if (data) {
      mutate();
    }
  }, [id, handleProcessOrder, mutate]);

  const handleCancel = useCallback(async () => {
    const data = await handleProcessOrder(id, ORDER_ACTIONS.CANCEL);
    if (data) {
      mutate();
    }
  }, [id, handleProcessOrder, mutate]);

  const showConfirmModal = useCallback(
    (action) => {
      switch (action) {
        case ORDER_ACTIONS.CONFIRM:
          onOpen({
            title: "Confirm order",
            message: "Do you want to confirm this order?",
            onYes: handleConfirm,
          });
          break;
        case ORDER_ACTIONS.CANCEL:
          onOpen({
            title: "Cancel order",
            message: "Do you want to cancel this order?",
            onYes: handleCancel,
          });
          break;
        default:
          showNewAlert({
            message: `There is no action ${action}`,
            variant: "danger",
          });
          break;
      }
    },
    [id, handleConfirm, handleCancel, onOpen, showNewAlert]
  );

  if (loadingOrder || processLoading) {
    return <Loading />;
  }

  if (orderError?.message || processError?.message) {
    return (
      <ErrorDisplay message={orderError?.message || processError?.message} />
    );
  }

  const orderDate = formatDate(orderData?.orderDate);
  const orderNumber = orderData?.orderId;
  const orderStatus = orderData?.status?.toLowerCase();
  const shippingCost = orderData?.shippingCost || 0;
  const shippingAddress = orderData?.shippingAddress;
  const table = orderData?.table || null;
  const orderItems = orderData?.orderDetails || [];
  const billingAddress = orderData?.customer;
  const total = orderItems.reduce(
    (sum, item) => sum + item.unitPrice * item.quantity,
    0
  );

  return (
    <div>
      <OrderDetails
        orderDate={orderDate}
        orderNumber={orderNumber}
        orderStatus={orderStatus}
        orderItems={orderItems}
        shippingCost={shippingCost}
        table={table}
        total={total}
        billingAddress={billingAddress}
        shippingAddress={shippingAddress}
      />
      {orderData?.status === ORDER_STATUSES.PENDING && (
        <div
          className="d-flex justify-content-end mt-3 container"
          style={{ maxWidth: "800px" }}
        >
          {orderStatus !== "confirmed" && (
            <button
              className="btn btn-success me-2"
              onClick={() => showConfirmModal(ORDER_ACTIONS.CONFIRM)}
              disabled={processLoading}
            >
              Confirm Order
            </button>
          )}
          {orderStatus !== "cancelled" && (
            <button
              className="btn btn-danger"
              onClick={() => showConfirmModal(ORDER_ACTIONS.CANCEL)}
              disabled={processLoading}
            >
              Cancel Order
            </button>
          )}
        </div>
      )}
    </div>
  );
}

export default AdminOrderDetailsPage;
