import { useState } from "react";
import { Clock, Plus, Trash, User, Users } from "lucide-react";
import styles from "./WaitingList.module.css";

export function WaitingList({
  waitingList,
  removeFromWaitingList,
  addToWaitingList,
}) {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [newCustomer, setNewCustomer] = useState({
    name: "",
    size: 2,
    phone: "",
    notes: "",
  });

  const handleAddCustomer = () => {
    if (newCustomer.name && newCustomer.size > 0 && newCustomer.phone) {
      addToWaitingList(newCustomer);
      setNewCustomer({
        name: "",
        size: 2,
        phone: "",
        notes: "",
      });
      setIsDialogOpen(false);
    }
  };

  return (
    <div className={styles.container}>
      <button
        className={styles.addButton}
        onClick={() => setIsDialogOpen(true)}
      >
        <Plus size={16} className={styles.plusIcon} />
        Add to Waiting List
      </button>

      <div className={styles.waitingListContainer}>
        {waitingList.length === 0 ? (
          <div className={styles.emptyState}>No customers waiting</div>
        ) : (
          waitingList.map((customer) => (
            <div key={customer.id} className={styles.customerCard}>
              <div className={styles.cardHeader}>
                <div>
                  <h4 className={styles.customerName}>{customer.name}</h4>
                  <div className={styles.partySize}>
                    <Users size={14} className={styles.icon} />
                    <span>Party of {customer.size}</span>
                  </div>
                </div>
                <button
                  className={styles.removeButton}
                  onClick={() => removeFromWaitingList(customer.id)}
                >
                  <Trash size={16} />
                  <span className={styles.srOnly}>Remove</span>
                </button>
              </div>
              <div className={styles.waitingTime}>
                <Clock size={14} className={styles.icon} />
                <span>Waiting since {customer.waitingSince}</span>
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

      {isDialogOpen && (
        <div
          className={styles.modalOverlay}
          onClick={() => setIsDialogOpen(false)}
        >
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalHeader}>
              <h2 className={styles.modalTitle}>Add to Waiting List</h2>
              <button
                className={styles.closeButton}
                onClick={() => setIsDialogOpen(false)}
              >
                &times;
              </button>
            </div>
            <div className={styles.modalBody}>
              <div className={styles.formGroup}>
                <label htmlFor="customerName" className={styles.formLabel}>
                  Customer Name
                </label>
                <input
                  type="text"
                  id="customerName"
                  className={styles.formInput}
                  placeholder="Smith"
                  value={newCustomer.name}
                  onChange={(e) =>
                    setNewCustomer({ ...newCustomer, name: e.target.value })
                  }
                />
              </div>
              <div className={styles.formGroup}>
                <label htmlFor="partySize" className={styles.formLabel}>
                  Party Size
                </label>
                <input
                  type="number"
                  id="partySize"
                  className={styles.formInput}
                  min="1"
                  value={newCustomer.size}
                  onChange={(e) =>
                    setNewCustomer({
                      ...newCustomer,
                      size: parseInt(e.target.value),
                    })
                  }
                />
              </div>
              <div className={styles.formGroup}>
                <label htmlFor="phoneNumber" className={styles.formLabel}>
                  Phone Number
                </label>
                <input
                  type="text"
                  id="phoneNumber"
                  className={styles.formInput}
                  placeholder="555-1234"
                  value={newCustomer.phone}
                  onChange={(e) =>
                    setNewCustomer({ ...newCustomer, phone: e.target.value })
                  }
                />
              </div>
              <div className={styles.formGroup}>
                <label htmlFor="notes" className={styles.formLabel}>
                  Notes (Optional)
                </label>
                <textarea
                  id="notes"
                  className={styles.formTextarea}
                  rows={3}
                  placeholder="Special requests or notes"
                  value={newCustomer.notes}
                  onChange={(e) =>
                    setNewCustomer({ ...newCustomer, notes: e.target.value })
                  }
                ></textarea>
              </div>
            </div>
            <div className={styles.modalFooter}>
              <button
                className={styles.cancelButton}
                onClick={() => setIsDialogOpen(false)}
              >
                Cancel
              </button>
              <button
                className={styles.submitButton}
                onClick={handleAddCustomer}
              >
                Add to List
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
