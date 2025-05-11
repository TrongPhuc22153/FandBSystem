import { useState } from "react";
import ReservationForm from "../../components/ReservationForm/ReservationForm";
import MenuSelection from "../../components/MenuSelection/MenuSelection";
import Confirmation from "../../components/Confirmation/Confirmation";
import styles from "./ReservationPage.module.css";

export default function ReservationFormPage() {
  // Current step in the reservation process
  const [currentStep, setCurrentStep] = useState(1);

  // Initialize reservation data with defaults
  const [reservationData, setReservationData] = useState(() => {
    const now = new Date();
    const endTime = new Date(now);
    endTime.setHours(endTime.getHours() + 3); // Default 3 hours

    return {
      startDateTime: now,
      endDateTime: endTime,
      numberOfGuests: 2,
      tableSelection: "",
      autoSelectTable: true,
      notes: "",
      selectedItems: [],
    };
  });

  // Handle next step
  const handleNext = () => {
    setCurrentStep(currentStep + 1);
  };

  // Handle previous step
  const handlePrevious = () => {
    setCurrentStep(currentStep - 1);
  };

  // Update reservation data
  const updateReservationData = (data) => {
    setReservationData({ ...reservationData, ...data });
  };

  // Render the current step
  const renderStep = () => {
    switch (currentStep) {
      case 1:
        return (
          <ReservationForm
            reservationData={reservationData}
            updateReservationData={updateReservationData}
            onNext={handleNext}
          />
        );
      case 2:
        return (
          <MenuSelection
            reservationData={reservationData}
            updateReservationData={updateReservationData}
            onNext={handleNext}
            onPrevious={handlePrevious}
          />
        );
      case 3:
        return (
          <Confirmation
            reservationData={reservationData}
            onPrevious={handlePrevious}
          />
        );
      default:
        return null;
    }
  };

  return (
    <div className={styles.formContainer}>
      <div className="progress mb-4">
        <div
          className="progress-bar"
          role="progressbar"
          style={{ width: `${(currentStep / 3) * 100}%` }}
          aria-valuenow={(currentStep / 3) * 100}
          aria-valuemin={0}
          aria-valuemax={100}
        >
          Step {currentStep} of 3
        </div>
      </div>
      {renderStep()}
    </div>
  );
}
