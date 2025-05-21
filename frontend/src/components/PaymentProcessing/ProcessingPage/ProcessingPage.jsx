import styles from './ProcessingPage.module.css';

function ProcessingPage() {
  return (
    <div className={`center ${styles.card} ${styles['processing-card']}`}>
      <div className={styles['icon-container']}>
        <div className={styles.spinner}></div>
      </div>
      <h2 className={styles['heading-primary']}>Processing Payment...</h2>
      <p className={styles['status-paragraph']}>
        Please do not close this window. Your transaction is being processed.
      </p>
    </div>
  );
}

export default ProcessingPage;
