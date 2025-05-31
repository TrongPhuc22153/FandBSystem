import { useState } from "react";
import MenuSelection from "../../components/MenuSelection/MenuSelection";
import Confirmation from "../../components/Confirmation/Confirmation";
import ReservationForm from "../../components/ReservationForm/ReservationForm"
import styles from "./ReservationPage.module.css";
import { RESERVATION_TIME } from "../../constants/webConstant";

export default function ReservationFormPage() {
  // Current step in the reservation process
  const [currentStep, setCurrentStep] = useState(1);

  // Initialize reservation data with defaults
  const [reservationData, setReservationData] = useState(() => {
    const now = new Date();
    const today = now.toISOString().slice(0, 10); // YYYY-MM-DD format

    // Set default startTime to the next available hour within 10:00-21:00
    let defaultStartHour = now.getHours() + 1;
    if (defaultStartHour < 10) defaultStartHour = 10; // Enforce MIN_TIME
    if (defaultStartHour > 21) defaultStartHour = 21; // Enforce RESERVATION_TIME.MAX_TIME
    const defaultStartTime = `${defaultStartHour.toString().padStart(2, '0')}:00`;

    // Set default endTime to 2 hours after startTime, capped at RESERVATION_TIME.MAX_TIME
    const defaultEndDateTime = new Date(now);
    defaultEndDateTime.setHours(defaultStartHour + RESERVATION_TIME.DEFAULT_DURATION_HOURS);
    let defaultEndTime = defaultEndDateTime.toTimeString().slice(0, 5);
    if (defaultEndTime > RESERVATION_TIME.MAX_TIME) defaultEndTime = RESERVATION_TIME.MAX_TIME;

    return {
      date: new Date(today),
      startTime: defaultStartTime,
      endTime: defaultEndTime,
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
