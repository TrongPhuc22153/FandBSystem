import styles from './ReservationList.module.css';

const ReservationList = ({ reservations, selectedItemId, selectedItemType, onReservationSelect, onMarkPrepared }) => {
    return (
        <div className={styles.listGroup}>
            <h3 className="text-lg font-semibold mb-2">Reservations</h3>
            {reservations.length === 0 ? (
                <p className={styles.textMuted}>No reservations found.</p>
            ) : (
                reservations.map(reservation => (
                    <button
                        key={reservation.id}
                        type="button"
                        className={`${styles.listItem} ${selectedItemId === reservation.id && selectedItemType === 'reservation' ? styles.active : ''}`}
                        onClick={() => onReservationSelect(reservation)}
                    >
                        <span>Reservation #{reservation.id} - {reservation.time}</span>
                        {reservation.status === 'Pending' && (
                            <button
                                className={styles.button}
                                onClick={(e) => { e.stopPropagation(); onMarkPrepared(reservation.id); }}
                            >
                                Mark as Prepared
                            </button>
                        )}
                    </button>
                ))
            )}
        </div>
    );
};

export default ReservationList;