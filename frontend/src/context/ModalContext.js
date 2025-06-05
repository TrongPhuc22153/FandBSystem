import { createContext, useCallback, useContext, useState } from "react";
import { Button, Modal } from "react-bootstrap";

const ModalContext = createContext();

const ModalProvider = ({ children }) => {
  const [isOpened, setIsOpened] = useState(false);
  const [message, setMessage] = useState(null);
  const [title, setTitle] = useState(null);
  const [onYes, setOnYes] = useState(null);
  const [onNo, setOnNo] = useState(null);

  const onOpen = useCallback(({ message, title, onYes, onNo }) => {
    setIsOpened(true);
    setMessage(message);
    setTitle(title);
    setOnYes(() => onYes);
    setOnNo(() => onNo);
  }, []);

  const onClose = useCallback(() => {
    setIsOpened(false);
    setMessage(null);
    setTitle(null);
    setOnYes(null);
  }, []);

  const handleNo = useCallback(() => {
    if (onNo) {
      onNo();
    }
    onClose();
  }, [onNo, onClose]);

  const handleYes = useCallback(() => {
    if (onYes) {
      onYes();
    }
    handleNo();
  }, [onYes, handleNo]);

  return (
    <ModalContext.Provider
      value={{
        isOpened,
        message,
        title,
        onOpen,
        onClose,
        onYes,
        handleYes,
        handleNo,
      }}
    >
      {children}
      <Modal show={isOpened} onHide={() => handleNo()}>
        <Modal.Header closeButton>
          <Modal.Title>{title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>{message}</Modal.Body>
        <Modal.Footer>
          <Button variant="success" onClick={() => handleYes()}>
            Yes
          </Button>
          <Button variant="danger" onClick={() => handleNo()}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </ModalContext.Provider>
  );
};

export default ModalProvider;

export const useModal = () => useContext(ModalContext);
