import { Link } from "react-router";
import { getPurchasedOrder } from "../api/OrderAPI";
import { useEffect, useState } from "react";
import { USER_ORDER_URI } from "../constants/WebPageURI";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCircleUser,
  faDollar,
} from "@fortawesome/free-solid-svg-icons";
import { getCOD, getZaloPay } from "../services/ImageService";

export default function CheckoutOrderPage() {
  const [orderData, setOrderData] = useState();
  const [selectedTab, setSelectedTab] = useState(0); // Default active tab
  const [selectedPayment, setSelectedPayment] = useState("paypal");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [orderInformation, setOrderInformation] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    address: "",
    city: "",
    state: "",
    zip: "",
    notes: "",
  });

  const handlePaymentChange = (value) => {
    setSelectedPayment(value);
  };

  const handleTabClick = (index) => {
    setSelectedTab(index);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    // Handle form submission logic here
    setSelectedTab(1); // Move to the next tab (Payment Info)
  }

  useEffect(() => {
    const fetchOrder = async () => {
      const data = await getPurchasedOrder();
      setOrderData(data);
    };
    fetchOrder();
  }, []);

  return (
    <div id="checkout-frm" className="container mb-5 panel-margin-top">
      <div className="card">
        <div className="card-body">
          <ul className="nav nav-pills bg-nav-pills nav-justified mb-3">
            <li className="nav-item">
              <button onClick={() => handleTabClick(0)} className={`nav-link ${selectedTab === 0 && "active"}`}>
                <FontAwesomeIcon icon={faCircleUser} />
                <p>Billing Info</p>
              </button>
            </li>
            {/* <li className="nav-item">
              <a className="nav-link">
                <FontAwesomeIcon icon={faTruckFast} />
                <p>Shipping Info</p>
              </a>
            </li> */}
            <li className="nav-item">
              <button onClick={() => handleTabClick(1)} className={`nav-link ${selectedTab === 1 && "active"}`}>
                <FontAwesomeIcon icon={faDollar} />
                <p>Payment Info</p>
              </button>
            </li>
          </ul>
          <div className="row">
            <div className="col-md-8">
              {selectedTab === 0 ? (
                <>
                  <h4>Billing information</h4>
                  <form className="mb-3" onSubmit={handleSubmit}>
                    <div className="row mb-2">
                      <div className="form-group col-md-6">
                        <label htmlFor="inputFirstName">First Name</label>
                        <input
                          value={orderInformation.firstName}
                          onChange={(e) => setOrderInformation({ ...orderInformation, firstName: e.target.value })}
                          required
                          type="text"
                          className="form-control"
                          id="inputFirstName"
                          placeholder="First Name"
                        />
                      </div>
                      <div className="form-group col-md-6">
                        <label htmlFor="inputLastName">Last Name</label>
                        <input
                          value={orderInformation.lastName}
                          onChange={(e) => setOrderInformation({ ...orderInformation, lastName: e.target.value })}
                          required
                          type="text"
                          className="form-control"
                          id="inputLastName"
                          placeholder="Last Name"
                        />
                      </div>
                    </div>
                    <div className="row mb-2">
                      <div className="form-group col-md-6">
                        <label htmlFor="inputEmail">Email</label>
                        <input
                          value={orderInformation.email}
                          onChange={(e) => setOrderInformation({ ...orderInformation, email: e.target.value })}
                          required
                          type="email"
                          className="form-control"
                          id="inputEmail"
                          placeholder="First Name"
                        />
                      </div>
                      <div className="form-group col-md-6">
                        <label htmlFor="inputPhone">Phone</label>
                        <input
                          value={orderInformation.phone}
                          onChange={(e) => setOrderInformation({ ...orderInformation, phone: e.target.value })}
                          required
                          type="phone"
                          className="form-control"
                          id="inputPhone"
                          placeholder="Phone"
                        />
                      </div>
                    </div>
                    <div className="form-group mb-2">
                      <label htmlFor="inputAddress">Address</label>
                      <input
                        value={orderInformation.address}
                        onChange={(e) => setOrderInformation({ ...orderInformation, address: e.target.value })}
                        required
                        type="text"
                        className="form-control"
                        id="inputAddress"
                        placeholder="Address"
                      />
                    </div>
                    <div className="row mb-2">
                      <div className="form-group col-md-6">
                        <label htmlFor="inputCity">City</label>
                        <input
                          value={orderInformation.city}
                          onChange={(e) => setOrderInformation({ ...orderInformation, city: e.target.value })}
                          required
                          type="text"
                          className="form-control"
                          id="inputCity"
                        />
                      </div>
                      <div className="form-group col-md-4">
                        <label htmlFor="inputState">State</label>
                        <select id="inputState" className="form-control">
                          <option selected>Choose...</option>
                          <option>...</option>
                        </select>
                      </div>
                      <div className="form-group col-md-2">
                        <label htmlFor="inputZip">Zip</label>
                        <input
                          required type="text" className="form-control" id="inputZip" />
                      </div>
                    </div>
                    <div className="form-group mb-2">
                      <label htmlFor="txtAreaNote">Notes:</label>
                      <textarea
                        value={orderInformation.notes}
                        onChange={(e) => setOrderInformation({ ...orderInformation, notes: e.target.value })}
                        className="form-control"
                        id="txtAreaNote"
                        rows="3"
                      ></textarea>
                    </div>
                    <hr />
                    <button type="submit" className="btn btn-primary float-end">
                      Next
                    </button>
                  </form>
                </>
              ) :
                selectedTab === 1 && (
                  <>
                    <h4>Payment Information</h4>

                    {/* PayPal */}
                    <div className="rounded border border-2 p-3 mb-3" onClick={() => handlePaymentChange("paypal")}>
                      <div className="row">
                        <div className="col-6 d-flex align-items-center">
                          <input
                            className="form-check-input p-0"
                            type="radio"
                            name="paymentMethod"
                            id="paypal"
                            value="paypal"
                            checked={selectedPayment === "paypal"}
                            onChange={() => handlePaymentChange("paypal")}
                          />
                          <label className="ms-2 form-check-label" htmlFor="paypal">
                            Pay with PayPal
                          </label>
                        </div>
                        <div className="col-6">
                          <img
                            className="img-fluid float-end"
                            src="https://cdn-icons-png.flaticon.com/512/349/349247.png"
                            alt="Paypal"
                            style={{ width: "50px", height: "50px", marginLeft: "10px" }}
                          />
                        </div>
                      </div>
                    </div>

                    {/* ZaloPay */}
                    <div className="rounded border border-2 p-3 mb-3" onClick={() => handlePaymentChange("zalopay")}>
                      <div className="row">
                        <div className="col-6 d-flex align-items-center">
                          <input
                            className="form-check-input p-0"
                            type="radio"
                            name="paymentMethod"
                            id="zalopay"
                            value="zalopay"
                            checked={selectedPayment === "zalopay"}
                            onChange={() => handlePaymentChange("zalopay")}
                          />
                          <label className="ms-2 form-check-label" htmlFor="zalopay">
                            ZaloPay
                          </label>
                        </div>
                        <div className="col-6">
                          <img
                            className="img-fluid float-end"
                            src={getZaloPay()}
                            alt="ZaloPay"
                            style={{ width: "50px", height: "50px", marginLeft: "10px" }}
                          />
                        </div>
                      </div>
                    </div>

                    {/* Cash on Delivery (COD) */}
                    <div className="rounded border border-2 p-3 mb-3" onClick={() => handlePaymentChange("cod")}>
                      <div className="row">
                        <div className="col-6 d-flex align-items-center">
                          <input
                            className="form-check-input p-0"
                            type="radio"
                            name="paymentMethod"
                            id="cod"
                            value="cod"
                            checked={selectedPayment === "cod"}
                            onChange={() => handlePaymentChange("cod")}
                          />
                          <label className="ms-2 form-check-label" htmlFor="cod">
                            COD (Cash on Delivery)
                          </label>
                        </div>
                        <div className="col-6">
                          <img
                            className="img-fluid float-end"
                            src={getCOD()}
                            alt="COD"
                            style={{ width: "50px", height: "50px", marginLeft: "10px" }}
                          />
                        </div>
                      </div>
                    </div>

                    <hr />
                    <button type="submit" className="btn btn-primary float-end">
                      Proceed to Checkout
                    </button>
                  </>
                )
              }
            </div>
            <div className="col-md-4">
              <div className="main">
                <span id="sub-title">
                  <h4>Payment summary</h4>
                </span>
                {orderData?.paymentSummary?.map((item, index) => (
                  <div className="row row-main" key={index}>
                    <div className="col-3">
                      <img
                        className="img-fluid"
                        src={item.image}
                        alt="Product Image"
                      />
                    </div>
                    <div className="col-6">
                      <div className="row d-flex">
                        <p>
                          <b>{item.name}</b>
                        </p>
                      </div>
                      <div className="row d-flex">
                        <p className="text-muted">{item.description}</p>
                      </div>
                    </div>
                    <div className="col-3 d-flex justify-content-end">
                      <p>
                        <b>{item.price}</b>
                      </p>
                    </div>
                  </div>
                ))}
                <hr />
                <div className="total">
                  <div className="row mb-3">
                    <div className="col">
                      <b>Subtotal:</b>
                    </div>
                    <div className="col d-flex justify-content-end">
                      <b>{orderData?.subtotal || "Loading..."}</b>
                    </div>
                  </div>
                  <div className="row mb-3">
                    <div className="col">
                      <b>Shipping:</b>
                    </div>
                    <div className="col d-flex justify-content-end">
                      <b>{orderData?.shipping || "Loading..."}</b>
                    </div>
                  </div>
                  <div className="row mb-3">
                    <div className="col">
                      <b>Total:</b>
                    </div>
                    <div className="col d-flex justify-content-end">
                      <b>{orderData?.total || "Loading..."}</b>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className={`modal overlay ${isModalOpen && 'd-block'}`} tabindex="-1" role="dialog">
        <div className="modal-dialog" role="document">
          <div className="modal-content">
            <div id="checkout-bill" className="shadow pb-2">
              <div className="col d-flex">
                <span className="text-muted" id="orderno">
                  order #{orderData?.orderNumber || "Loading..."}
                </span>
              </div>
              <div className="gap">
                <div className="col-2 d-flex mx-auto"> </div>
              </div>
              <div className="title mx-auto">
                <h2 className="w-100 m-0 p-0">{orderData?.title || "Loading..."}</h2>
              </div>
              <div className="main">
                <span id="sub-title">
                  <p>
                    <b>Payment Summary</b>
                  </p>
                </span>
                {orderData?.paymentSummary?.map((item, index) => (
                  <div className="row row-main" key={index}>
                    <div className="col-3">
                      <img
                        className="img-fluid"
                        src={item.image}
                        alt="Product Image"
                      />
                    </div>
                    <div className="col-6">
                      <div className="row d-flex">
                        <p>
                          <b>{item.name}</b>
                        </p>
                      </div>
                      <div className="row d-flex">
                        <p className="text-muted">{item.description}</p>
                      </div>
                    </div>
                    <div className="col-3 d-flex justify-content-end">
                      <p>
                        <b>{item.price}</b>
                      </p>
                    </div>
                  </div>
                ))}
                <hr />
                <div className="total">
                  <div className="row mb-3">
                    <div className="col">
                      <b>Shipping:</b>
                    </div>
                    <div className="col d-flex justify-content-end">
                      <b>{orderData?.shipping || "Loading..."}</b>
                    </div>
                  </div>
                  <div className="row mb-3">
                    <div className="col">
                      <b>Total:</b>
                    </div>
                    <div className="col d-flex justify-content-end">
                      <b>{orderData?.total || "Loading..."}</b>
                    </div>
                  </div>
                  <div className="mb-3 text-center">
                    <Link
                      className="btn btn-secondary mx-auto track-order-btn w-50"
                      to={USER_ORDER_URI}
                    >
                      Track your order
                    </Link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
