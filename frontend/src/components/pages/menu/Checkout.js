import { Link } from "react-router";
import { getPurchasedOrder } from "../../../api/OrderAPI";
import { useEffect, useState } from "react";
import { USER_ORDER_URI } from "../../../constants/WebPageURI";

export default function CheckoutOrderComponent() {
    const [orderData, setOrderData] = useState()

    useEffect(() => {
        const fetchOrder = async () => {
            const data = await getPurchasedOrder();
            setOrderData(data);
        };
        fetchOrder();
    }, []);

    return (
        <div className="container mb-5 panel-margin-top">
            <div id="checkout-bill" className="shadow pb-2">
                <div className="col d-flex">
                    <span className="text-muted" id="orderno">order #{orderData?.orderNumber || "Loading..."}</span>
                </div>
                <div className="gap">
                    <div className="col-2 d-flex mx-auto"> </div>
                </div>
                <div className="title mx-auto">
                    <h2 className="w-100 m-0 p-0">{orderData?.title || "Loading..."}</h2>
                </div>
                <div className="main">
                    <span id="sub-title">
                        <p><b>Payment Summary</b></p>
                    </span>
                    {orderData?.paymentSummary?.map((item, index) => (
                        <div className="row row-main" key={index}>
                            <div className="col-3">
                                <img className="img-fluid" src={item.image} alt="Product Image" />
                            </div>
                            <div className="col-6">
                                <div className="row d-flex">
                                    <p><b>{item.name}</b></p>
                                </div>
                                <div className="row d-flex">
                                    <p className="text-muted">{item.description}</p>
                                </div>
                            </div>
                            <div className="col-3 d-flex justify-content-end">
                                <p><b>{item.price}</b></p>
                            </div>
                        </div>
                    ))}
                    <hr />
                    <div className="total">
                        <div className="row mb-3">
                            <div className="col"><b>Shipping:</b></div>
                            <div className="col d-flex justify-content-end"><b>{orderData?.shipping || "Loading..."}</b></div>
                        </div>
                        <div className="row mb-3">
                            <div className="col"><b>Total:</b></div>
                            <div className="col d-flex justify-content-end"><b>{orderData?.total || "Loading..."}</b></div>
                        </div>
                        <div className="mb-3 text-center">
                            <Link className="btn btn-secondary mx-auto track-order-btn w-50" to={USER_ORDER_URI}>
                                Track your order
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}