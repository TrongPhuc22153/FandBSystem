
import { useMemo } from "react";
import { PAYMENT_TYPES } from "../../../constants/paymentConstants";
import { usePaymentMethods } from "../../../hooks/paymentMethodHooks";
import ErrorDisplay from "../../ErrorDisplay/ErrorDisplay";
import styles from "./Step3Overview.module.css"
import { getImageSrc } from "../../../utils/imageUtils";
import { useModal } from "../../../context/ModalContext";

export default function Step3Overview({ data, updateData, onBack, onSubmit, onEdit }) {
  const {
    data: paymentMethodsData,
    error: methodsError,
  } = usePaymentMethods(PAYMENT_TYPES.RESERVATION);

  const paymentMethods = useMemo(
    () => paymentMethodsData || [],
    [paymentMethodsData]
  );

  const { onOpen } = useModal();

  const handleConfirmReservation = async (event) => {
    event.preventDefault();
    onOpen({
      title: "Submit Reservation",
      message: "Do you want to confirm your reservation?",
      onYes: () => onSubmit()
    })
  }

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  const formatTime = (timeString) => {
    return new Date(`2000-01-01T${timeString}`).toLocaleTimeString("en-US", {
      hour: "numeric",
      minute: "2-digit",
      hour12: true,
    })
  }

  const calculateSubtotal = () => {
    return data.menuItems.reduce((total, product) => {
      return total + product.unitPrice * product.quantity
    }, 0)
  }

  const subtotal = calculateSubtotal()
  const tax = subtotal * 0.08
  const total = subtotal + tax

  if (methodsError?.message) {
    return <ErrorDisplay message={methodsError.message} />
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>Review Your Reservation</h2>
        <p>Please review all details before confirming</p>
      </div>

      <div className={styles.content}>
        <div className={styles.section}>
          <div className={styles.sectionHeader}>
            <h3>Reservation Details</h3>
            <button onClick={() => onEdit(1)} className={styles.editButton}>
              Edit
            </button>
          </div>
          <div className={styles.detailsGrid}>
            <div className={styles.detail}>
              <span className={styles.label}>Date:</span>
              <span className={styles.value}>{formatDate(data.date)}</span>
            </div>
            <div className={styles.detail}>
              <span className={styles.label}>Time:</span>
              <span className={styles.value}>
                {formatTime(data.startTime)} - {formatTime(data.endTime)}
              </span>
            </div>
            <div className={styles.detail}>
              <span className={styles.label}>Guests:</span>
              <span className={styles.value}>{data.guests} people</span>
            </div>
            <div className={styles.detail}>
              <span className={styles.label}>Table:</span>
              <span className={styles.value}>{data.autoAssignTable ? "Auto-assign" : data.selectedTable}</span>
            </div>
            {data.notes && (
              <div className={styles.detail}>
                <span className={styles.label}>Notes:</span>
                <span className={styles.value}>{data.notes}</span>
              </div>
            )}
          </div>
        </div>

        {data.menuItems.length > 0 && (
          <div className={styles.section}>
            <div className={styles.sectionHeader}>
              <h3>Selected Items</h3>
              <button onClick={() => onEdit(2)} className={styles.editButton}>
                Edit
              </button>
            </div>
            <div className={styles.productsGrid}>
              {data.menuItems.map((product) => (
                <div key={product.productId} className={styles.productCard}>
                  <img src={getImageSrc(product.picture)} alt={product.productName} className={styles.productImage} />
                  <div className={styles.productInfo}>
                    <h4 className={styles.productName}>{product.productName}</h4>
                    <div className={styles.productDetails}>
                      <span className={styles.quantity}>Qty: {product.quantity}</span>
                      <span className={styles.price}>${(product.unitPrice * product.quantity).toFixed(2)}</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <div className={styles.totals}>
              <div className={styles.totalRow}>
                <span>Subtotal:</span>
                <span>${subtotal.toFixed(2)}</span>
              </div>
              <div className={styles.totalRow}>
                <span>Tax (8%):</span>
                <span>${tax.toFixed(2)}</span>
              </div>
              <div className={`${styles.totalRow} ${styles.finalTotal}`}>
                <span>Total:</span>
                <span>${total.toFixed(2)}</span>
              </div>
            </div>
          </div>
        )}

        <div className={styles.section}>
          <h3>Payment Method</h3>
          <div className={styles.paymentMethods}>
            {paymentMethods.map((method) => (
              <label key={method.methodId} className={styles.paymentMethod}>
                <input
                  type="radio"
                  name="paymentMethod"
                  value={method.methodName}
                  checked={data.paymentMethod === method.methodName}
                  onChange={(e) => updateData({ paymentMethod: e.target.value })}
                />
                <div className={styles.paymentMethodContent}>
                  <span className={styles.paymentIcon}>{method.icon}</span>
                  <span className={styles.paymentName}>{method.methodName}</span>
                </div>
              </label>
            ))}
          </div>
        </div>
      </div>

      <div className={styles.actions}>
        <button onClick={onBack} className={styles.backButton}>
          Back
        </button>
        <button onClick={handleConfirmReservation} className={styles.confirmButton}>
          Confirm Reservation
        </button>
      </div>
    </div>
  )
}
