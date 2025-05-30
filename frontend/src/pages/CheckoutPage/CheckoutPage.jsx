import { useState, useEffect, useCallback, useMemo } from "react";
import {
  useShippingAddressActions,
  useShippingAddresses,
} from "../../hooks/addressHooks";
import { useCart } from "../../hooks/cartHooks";
import { useOrderActions } from "../../hooks/orderHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useModal } from "../../context/ModalContext";
import CheckoutSummary from "../../components/CheckoutSummary/CheckoutSummary";
import { useAlert } from "../../context/AlertContext";
import { Link } from "react-router-dom";
import { HOME_URI } from "../../constants/routes";
import { CHECKOUT_ITEMS, ORDER_TYPES } from "../../constants/webConstant";
import { usePaymentMethods } from "../../hooks/paymentMethodHooks";
import PaymentMethodOptions from "../../components/PaymentMethodOptions/PaymentMethodOptions";
import { CANCEL_PAYMENT_URL, PAYMENT_METHODS, PAYMENT_TYPES, SUCCESS_PAYMENT_URL } from "../../constants/paymentConstants";
import { usePaymentActions } from "../../hooks/paymentHooks";

const CheckoutPage = () => {
  const [selectedPayment, setSelectedPayment] = useState(null);

  const {
    data: addresses,
    isLoading: loadingAddresses,
    error: addressesError,
  } = useShippingAddresses();

  const {
    data: cartData,
    isLoading: loadingCartData,
    error: cartError,
  } = useCart();

  const {
    handleProcessPayment,
    paymentError,
    resetPayment
  } = usePaymentActions()

  const {
    data: paymentMethodsData,
    isLoading: loadingPaymentMethods,
    error: paymentMethodsError,
  } = usePaymentMethods(PAYMENT_TYPES.TAKE_AWAY);

  const paymentMethods = useMemo(
    () => paymentMethodsData || [],
    [paymentMethodsData]
  );

  const handlePaymentMethodChange = useCallback((id) => {
    setSelectedPayment(id);
  }, []);

  useEffect(() => {
    if (paymentMethods.length > 0 && selectedPayment === null) {
      const codMethod = paymentMethods.find(
        (method) => method.methodName.toLowerCase() === PAYMENT_METHODS.COD
      );
      if (codMethod) {
        setSelectedPayment(codMethod.methodName);
      } else if (paymentMethods.length > 0) {
        setSelectedPayment(paymentMethods[0].methodName);
      }
    }
  }, [paymentMethods, selectedPayment]);

  const getItemsFromLocalStorage = (key) => {
    try {
      const serializedItems = localStorage.getItem(key);
      if (serializedItems === null) {
        return [];
      }
      return JSON.parse(serializedItems);
    } catch (error) {
      return [];
    }
  };

  // get checkout Items
  const getCheckoutItems = () => {
    const items = cartData?.cartItems || [];
    const selectedItems = getItemsFromLocalStorage(CHECKOUT_ITEMS);
    // filter selected items
    const checkoutItems = items.filter((item) => {
      return selectedItems.some(
        (selectedItem) => selectedItem.productId === item.product.productId
      );
    });
    // merget selectedItems with quantity
    const result = checkoutItems.map((item) => {
      const matchingSelectedItem = selectedItems.find(
        (selectedItem) => selectedItem.productId === item.product.productId
      );
      return {
        ...item,
        quantity: matchingSelectedItem ? matchingSelectedItem.quantity : 0,
      };
    });
    return result;
  };

  const cartItems = getCheckoutItems();
  const totalPrice = cartData?.totalPrice || 0;
  const shippingCost = 0;
  const finalTotalPrice = totalPrice + shippingCost;

  const { handlePlaceOrder, placeError, placeLoading, placeSuccess } =
    useOrderActions();

  const { showNewAlert } = useAlert();

  const {
    handleCreateShippingAddress,
    createError: createAddressMessageError,
  } = useShippingAddressActions();

  const { onOpen } = useModal();

  const [address, setAddress] = useState({
    shipName: "",
    shipAddress: "",
    shipCity: "",
    shipDistrict: "",
    shipWard: "",
    phone: "",
  });

  const [selectedAddressId, setSelectedAddressId] = useState(null);

  const [fieldErrors, setFieldErrors] = useState({});
  const [isOpenPopUp, setIsOpenPopUp] = useState(false);

  useEffect(() => {
    if (addresses && addresses.length > 0) {
      const defaultAddress = addresses.find((addr) => addr.isDefault);
      if (defaultAddress) {
        setAddress({
          shipName: defaultAddress.shipName || "",
          shipAddress: defaultAddress.shipAddress || "",
          shipCity: defaultAddress.shipCity || "",
          shipDistrict: defaultAddress.shipDistrict || "",
          shipWard: defaultAddress.shipWard || "",
          phone: defaultAddress.phone || "",
        });
        setSelectedAddressId(defaultAddress.id);
      }
    }
  }, [addresses]);

  useEffect(() => {
    if(paymentError?.message){
      showNewAlert({
        message: paymentError.message,
        variant: "danger",
        action: resetPayment
      })
    }
    setFieldErrors(placeError?.fields ?? {});
  }, [placeError, paymentError, resetPayment, showNewAlert]);

  useEffect(() => {
    setFieldErrors(createAddressMessageError?.fields ?? {});
  }, [createAddressMessageError]);

  const validateForm = () => {
    const errors = {};
    if (!selectedAddressId || selectedAddressId === "new") {
      if (!address.shipName.trim()) {
        errors.shipName = ["Please enter your name."];
      }
      if (!address.shipAddress.trim()) {
        errors.shipAddress = ["Please enter your address."];
      }
      if (!address.shipCity.trim()) {
        errors.shipCity = ["Please enter your city."];
      }
      if (!address.shipDistrict.trim()) {
        errors.shipDistrict = ["Please enter your district."];
      }
      if (!address.shipWard.trim()) {
        errors.shipWard = ["Please enter your ward."];
      }
      if (!address.phone.trim()) {
        errors.phone = ["Please enter your phone number."];
      } else if (!/^\d+$/.test(address.phone)) {
        errors.phone = ["Please enter a valid phone number (numbers only)."];
      }
    }
    setFieldErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handlePlace = useCallback(async () => {
    if (cartItems && cartItems.length > 0) {
      const orderItems = cartItems.map((item) => ({
        productId: item.product.productId,
        quantity: item.quantity,
      }));

      const requestOrderDTO = {
        orderDetails: orderItems,
      };

      if (selectedAddressId && selectedAddressId !== "new") {
        requestOrderDTO.shippingAddressId = selectedAddressId;
      } else {
        // Create new shipping address
        const newAddressData = {
          ...address,
          id: null,
          isDefault: false,
        };
        const newCreatedAddress = await handleCreateShippingAddress(
          newAddressData
        );
        if (newCreatedAddress) {
          requestOrderDTO.shippingAddressId = newCreatedAddress?.id;
        }
      }

      const res = await handlePlaceOrder(requestOrderDTO, ORDER_TYPES.TAKE_AWAY);
      if (res) {
        setIsOpenPopUp(true);
        setFieldErrors({});

        const orderId = res.data.orderId;
        const paymentId = res.data.payment.paymentId;

        const paymentRes = await handleProcessPayment({
          id: paymentId,
          returnUrl: SUCCESS_PAYMENT_URL,
          cancelUrl: CANCEL_PAYMENT_URL,
          paymentMethod: selectedPayment,
          orderId: orderId
        })
        if(paymentRes){
          const link = paymentRes.data.link;
          if(link){
            window.location.href = link;
          }
        }
      }
    } else {
      showNewAlert({
        message: "Your cart is empty. Please add items to proceed.",
        variant: "danger",
      });
    }
  }, [handleCreateShippingAddress, handlePlaceOrder, showNewAlert, cartItems, address, selectedAddressId, cartData]);

  const showConfirmModal = () => {
    if (validateForm()) {
      onOpen({
        title: "Checkout",
        message: "Do you want to checkout?",
        onYes: handlePlace,
      });
    }
  };

  const handleInputChange = (e) => {
    const { id, value } = e.target;
    setAddress((prev) => ({ ...prev, [id]: value }));
  };

  const handleAddressChange = (e) => {
    const value = e.target.value;
    setSelectedAddressId(value);

    if (value === "new") {
      setAddress({
        shipName: "",
        shipAddress: "",
        shipCity: "",
        shipDistrict: "",
        shipWard: "",
        phone: "",
      });
    } else {
      const selectedAddress = addresses.find((addr) => addr.id == value);
      if (selectedAddress) {
        setAddress({
          shipName: selectedAddress.shipName || "",
          shipAddress: selectedAddress.shipAddress || "",
          shipCity: selectedAddress.shipCity || "",
          shipDistrict: selectedAddress.shipDistrict || "",
          shipWard: selectedAddress.shipWard || "",
          phone: selectedAddress.phone || "",
        });
      }
    }
  };

  if (loadingAddresses || loadingCartData || loadingPaymentMethods) {
    return <Loading />;
  }

  if (addressesError || cartError || paymentMethodsError) {
    return (
      <ErrorDisplay
        message={
          addressesError?.message ||
          cartError?.message ||
          paymentMethodsError?.message
        }
      />
    );
  }

  return (
    <div className="container">
      <div className="row">
        <div className="col-xl-8 col-lg-8 mb-4">
          <div className="card shadow-0 border">
            <div className="p-4">
              <h5 className="card-title mb-3">Shipping info</h5>

              <div className="mb-3">
                <p className="mb-0">Select Address</p>
                <div className="form-outline">
                  <select
                    className="form-control"
                    value={selectedAddressId || ""}
                    onChange={handleAddressChange}
                  >
                    <option value="" disabled>
                      Choose an address
                    </option>
                    {addresses && addresses.length > 0 ? (
                      addresses.map((addr) => (
                        <option key={addr.id} value={addr.id}>
                          {addr.shipName}, {addr.shipAddress}, {addr.shipCity}
                        </option>
                      ))
                    ) : (
                      <option disabled>No saved addresses</option>
                    )}
                    <option value="new">Enter new address</option>
                  </select>
                </div>
              </div>

              <PaymentMethodOptions
                paymentMethods={paymentMethods}
                selectedPaymentMethod={selectedPayment}
                onPaymentMethodChange={handlePaymentMethodChange}
              />

              <div className="mb-3">
                <p className="mb-0">Name</p>
                <div className="form-outline">
                  <input
                    type="text"
                    id="shipName"
                    className={`form-control ${
                      fieldErrors?.shipName ? "is-invalid" : ""
                    }`}
                    value={address.shipName}
                    onChange={handleInputChange}
                    disabled={selectedAddressId && selectedAddressId !== "new"}
                  />
                </div>
                {fieldErrors.shipName &&
                  fieldErrors.shipName.map((error, index) => (
                    <div key={index} className="error-message text-danger">
                      {error}
                    </div>
                  ))}
              </div>

              <div className="mb-3">
                <p className="mb-0">Address</p>
                <div className="form-outline">
                  <input
                    type="text"
                    id="shipAddress"
                    className={`form-control ${
                      fieldErrors?.shipAddress ? "is-invalid" : ""
                    }`}
                    value={address.shipAddress}
                    onChange={handleInputChange}
                    disabled={selectedAddressId && selectedAddressId !== "new"}
                  />
                </div>
                {fieldErrors.shipAddress &&
                  fieldErrors.shipAddress.map((error, index) => (
                    <div key={index} className="error-message text-danger">
                      {error}
                    </div>
                  ))}
              </div>

              <div className="row">
                <div className="col-sm-6 mb-3">
                  <p className="mb-0">City</p>
                  <div className="form-outline">
                    <input
                      type="text"
                      id="shipCity"
                      className={`form-control ${
                        fieldErrors?.shipCity ? "is-invalid" : ""
                      }`}
                      value={address.shipCity}
                      onChange={handleInputChange}
                      disabled={
                        selectedAddressId && selectedAddressId !== "new"
                      }
                    />
                  </div>
                  {fieldErrors.shipCity &&
                    fieldErrors.shipCity.map((error, index) => (
                      <div key={index} className="error-message text-danger">
                        {error}
                      </div>
                    ))}
                </div>
                <div className="col-sm-6 mb-3">
                  <p className="mb-0">District</p>
                  <div className="form-outline">
                    <input
                      type="text"
                      id="shipDistrict"
                      className={`form-control ${
                        fieldErrors?.shipDistrict ? "is-invalid" : ""
                      }`}
                      value={address.shipDistrict}
                      onChange={handleInputChange}
                      disabled={
                        selectedAddressId && selectedAddressId !== "new"
                      }
                    />
                  </div>
                  {fieldErrors.shipDistrict &&
                    fieldErrors.shipDistrict.map((error, index) => (
                      <div key={index} className="error-message text-danger">
                        {error}
                      </div>
                    ))}
                </div>
              </div>

              <div className="mb-3">
                <p className="mb-0">Ward</p>
                <div className="form-outline">
                  <input
                    type="text"
                    id="shipWard"
                    className={`form-control ${
                      fieldErrors?.shipWard ? "is-invalid" : ""
                    }`}
                    value={address.shipWard}
                    onChange={handleInputChange}
                    disabled={selectedAddressId && selectedAddressId !== "new"}
                  />
                </div>
                {fieldErrors.shipWard &&
                  fieldErrors.shipWard.map((error, index) => (
                    <div key={index} className="error-message text-danger">
                      {error}
                    </div>
                  ))}
              </div>

              <div className="mb-3">
                <p className="mb-0">Phone Number</p>
                <div className="form-outline">
                  <input
                    type="tel"
                    id="phone"
                    className={`form-control ${
                      fieldErrors?.phone ? "is-invalid" : ""
                    }`}
                    value={address.phone}
                    onChange={handleInputChange}
                    disabled={selectedAddressId && selectedAddressId !== "new"}
                  />
                </div>
                {fieldErrors.phone &&
                  fieldErrors.phone.map((error, index) => (
                    <div key={index} className="error-message text-danger">
                      {error}
                    </div>
                  ))}
              </div>

              <div className="float-end">
                <button
                  className="btn btn-success shadow-0 border"
                  onClick={showConfirmModal}
                  disabled={placeLoading}
                >
                  {placeLoading ? "Placing Order..." : "Continue"}
                </button>
                {placeSuccess && (
                  <div className="mt-2 text-success">{placeSuccess}</div>
                )}
                {placeError?.message && (
                  <div className="mt-2 text-danger">
                    Error: {placeError.message}
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
        <div className="col-xl-4 col-lg-4 d-flex justify-content-center justify-content-lg-end">
          <CheckoutSummary
            cartItems={cartItems}
            totalPrice={totalPrice}
            shippingCost={shippingCost}
            finalTotalPrice={finalTotalPrice}
          />
        </div>
      </div>
      {isOpenPopUp && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100%",
            height: "100%",
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            zIndex: 1000,
          }}
        >
          <div
            style={{
              backgroundColor: "white",
              padding: "20px",
              borderRadius: "8px",
              boxShadow: "0 0 10px rgba(0, 0, 0, 0.2)",
              textAlign: "center",
            }}
          >
            <h3>Order Placed Successfully!</h3>
            <p>Thank you for your order.</p>
            <Link to={HOME_URI} className="btn btn-primary mt-3">
              Home
            </Link>
          </div>
        </div>
      )}
    </div>
  );
};

export default CheckoutPage;