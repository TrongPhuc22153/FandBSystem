import styles from "./StepIndicator.module.css"


export default function StepIndicator({ currentStep }) {
  const steps = [
    { number: 1, title: "Reservation Details" },
    { number: 2, title: "Product Selection" },
    { number: 3, title: "Overview & Payment" },
  ]

  return (
    <div className={styles.container}>
      {steps.map((step, index) => (
        <div key={step.number} className={styles.stepWrapper}>
          <div className={styles.step}>
            <div className={`${styles.stepNumber} ${currentStep >= step.number ? styles.active : ""}`}>
              {currentStep > step.number ? "✓" : step.number}
            </div>
            <span className={`${styles.stepTitle} ${currentStep >= step.number ? styles.activeTitle : ""}`}>
              {step.title}
            </span>
          </div>
          {index < steps.length - 1 && (
            <div className={`${styles.connector} ${currentStep > step.number ? styles.activeConnector : ""}`} />
          )}
        </div>
      ))}
    </div>
  )
}
