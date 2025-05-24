const PaymentMethodOptions = ({
  paymentMethods,
  selectedPaymentMethod,
  onPaymentMethodChange,
}) => {
  return (
    <div className="row mb-3">
      {paymentMethods.map((method) => (
        <div className="col-lg-4 mb-3" key={method.methodId}>
          <div
            className="form-check h-100 border rounded-3"
            key={method.methodId}
          >
            <div className="p-3">
              <input
                className="form-check-input"
                type="radio"
                name="paymentMethodRadio"
                id={method.methodId}
                checked={selectedPaymentMethod === method.methodName}
                onChange={() => onPaymentMethodChange(method.methodName)}
              />
              <label className="form-check-label" htmlFor={method.methodId}>
                {method.methodName.toUpperCase()} <br />
                <small className="text-muted">{method.details}</small>
              </label>
            </div>
          </div>
        </div>

      ))}
    </div>
  );
};
export default PaymentMethodOptions;
