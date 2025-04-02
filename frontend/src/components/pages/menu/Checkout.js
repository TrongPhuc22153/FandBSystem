import { Link } from "react-router";
import { getPurchasedOrder } from "../../../api/OrderAPI";
import { useEffect, useState } from "react";
import { USER_ORDER_URI } from "../../../constants/WebPageURI";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCircleUser,
  faDollar,
  faTruckFast,
} from "@fortawesome/free-solid-svg-icons";

export default function CheckoutOrderComponent() {
  const [orderData, setOrderData] = useState();

  useEffect(() => {
    const fetchOrder = async () => {
      const data = await getPurchasedOrder();
      setOrderData(data);
    };
    fetchOrder();
  }, []);

  return (
    <div id="checkout-frm" className="container mb-5 panel-margin-top">
      {/* <div id="checkout-bill" className="shadow pb-2">
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
      </div> */}

      <div className="card">
        <div className="card-body">
          <ul className="nav nav-pills bg-nav-pills nav-justified mb-3">
            <li className="nav-item">
              <a href="#" className="nav-link">
                <FontAwesomeIcon icon={faCircleUser} />
                <p>Billing Info</p>
              </a>
            </li>
            <li className="nav-item">
              <a href="#" className="nav-link">
                <FontAwesomeIcon icon={faTruckFast} />
                <p>Shipping Info</p>
              </a>
            </li>
            <li className="nav-item">
              <a href="#" className="nav-link">
                <FontAwesomeIcon icon={faDollar} />
                <p>Payment Info</p>
              </a>
            </li>
          </ul>
          <div className="row">
            <div className="col-md-8">
              <h4>Billing information</h4>
              <form className="mb-3">
                <div className="row mb-2">
                  <div className="form-group col-md-6">
                    <label for="inputFirstName">First Name</label>
                    <input
                      type="text"
                      className="form-control"
                      id="inputFirstName"
                      placeholder="First Name"
                    />
                  </div>
                  <div className="form-group col-md-6">
                    <label for="inputLastName">Last Name</label>
                    <input
                      type="text"
                      className="form-control"
                      id="inputLastName"
                      placeholder="Last Name"
                    />
                  </div>
                </div>
                <div className="row mb-2">
                  <div className="form-group col-md-6">
                    <label for="inputEmail">Email</label>
                    <input
                      type="email"
                      className="form-control"
                      id="inputEmail"
                      placeholder="First Name"
                    />
                  </div>
                  <div className="form-group col-md-6">
                    <label for="inputPhone">Phone</label>
                    <input
                      type="number"
                      className="form-control"
                      id="inputPhone"
                      placeholder="Phone"
                    />
                  </div>
                </div>
                <div className="form-group mb-2">
                  <label for="inputAddress">Address</label>
                  <input
                    type="text"
                    className="form-control"
                    id="inputAddress"
                    placeholder="Address"
                  />
                </div>
                <div className="row mb-2">
                  <div className="form-group col-md-6">
                    <label for="inputCity">City</label>
                    <input
                      type="text"
                      className="form-control"
                      id="inputCity"
                    />
                  </div>
                  <div className="form-group col-md-4">
                    <label for="inputState">State</label>
                    <select id="inputState" className="form-control">
                      <option selected>Choose...</option>
                      <option>...</option>
                    </select>
                  </div>
                  <div className="form-group col-md-2">
                    <label for="inputZip">Zip</label>
                    <input type="text" className="form-control" id="inputZip" />
                  </div>
                </div>
                <div class="form-group mb-2">
                  <label for="txtAreaNote">Notes:</label>
                  <textarea
                    class="form-control"
                    id="txtAreaNote"
                    rows="3"
                  ></textarea>
                </div>
                <hr />
                <button type="submit" className="btn btn-primary">
                  Confirm
                </button>
              </form>
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
    </div>
  );
}
