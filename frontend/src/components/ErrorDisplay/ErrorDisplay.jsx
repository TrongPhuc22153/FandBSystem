import styles from './ErrorDisplay.module.css';

export default function ErrorDisplay({ message }) {
  return <div className={styles["error-display"]}>Error: {message}</div>;
}
