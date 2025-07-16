import { useParams } from "react-router-dom";
import {
  useReservation,
} from "../../hooks/reservationHooks";
import Loading from "../../shared/components/Loading/Loading";
import ErrorDisplay from "../../shared/components/ErrorDisplay/ErrorDisplay";
import ReservationDetails from "../../components/ReservationDetails/ReservationDetails";

function AdminReservationDetailsPage() {
  const { id } = useParams();
  const {
    data: reservationData,
    isLoading: loadingReservation,
    error: reservationError,
  } = useReservation({ reservationId: id });

  if (loadingReservation) {
    return <Loading />;
  }

  if (reservationError?.message) {
    return (
      <ErrorDisplay
        message={reservationError.message}
      />
    );
  }

  return (
    <div className="d-flex justify-content-center p-4">
      <ReservationDetails reservation={reservationData} />
    </div>
  );
}

export default AdminReservationDetailsPage;
