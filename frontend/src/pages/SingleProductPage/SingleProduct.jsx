import React, { useState, useCallback, useEffect } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { HOME_URI, LOGIN_URI, SHOP_URI } from "../../constants/routes";
import QuantityInput from "../../components/QuantityInput/QuanityInput";
import { useProduct } from "../../hooks/productHooks";
import { useRatings, useUserProductRating } from "../../hooks/ratingHooks";
import { useCartActions } from "../../hooks/cartHooks";
import { useAlert } from "../../context/AlertContext";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import background from "../../assets/images/background.jpg";
import ReviewForm from "../../components/ReviewForm/ReviewForm";
import ReviewList from "../../components/ReviewList/ReviewList";
import RatingStar from "../../components/RatingStar/RatingStar";
import { hasRole } from "../../utils/authUtils";
import { useAuth } from "../../context/AuthContext";
import { ROLES } from "../../constants/roles";
import SingleImageDisplay from "../../components/SingleImageDisplay/SingleImageDisplay";
import { useModal } from "../../context/ModalContext";

const SingleProduct = () => {
  const [searchParams] = useSearchParams();
  const productIdSearch = searchParams.get("id") || 0;
  const { user, error: authError } = useAuth();

  const [productId] = useState(productIdSearch);
  const [quantity, setQuantity] = useState(1);

  const { showNewAlert } = useAlert();

  const { handleAddCartItem, addError, resetAdd } = useCartActions();
  const { data: userRatingData } = useUserProductRating({ productId });
  const { onOpen } = useModal();
  const navigate = useNavigate();

  useEffect(() => {
    if (authError && addError) {
      onOpen({
        title: "Login is required!",
        message: "You need to login to add this food to cart!",
        onYes: () => {
          resetAdd();
          navigate(LOGIN_URI);
        },
        onNo: () => resetAdd(),
      });
    }
    if(addError?.message){
      showNewAlert({
        message: addError.message,
        variant: "danger",
        action: resetAdd
      });
    }
  }, [authError, addError]);

  const handleQuantityChange = useCallback((newQuantity) => {
    setQuantity(newQuantity);
  }, []);

  const handleAddToCart = useCallback(
    async (productId, qty) => {
      const data = {
        productId: productId,
        quantity: qty,
      };
      const response = await handleAddCartItem(data);
      if (response) {
        showNewAlert({
          message: response.message,
        });
      }
    },
    [handleAddCartItem]
  );

  const {
    data: product,
    error: productError,
    isLoading: productLoading,
  } = useProduct({ productId });

  const { data: ratingsData } = useRatings({
    productId,
  });

  if (!product && productLoading) {
    return <Loading />;
  }

  if (productError?.message) {
    return <ErrorDisplay message={productError.message} />;
  }

  const formattedProduct = {
    name: product?.productName || "Product Name Unavailable",
    rating: ratingsData?.averageScore || 0,
    price: product?.unitPrice
      ? `$${product.unitPrice.toFixed(2)}`
      : "Price Unavailable",
    description: product?.description || "No description available.",
    stock:
      product?.unitsInStock !== undefined
        ? product.unitsInStock > 5
          ? `${product.unitsInStock} in stock`
          : product.unitsInStock > 0
          ? `Limited stock (${product.unitsInStock})`
          : "Out of stock"
        : "Stock information unavailable",
    unitsInStock: product?.unitsInStock,
    category: {
      id: product?.category?.id,
      name: product?.category?.categoryName || "Uncategorized",
    },
    images: product?.images,
    reviews: ratingsData?.ratings?.content || [],
    details: product?.description || "No details available.",
  };

  return (
    <>
      <section id="hero">
        <div
          className="container-fluid align-items-center d-flex rounded-4"
          style={{
            height: "40vh",
            backgroundImage: `url(${background})`,
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        >
          <div className="hero-content container justify-content-center text-center">
            <h2 className="display-2 fw-bold text-body text-capitalize mb-1">
              {formattedProduct.name}
            </h2>
            <span className="item">
              <Link to={HOME_URI} className="text-body">
                Home /
              </Link>
            </span>
            <span className="item">Product</span>
          </div>
        </div>
      </section>
      <section id="selling-product" className="my-5">
        <div className="container-fluid">
          <div className="row px-3">
            <div className="col-lg-4">
              <SingleImageDisplay imageUrl={product.picture}/>
              {/* <ImagesShowcase images={product.images} /> */}
            </div>
            <div className="col-lg-6 mt-5 mt-lg-0">
              <div className="product-info">
                <div className="element-header">
                  <h2 itemProp="name" className="display-6 fw-bold mt-0 mb-3">
                    {formattedProduct.name}
                  </h2>
                  <div className="rating-container d-flex gap-0 align-items-center">
                    <RatingStar score={formattedProduct.rating} />
                  </div>
                </div>
                <div className="product-price pt-3 pb-3">
                  <strong className="text-primary display-6 fw-semibold">
                    {formattedProduct.price}
                  </strong>
                </div>
                <p>{formattedProduct.description}</p>
                <div className="product-quantity">
                  <div className="stock-number text-dark mb-2">
                    <em>{formattedProduct.stock}</em>
                  </div>
                  <div className="stock-button-wrap">
                    <div className="d-md-flex flex-wrap align-items-center">
                      {product && (
                        <QuantityInput
                          unitsInStock={product.unitsInStock}
                          quantity={quantity}
                          onQuantityChange={handleQuantityChange}
                        />
                      )}
                      <div className="d-flex align-items-center flex-wrap mt-2">
                        {product && (
                          <button
                            type="button"
                            className="text-capitalize me-1 px-3 py-2 btn btn-primary"
                            onClick={() =>
                              handleAddToCart(product.id, quantity)
                            }
                          >
                            Add to Cart
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
                <div className="meta-product pt-3">
                  <div className="meta-item d-flex align-items-baseline">
                    <h5 className="item-title fw-bold no-margin pe-2">
                      Category:
                    </h5>
                    <ul className="select-list list-unstyled d-flex">
                      <Link
                        to={`${SHOP_URI}?categoryId=${formattedProduct.category.id}`}
                      >
                        {formattedProduct.category.name}
                      </Link>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="row mt-5">
            <div className="tabs-listing">
              <nav>
                <div
                  className="nav nav-tabs d-flex justify-content-center"
                  id="nav-tab"
                  role="tablist"
                >
                  <button
                    className="nav-link text-uppercase px-5 py-3 mx-3 active"
                    id="nav-home-tab"
                    data-bs-toggle="tab"
                    data-bs-target="#nav-home"
                    type="button"
                    role="tab"
                    aria-controls="nav-home"
                    aria-selected="false"
                    tabIndex="-1"
                  >
                    Details
                  </button>
                  <button
                    className="nav-link text-uppercase px-5 py-3 mx-3"
                    id="nav-review-tab"
                    data-bs-toggle="tab"
                    data-bs-target="#nav-review"
                    type="button"
                    role="tab"
                    aria-controls="nav-review"
                    aria-selected="true"
                  >
                    Reviews
                  </button>
                </div>
              </nav>
              <div className="tab-content py-5" id="nav-tabContent">
                <div
                  className="tab-pane fade active show"
                  id="nav-home"
                  role="tabpanel"
                  aria-labelledby="nav-home-tab"
                >
                  <h5>Product Description</h5>
                  <p>{formattedProduct.details}</p>
                </div>
                <div
                  className="tab-pane fade"
                  id="nav-review"
                  role="tabpanel"
                  aria-labelledby="nav-review-tab"
                >
                  {hasRole(user, ROLES.CUSTOMER) && userRatingData && (
                    <ReviewForm review={userRatingData} productId={productId} />
                  )}
                  <ReviewList ratingsData={ratingsData} />
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default SingleProduct;
