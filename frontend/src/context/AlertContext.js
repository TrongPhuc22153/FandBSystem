import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import { Alert } from "react-bootstrap";

const AlertContext = createContext();

const AlertProvider = ({ children }) => {
  const [alerts, setAlerts] = useState([]);
  const timers = {};

  const showNewAlert = useCallback(
    ({ message, action, variant = "success", duration = 5000 }) => {
      const newAlert = { id: Date.now(), action, message, variant, duration };
      setAlerts([...alerts, newAlert]);
    },
    []
  );

  const removeAlert = (id) => {
    setAlerts((prevAlerts) => prevAlerts.filter((alert) => alert.id !== id));
    if (timers[id]) {
      clearTimeout(timers[id]);
      delete timers[id];
    }
  };

  useEffect(() => {
    alerts.forEach((alert) => {
      if (!timers[alert.id]) {
        timers[alert.id] = setTimeout(() => {
          if (alert.action) {
            alert.action();
          }
          removeAlert(alert.id);
        }, alert.duration);
      }
    });

    return () => {
      Object.values(timers).forEach(clearTimeout);
    };
  }, [alerts, removeAlert]);

  return (
    <AlertContext.Provider
      value={{
        alerts,
        showNewAlert,
        removeAlert,
      }}
    >
      {children}
      <div className="alert-position">
        {alerts.map((alert) => (
          <Alert
            key={alert.id}
            variant={alert.variant}
            onClose={() => removeAlert(alert.id)}
            dismissible
          >
            {alert.message}
          </Alert>
        ))}
      </div>
    </AlertContext.Provider>
  );
};

export default AlertProvider;

export const useAlert = () => useContext(AlertContext);
