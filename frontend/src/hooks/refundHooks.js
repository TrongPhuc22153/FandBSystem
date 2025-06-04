import { fetchOrderRefundPreview, fetchReservationRefundPreview } from "../api/refundApi";
import { useAuth } from "../context/AuthContext";


export const useRefundActions = () => {
  const { token } = useAuth();

  const fetchOrderPreview = async (orderId) => {
    return fetchOrderRefundPreview(orderId, token);
  };

  const fetchReservationPreview = async (reservationId) => {
    return fetchReservationRefundPreview(reservationId, token);
  };

  return {
    fetchOrderPreview,
    fetchReservationPreview,
  };
}