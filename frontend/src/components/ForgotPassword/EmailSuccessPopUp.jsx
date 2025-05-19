import { Modal, Button } from 'react-bootstrap';
import styles from './EmailSuccessPopUp.module.css';

const EmailSuccessPopup = ({ show, onClose }) => {
  return (
    <Modal
      show={show}
      onHide={onClose}
      centered
      backdrop="static"
      keyboard={false}
      className={styles.modal}
    >
      <Modal.Body className={styles.modalContent}>
        <div className={styles.modalHeader}>
          <span className={styles.successIcon}>✔</span>
        </div>
        <div className={styles.modalBody}>
          <h4 className={styles.modalTitle}>Email Sent Successfully!</h4>
          <p className={styles.modalText}>Your message has been sent. We'll get back to you soon!</p>
        </div>
        <div className={styles.modalFooter}>
          <Button variant="primary" onClick={onClose}>
            Close
          </Button>
        </div>
      </Modal.Body>
    </Modal>
  );
};

export default EmailSuccessPopup;