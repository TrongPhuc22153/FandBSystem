import styles from "./ReservationCard.module.css";

const ReservationCard = ({ reservation, onMarkPrepared }) => {
  return (
    <div className={styles.card}>
      <div className={styles.cardContent}>
        <h3 className="font-semibold">{reservation.customer}</h3>
        <p>Time: {reservation.time}</p>
        <p>Guests: {reservation.guests}</p>
        <p>Status: {reservation.status}</p>
      </div>
      {reservation.status === "Pending" && (
        <button
          className={styles.button}
          onClick={() => onMarkPrepared(reservation.id)}
        >
          Mark as Prepared
        </button>
      )}
    </div>
  );
};
export default ReservationCard;
