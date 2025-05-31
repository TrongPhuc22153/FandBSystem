import { useParams } from "react-router-dom";
import {
  useReservation,
} from "../../hooks/reservationHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
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
    <div
      style={{
        position: "absolute",
        top: "12%",
        left: "50%",
        transform: "translate(-50%, 0)",
      }}
    >
      <div className="shadow rounded">
        <ReservationDetails reservation={reservationData} />
      </div>
    </div>
  );
}

export default AdminReservationDetailsPage;
