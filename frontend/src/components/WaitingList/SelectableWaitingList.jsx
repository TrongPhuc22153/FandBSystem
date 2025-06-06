import { useCallback, useEffect } from "react";
import { Clock, Trash, User, Users } from "lucide-react";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";
import { TABLE_OCCUPANCY_STATUSES } from "../../constants/webConstant";
import styles from "./WaitingList.module.css";
import { formatDate } from "../../utils/datetimeUtils";
import { useTableOccupancyActions } from "../../hooks/tableOccupancyHooks";

export default function SelectableWaitingList({
  waitingList,
  mutate,
  selectedCustomer,
  setSelectedCustomer,
}) {
  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  const {
    handleUpdateTableOccupancyStatus,
    updateStatusError,
    updateStatusSuccess,
    resetUpdateStatus,
  } = useTableOccupancyActions();

  useEffect(() => {
    if (updateStatusError?.message) {
      showNewAlert({
        message: updateStatusError.message,
        variant: "danger",
      });
    }
  }, [updateStatusError, showNewAlert]);

  useEffect(() => {
    if (updateStatusSuccess) {
      showNewAlert({
        message: updateStatusSuccess,
        action: resetUpdateStatus,
      });
    }
  }, [updateStatusSuccess, showNewAlert, resetUpdateStatus]);

  const handleSelectCustomer = useCallback(
    (customer) => {
      setSelectedCustomer((prev) =>
        prev?.id === customer.id ? null : customer
      );
    },
    [setSelectedCustomer]
  );

  const handleRemoveCustomer = useCallback(
    async (idToDelete) => {
      const res = await handleUpdateTableOccupancyStatus(idToDelete, {
        status: TABLE_OCCUPANCY_STATUSES.CANCELED,
      });
      if (res) {
        mutate((prevData) => {
          if (!prevData?.content) return prevData;
          return {
            ...prevData,
            content: prevData.content.filter(
              (waitingList) => waitingList.id !== idToDelete
            ),
          };
        }, false);
        mutate();
        if (selectedCustomer?.id === idToDelete) {
          setSelectedCustomer(null);
        }
      }
    },
    [handleUpdateTableOccupancyStatus, mutate, selectedCustomer, setSelectedCustomer]
  );

  const showRemoveCustomerConfirmModal = (id) => {
    onOpen({
      title: "Remove customer",
      message: "Do you want to remove this customer?",
      onYes: () => handleRemoveCustomer(id),
    });
  };

  return (
    <div className={styles.container}>
      <div className={styles.waitingListContainer}>
        {waitingList.length === 0 ? (
          <div className={styles.emptyState}>No customers waiting</div>
        ) : (
          waitingList.map((customer) => (
            <div
              key={customer.id}
              className={`${styles.customerCard} ${
                selectedCustomer?.id === customer.id ? styles.selected : ""
              }`}
              onClick={() => handleSelectCustomer(customer)}
            >
              <div className={styles.cardHeader}>
                <div>
                  <h4 className={styles.customerName}>
                    {customer.contactName}
                  </h4>
                  <div className={styles.partySize}>
                    <Users size={14} className={styles.icon} />
                    <span>Party of {customer.partySize}</span>
                  </div>
                </div>
                <button
                  className={styles.removeButton}
                  onClick={(e) => {
                    e.stopPropagation();
                    showRemoveCustomerConfirmModal(customer.id);
                  }}
                >
                  <Trash size={16} />
                  <span className={styles.srOnly}>Remove</span>
                </button>
              </div>
              <div className={styles.waitingTime}>
                <Clock size={14} className={styles.icon} />
                <span>Waiting since {formatDate(customer.createdAt)}</span>
              </div>
              <div className={styles.phoneNumber}>
                <User size={14} className={styles.icon} />
                <span>{customer.phone}</span>
              </div>
              {customer.notes && (
                <div className={styles.notes}>{customer.notes}</div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
