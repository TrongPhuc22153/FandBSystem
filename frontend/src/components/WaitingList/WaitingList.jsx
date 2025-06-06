import { useCallback, useEffect, useState } from "react";
import { Clock, Plus, Trash, User, Users } from "lucide-react";
import { useModal } from "../../context/ModalContext";
import styles from "./WaitingList.module.css";
import { formatDate } from "../../utils/datetimeUtils";
import { useTableOccupancyActions } from "../../hooks/tableOccupancyHooks";
import { useAlert } from "../../context/AlertContext";
import { TABLE_OCCUPANCY_STATUSES, TABLE_OCCUPANCY_TYPES } from "../../constants/webConstant";

export function WaitingList({ waitingList, mutate }) {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [newCustomer, setNewCustomer] = useState({
    name: "",
    size: 2,
    phone: "",
    notes: "",
  });
  const [editingCustomerId, setEditingCustomerId] = useState(null);
  const [fieldErrors, setFieldErrors] = useState({});

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  const {
    handleCreateTableOccupancy,
    createLoading,
    createSuccess,
    createError,
    resetCreate,

    handleUpdateTableOccupancyStatus,
    updateStatusError,
    updateStatusSuccess,
    resetUpdateStatus,
    
    handleUpdateTableOccupancy,
    updateError,
    updateSuccess,
    resetUpdate,
  } = useTableOccupancyActions();

  useEffect(() => {
    setFieldErrors(createError?.fields ?? updateError?.fields ?? {});
    if (createError?.message || updateError?.message || updateStatusError?.message) {
      showNewAlert({
        message: createError?.message || updateError?.message || updateStatusError?.message,
        variant: "danger",
      });
    }
  }, [createError, updateError, updateStatusError, showNewAlert]);

  useEffect(() => {
    if (updateStatusSuccess) {
      showNewAlert({
        message: updateStatusSuccess,
        action: resetUpdateStatus,
      });
    }
    if (createSuccess) {
      showNewAlert({
        message: createSuccess,
        action: resetCreate,
      });
    }
    if (updateSuccess) {
      showNewAlert({
        message: updateSuccess,
        action: resetUpdate,
      });
    }
  }, [
    createSuccess,
    updateSuccess,
    updateStatusSuccess,
    showNewAlert,
    resetCreate,
    resetUpdate,
    resetUpdateStatus,
  ]);

  const handleCustomerInWaitingList = useCallback(async () => {
    const errors = {};
    if (!newCustomer.name) {
      errors.contactName = ["Customer name is required"];
    }
    if (newCustomer.size <= 0) {
      errors.partySize = ["Party size must be greater than 0"];
    }
    if (!newCustomer.phone) {
      errors.phone = ["Phone number is required"];
    } else if (!/^[0-9\-+]{9,15}$/.test(newCustomer.phone)) {
      errors.phone = [
        "Phone number must be 9-15 characters and contain only digits, hyphens, or plus signs",
      ];
    }

    if (Object.keys(errors).length > 0) {
      setFieldErrors(errors);
      showNewAlert({
        message: "Please fix the errors in the form",
        variant: "danger",
      });
      return;
    }

    const requestCustomer = {
      contactName: newCustomer.name,
      partySize: newCustomer.size,
      phone: newCustomer.phone,
      notes: newCustomer.notes,
      type: TABLE_OCCUPANCY_TYPES.WALK_IN
    };

    try {
      if (editingCustomerId) {
        const res = await handleUpdateTableOccupancy(editingCustomerId, requestCustomer);
        if (res) {
          mutate(
            (prevData) => ({
              ...prevData,
              content: prevData.content.map((customer) =>
                customer.id === editingCustomerId ? { ...customer, ...res } : customer
              ),
            }),
            false
          );
          mutate();
        }
      } else {
        const res = await handleCreateTableOccupancy(requestCustomer);
        if (res) {
          mutate(
            (prevData) => ({
              ...prevData,
              content: [...(prevData?.content || []), res],
            }),
            false
          );
          mutate();
        }
      }
      setNewCustomer({ name: "", size: 2, phone: "", notes: "" });
      setFieldErrors({});
      setIsDialogOpen(false);
      setEditingCustomerId(null);
    } catch (error) {
      showNewAlert({
        message: editingCustomerId ? "Failed to update customer. Please try again." : "Failed to add customer. Please try again.",
        variant: "danger",
      });
    }
  }, [handleCreateTableOccupancy, handleUpdateTableOccupancy, newCustomer, showNewAlert, mutate, editingCustomerId]);

  const showCustomerConfirmModal = () => {
    onOpen({
      title: editingCustomerId ? "Update customer" : "Add customer",
      message: editingCustomerId ? "Do you want to update this customer?" : "Do you want to add new customer?",
      onYes: handleCustomerInWaitingList,
    });
  };

  const handleRemoveCustomer = useCallback(
    async (idToDelete) => {
      const res = await handleUpdateTableOccupancyStatus(
        idToDelete,
        { status: TABLE_OCCUPANCY_STATUSES.CANCELED }
      );
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
      }
    },
    [handleUpdateTableOccupancyStatus, mutate]
  );

  const showRemoveCustomerConfirmModal = (id) => {
    onOpen({
      title: "Remove customer",
      message: "Do you want to remove this customer?",
      onYes: () => handleRemoveCustomer(id),
    });
  };

  const handleEditCustomer = useCallback((customer) => {
    setNewCustomer({
      name: customer.contactName,
      size: customer.partySize,
      phone: customer.phone,
      notes: customer.notes || "",
    });
    setEditingCustomerId(customer.id);
    setIsDialogOpen(true);
  }, []);

  return (
    <div className={styles.container}>
      <button
        className={styles.addButton}
        onClick={() => {
          setNewCustomer({ name: "", size: 2, phone: "", notes: "" });
          setEditingCustomerId(null);
          setIsDialogOpen(true);
        }}
      >
        <Plus size={16} className={styles.plusIcon} />
        Add to Waiting List
      </button>

      <div className={styles.waitingListContainer}>
        {waitingList.length === 0 ? (
          <div className={styles.emptyState}>No customers waiting</div>
        ) : (
          waitingList.map((customer) => (
            <div
              key={customer.id}
              className={styles.customerCard}
              onClick={() => handleEditCustomer(customer)}
              style={{ cursor: "pointer" }}
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

      {isDialogOpen && (
        <div
          className={styles.modalOverlay}
          onClick={() => {
            setIsDialogOpen(false);
            setEditingCustomerId(null);
            setNewCustomer({ name: "", size: 2, phone: "", notes: "" });
          }}
        >
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalHeader}>
              <h2 className={styles.modalTitle}>
                {editingCustomerId ? "Edit Customer" : "Add to Waiting List"}
              </h2>
              <button
                className={styles.closeButton}
                onClick={() => {
                  setIsDialogOpen(false);
                  setEditingCustomerId(null);
                  setNewCustomer({ name: "", size: 2, phone: "", notes: "" });
                }}
              >
                ×
              </button>
            </div>
            <div className={styles.modalBody}>
              <div className={styles.formGroup}>
                <label htmlFor="contactName" className={styles.formLabel}>
                  Customer Name
                </label>
                <input
                  type="text"
                  id="contactName"
                  className={styles.formInput}
                  placeholder="Customer name..."
                  value={newCustomer.name}
                  onChange={(e) =>
                    setNewCustomer({ ...newCustomer, name: e.target.value })
                  }
                />
                {fieldErrors?.contactName &&
                  fieldErrors.contactName.map((error, index) => (
                    <div key={index} className="error-message text-danger">
                      {error}
                    </div>
                  ))}
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
                {fieldErrors?.partySize &&
                  fieldErrors.partySize.map((error, index) => (
                    <div key={index} className="error-message text-danger">
                      {error}
                    </div>
                  ))}
              </div>
              <div className={styles.formGroup}>
                <label htmlFor="phone" className={styles.formLabel}>
                  Phone Number
                </label>
                <input
                  type="text"
                  id="phone"
                  className={styles.formInput}
                  placeholder="e.g., +123456789 or 123-456-7890"
                  value={newCustomer.phone}
                  onChange={(e) =>
                    setNewCustomer({ ...newCustomer, phone: e.target.value })
                  }
                />
                {fieldErrors?.phone &&
                  fieldErrors.phone.map((error, index) => (
                    <div key={index} className="error-message text-danger">
                      {error}
                    </div>
                  ))}
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
                {fieldErrors?.notes &&
                  fieldErrors.notes.map((error, index) => (
                    <div key={index} className="error-message text-danger">
                      {error}
                    </div>
                  ))}
              </div>
            </div>
            <div className={styles.modalFooter}>
              <button
                className={styles.cancelButton}
                onClick={() => {
                  setIsDialogOpen(false);
                  setEditingCustomerId(null);
                  setNewCustomer({ name: "", size: 2, phone: "", notes: "" });
                }}
              >
                Cancel
              </button>
              <button
                className={styles.submitButton}
                onClick={showCustomerConfirmModal}
                disabled={createLoading}
              >
                {editingCustomerId ? "Update" : "Add to List"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}