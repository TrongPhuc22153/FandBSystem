import { createContext, useCallback, useContext, useEffect, useRef, useState } from "react";
import { Alert } from "react-bootstrap";


const AlertContext = createContext(null);

const AlertProvider = ({ children }) => {
  const [alerts, setAlerts] = useState([]);
  const timers = useRef({});

  const showNewAlert = useCallback(
    ({ message, action, variant = "success", duration = 5000 }) => {
      setAlerts((prev) => [
        ...prev,
        { id: Date.now(), action, message, variant, duration },
      ]);
    },
    []
  );

  const removeAlert = useCallback((id) => {
    setAlerts((prevAlerts) => prevAlerts.filter((alert) => alert.id !== id));
    if (timers.current[id]) {
      clearTimeout(timers.current[id]);
      delete timers.current[id];
    }
  }, []);

  useEffect(() => {
    Object.values(timers.current).forEach(clearTimeout);
    timers.current = {};

    alerts.forEach((alert) => {
      if (!timers.current[alert.id]) {
        timers.current[alert.id] = setTimeout(() => {
          if (alert.action) {
            alert.action();
          }
          removeAlert(alert.id);
        }, alert.duration);
      }
    });

    return () => {
      Object.values(timers.current).forEach(clearTimeout);
    };
  }, [alerts, removeAlert]);

  return (
    <AlertContext.Provider value={{ alerts, showNewAlert, removeAlert }}>
      {children}
      <div style={{ position: 'fixed', top: 20, right: 20, zIndex: 1000 }}>
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

export const useAlert = () => {
  return useContext(AlertContext);
};