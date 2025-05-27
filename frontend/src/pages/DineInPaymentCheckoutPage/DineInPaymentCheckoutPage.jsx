import { useMemo, useState } from "react";
import BillSplitting from "../../components/DineInPaymentCheckout/BillingSplitting/BillingSplitting";
import ConfirmPaymentButton from "../../components/DineInPaymentCheckout/ConfirmPaymentButton/ConfirmPaymentButton";
import OrderSummary from "../../components/DineInPaymentCheckout/OrderSummary/OrderSummary";
import PaymentOptions from "../../components/DineInPaymentCheckout/PaymentOptions/PaymentOptions";
import ReceiptRequest from "../../components/DineInPaymentCheckout/ReceiptRequest/ReceiptRequest";
import TipSelection from "../../components/DineInPaymentCheckout/TipSelection/TipSelection";
import styles from "./DineInPaymentCheckoutPage.module.css";
import { usePayments } from "../../hooks/paymentHooks";
import { PAYMENT_STATUSES } from "../../constants/webConstant";

export default function DineInPaymentCheckoutPage() {
  const [order] = useState({
    items: [
      { id: 1, name: "Spaghetti Carbonara", quantity: 1, price: 18.5 },
      { id: 2, name: "Caesar Salad", quantity: 2, price: 9.0 },
      { id: 3, name: "Coca-Cola", quantity: 3, price: 3.0 },
    ],
    subtotal: 0, // Will be calculated
    taxRate: 0.08, // 8%
    serviceChargeRate: 0.1, // 10%
  });

  const [selectedTip, setSelectedTip] = useState(0); // in percentage
  const [selectedPaymentMethod, setSelectedPaymentMethod] = useState("");
  const [billSplitOption, setBillSplitOption] = useState("none"); // 'none', 'even', 'manual'
  const [numPeopleSplit, setNumPeopleSplit] = useState(1);
  const [receiptContact, setReceiptContact] = useState({ email: "", sms: "" });
  const [isProcessingPayment, setIsProcessingPayment] = useState(false);

  // Calculate totals
  const calculateTotals = () => {
    let subtotal = order.items.reduce(
      (sum, item) => sum + item.quantity * item.price,
      0
    );
    let tax = subtotal * order.taxRate;
    let serviceCharge = subtotal * order.serviceChargeRate;
    let totalBeforeTip = subtotal + tax + serviceCharge;
    let tipAmount = totalBeforeTip * (selectedTip / 100);
    let total = totalBeforeTip + tipAmount;
    return { subtotal, tax, serviceCharge, totalBeforeTip, tipAmount, total };
  };

  const totals = calculateTotals();

  const handleConfirmPayment = () => {
    setIsProcessingPayment(true);
    // Simulate API call
    setTimeout(() => {
      console.log("Processing Payment with:", {
        order: order.items,
        totals,
        selectedTip,
        selectedPaymentMethod,
        billSplitOption,
        numPeopleSplit,
        receiptContact,
      });
      setIsProcessingPayment(false);
      alert("Payment successful!");
      // Here you would typically navigate to a confirmation page or close the checkout
    }, 2000);
  };

  return (
    <div className={`container mt-5 mb-5 ${styles.checkoutPage}`}>
      <h1 className={`text-center mb-4 ${styles.pageTitle}`}>
        Dine-In Checkout
      </h1>

      <div className="row g-4">
        <div className="col-lg-7">
          <OrderSummary order={order} totals={totals} />
          <TipSelection
            selectedTip={selectedTip}
            setSelectedTip={setSelectedTip}
          />
          <PaymentOptions
            selectedPaymentMethod={selectedPaymentMethod}
            setSelectedPaymentMethod={setSelectedPaymentMethod}
          />
        </div>
        <div className="col-lg-5">
          <BillSplitting
            billSplitOption={billSplitOption}
            setBillSplitOption={setBillSplitOption}
            numPeopleSplit={numPeopleSplit}
            setNumPeopleSplit={setNumPeopleSplit}
            totalAmount={totals.total}
          />
          <ReceiptRequest
            receiptContact={receiptContact}
            setReceiptContact={setReceiptContact}
          />
        </div>
      </div>

      <div className="row mt-4">
        <div className="col-12">
          <ConfirmPaymentButton
            onConfirm={handleConfirmPayment}
            isProcessing={isProcessingPayment}
            total={totals.total}
          />
        </div>
      </div>
    </div>
  );
}
