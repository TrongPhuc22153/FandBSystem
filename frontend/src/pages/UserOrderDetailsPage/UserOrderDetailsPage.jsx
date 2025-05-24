import { useOrder } from "../../hooks/orderHooks";
import { useParams } from "react-router-dom";
import { formatDate } from "../../utils/datetimeUtils";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import OrderDetail from "../../components/OrderDetails/OrderDetails";

function UserOrderDetailsPage() {
  const { id } = useParams();
  const {
    data: orderData,
    isLoading: loadingOrder,
    error: orderError,
  } = useOrder({ orderId: id });

  if (loadingOrder) {
    return <Loading />;
  }

  if (orderError?.message) {
    return <ErrorDisplay message={orderError.message} />;
  }

  const orderDate = formatDate(orderData.orderDate);
  const orderNumber = orderData.orderId;
  const orderStatus = orderData.status.toLowerCase();
  const shippingCost = orderData.shippingCost || 0;
  const shippingAddress = orderData.shippingAddress;
  const orderItems = orderData.orderDetails;
  const total = orderData.totalPrice;
  const paymentMethod = orderData.payment.method;
  const paymentStatus = orderData.payment.status;

  return (
    <OrderDetail      
      orderDate={orderDate}
      orderNumber={orderNumber}
      orderStatus={orderStatus}
      orderItems={orderItems}
      shippingCost={shippingCost}
      total={total}
      shippingAddress={shippingAddress}
      paymentMethod={paymentMethod}
      paymentStatus={paymentStatus}
    />
  );
}

export default UserOrderDetailsPage;
