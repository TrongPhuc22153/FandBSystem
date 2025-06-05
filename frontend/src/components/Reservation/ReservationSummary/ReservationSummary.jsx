
import { getImageSrc } from "../../../utils/imageUtils"
import styles from "./ReservationSummary.module.css"

export default function ReservationSummary({ data }) {
  const formatDate = (dateString) => {
    if (!dateString) return "Not selected"
    return new Date(dateString).toLocaleDateString("en-US", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  const formatTime = (timeString) => {
    if (!timeString) return "Not selected"
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
  const tax = subtotal * 0.08 // 8% tax
  const total = subtotal + tax

  return (
    <div className={styles.container}>
      <h3 className={styles.title}>Reservation Summary</h3>

      <div className={styles.section}>
        <h4 className={styles.sectionTitle}>Details</h4>
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
          <span className={styles.value}>{data.guests}</span>
        </div>
        <div className={styles.detail}>
          <span className={styles.label}>Table:</span>
          <span className={styles.value}>
            {data.autoAssignTable ? "Auto-assign" : data.selectedTable || "Not selected"}
          </span>
        </div>
        {data.notes && (
          <div className={styles.detail}>
            <span className={styles.label}>Notes:</span>
            <span className={styles.value}>{data.notes}</span>
          </div>
        )}
      </div>

      {data.menuItems.length > 0 && (
        <div className={styles.section}>
          <h4 className={styles.sectionTitle}>Selected Items</h4>
          <div className={styles.products}>
            {data.menuItems.map((product) => (
              <div key={product.productId} className={styles.product}>
                <img src={getImageSrc(product.picture)} alt={product.productName} className={styles.productImage} />
                <div className={styles.productInfo}>
                  <div className={styles.productName}>{product.productName}</div>
                  <div className={styles.productQuantity}>Qty: {product.quantity}</div>
                </div>
                <div className={styles.productPrice}>${(product.unitPrice * product.quantity).toFixed(2)}</div>
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
    </div>
  )
}
