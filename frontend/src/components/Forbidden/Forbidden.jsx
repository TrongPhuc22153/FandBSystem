import styles from "./Forbidden.module.css"
import { ShieldX, Home, ArrowLeft } from "lucide-react"

export function Forbidden() {
  const handleGoBack = () => {
    window.history.back()
  }

  const handleGoHome = () => {
    window.location.href = "/"
  }

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <div className={styles.iconWrapper}>
          <ShieldX className={styles.icon} />
        </div>

        <div className={styles.textContent}>
          <h1 className={styles.errorCode}>403</h1>
          <h2 className={styles.title}>Access Forbidden</h2>
          <p className={styles.description}>
            You don't have permission to access this resource. Please contact your administrator if you believe this is
            an error.
          </p>
        </div>

        <div className={styles.actions}>
          <button onClick={handleGoBack} className={`${styles.button} ${styles.secondaryButton}`}>
            <ArrowLeft className={styles.buttonIcon} />
            Go Back
          </button>
          <button onClick={handleGoHome} className={`${styles.button} ${styles.primaryButton}`}>
            <Home className={styles.buttonIcon} />
            Go Home
          </button>
        </div>
      </div>
    </div>
  )
}
