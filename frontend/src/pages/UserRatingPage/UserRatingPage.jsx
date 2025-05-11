import React, { useState, useEffect, useCallback } from "react";
import { Icon } from "@iconify/react";
import { Button } from "react-bootstrap";
import { FormLabel, FormControl } from "react-bootstrap";
import { Alert } from "react-bootstrap";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { getPrimaryProductImage } from "../../utils/imageUtils";
import { useOrder, useOrders } from "../../hooks/orderHooks";
import { formatDate } from "../../utils/datetimeUtils";
import { useRatingActions } from "../../hooks/ratingHooks";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";

const UserRatingPage = () => {
  const [selectedOrderItemId, setSelectedOrderItemId] = useState(null);
  const [rating, setRating] = useState({
    score: null,
    comment: "",
  });
  const [hoverRating, setHoverRating] = useState(null);
  const [isRated, setIsRated] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [selectedOrderId, setSelectedOrderId] = useState(null);

  const {
    data: ordersData,
    error: ordersError,
    isLoading: ordersLoading,
  } = useOrders({ isRated: false });
  const orders = ordersData?.content || [];

  const { data: orderData, error: orderError } = useOrder({
    orderId: selectedOrderId,
    isRated: false,
  });
  const orderItems = orderData?.orderItems || [];

  const { handleCreateRating, createError, createSuccess, resetCreateRating } =
    useRatingActions();

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  const handleOrderSelect = (orderId) => {
    setSelectedOrderId(orderId);
    setSelectedOrderItemId(null);
    setSelectedProduct(null);
    setIsRated(false);
    setRating({ score: null, comment: "" });
  };

  const handleOrderItemSelect = (orderItemId, product) => {
    setSelectedOrderItemId(orderItemId);
    setSelectedProduct(product);
    setIsRated(false);
    setRating({ score: null, comment: "" });
  };

  const handleRatingChange = (newRating) => {
    setRating((prevRating) => ({ ...prevRating, score: newRating }));
  };

  const handleCommentChange = (event) => {
    setRating((prevRating) => ({ ...prevRating, comment: event.target.value }));
  };

  const handleSubmit = useCallback(() => {
    if (!selectedOrderItemId) {
      showNewAlert({
        message: "Please select a product from the order to rate.",
        variant: "danger",
        action: resetCreateRating,
      });
      return;
    }
    if (!rating.score) {
      showNewAlert({
        message: "Please provide a rating.",
        variant: "danger",
        action: resetCreateRating,
      });
      return;
    }

    onOpen({
      title: "Confirm Rating",
      message: `Are you sure you want to rate ${selectedProduct?.productName} with ${rating.score} stars?`,
      onYes: handleCreate,
    });
  }, [selectedOrderItemId, rating, selectedProduct, handleCreateRating]);

  const handleCreate = useCallback(async () => {
    const ratingData = {
      productId: selectedProduct.id,
      comment: rating.comment,
      score: rating.score,
    };
    const data = await handleCreateRating(ratingData);
    if (data) {
      setIsRated(true);
      showNewAlert({
        message: createSuccess,
        variant: "success",
        action: resetCreateRating,
      });
    }
  }, [
    rating,
    selectedProduct,
    handleCreateRating,
    createSuccess,
    resetCreateRating,
  ]);

  const renderRatingStars = () => {
    const displayRating = hoverRating !== null ? hoverRating : rating.score;
    const fullStars = Math.floor(displayRating || 0);
    const hasHalfStar = (displayRating || 0) % 1 !== 0;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

    const stars = [];
    // Full stars
    for (let i = 0; i < fullStars; i++) {
      stars.push(
        <Icon
          key={`full-${i}`}
          icon="clarity:star-solid"
          className="text-warning cursor-pointer"
          style={{ fontSize: "24px" }}
          onClick={() => handleRatingChange(i + 1)}
          onMouseEnter={() => setHoverRating(i + 1)}
          onMouseLeave={() => setHoverRating(null)}
          role="button"
          tabIndex={0}
          aria-label={`Rate ${i + 1} stars`}
        />
      );
    }
    // Half star
    if (hasHalfStar) {
      stars.push(
        <Icon
          key="half"
          icon="clarity:half-star-solid"
          className="text-warning cursor-pointer"
          style={{ fontSize: "24px" }}
          onClick={() => handleRatingChange(fullStars + 0.5)}
          onMouseEnter={() => setHoverRating(fullStars + 0.5)}
          onMouseLeave={() => setHoverRating(null)}
          role="button"
          tabIndex={0}
          aria-label={`Rate ${fullStars + 0.5} stars`}
        />
      );
    }
    // Empty stars
    for (let i = 0; i < emptyStars; i++) {
      stars.push(
        <Icon
          key={`empty-${fullStars + (hasHalfStar ? 1 : 0) + i}`}
          icon="clarity:star-solid"
          className="text-gray-300 cursor-pointer"
          style={{ fontSize: "24px" }}
          onClick={() =>
            handleRatingChange(fullStars + (hasHalfStar ? 1 : 0) + i + 1)
          }
          onMouseEnter={() =>
            setHoverRating(fullStars + (hasHalfStar ? 1 : 0) + i + 1)
          }
          onMouseLeave={() => setHoverRating(null)}
          role="button"
          tabIndex={0}
          aria-label={`Rate ${fullStars + (hasHalfStar ? 1 : 0) + i + 1} stars`}
        />
      );
    }

    return (
      <span className="rating">
        {stars}
        <span className="ms-1">
          ({(hoverRating !== null ? hoverRating : rating.score || 0).toFixed(1)}
          )
        </span>
      </span>
    );
  };

  if (ordersLoading) return <Loading />;
  if (ordersError?.message || orderError?.message)
    return <ErrorDisplay error={ordersError?.message || orderError?.message} />;

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Rate Your Recent Purchases</h1>

      <div className="row">
        <div className="col-md-4">
          <h3>Orders</h3>
          <div className="list-group">
            {orders.map((order) => (
              <button
                key={order.id}
                type="button"
                className={`list-group-item list-group-item-action ${
                  selectedOrderId === order.id ? "active" : ""
                }`}
                onClick={() => handleOrderSelect(order.id)}
              >
                Order #{order.id} -{" "}
                {order.orderDate ? formatDate(order.orderDate) : "N/A"}
              </button>
            ))}
            {orders.length === 0 && (
              <p className="text-muted">No unrated orders found.</p>
            )}
          </div>
        </div>
        <div className="col-md-8">
          <h3>Products in Order</h3>
          {selectedOrderId ? (
            orderItems.length > 0 ? (
              <ul className="list-group">
                {orderItems.map((item) => (
                  <li
                    key={item.id}
                    className={`list-group-item list-group-item-action ${
                      selectedOrderItemId === item.id ? "active" : ""
                    }`}
                    onClick={() => handleOrderItemSelect(item.id, item.product)}
                  >
                    <div className="d-flex align-items-center">
                      <img
                        src={getPrimaryProductImage(item.product?.images)}
                        alt={item.product?.productName}
                        className="rounded me-3"
                        style={{
                          width: "50px",
                          height: "50px",
                          objectFit: "cover",
                        }}
                      />
                      <div>
                        <h6 className="mb-0">{item.product?.productName}</h6>
                        <small className="text-muted">
                          Quantity: {item.quantity}
                        </small>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-muted">
                No products found in the selected order.
              </p>
            )
          ) : (
            <p className="text-muted">Select an order to view its products.</p>
          )}

          {selectedProduct && (
            <div className="mt-4 card p-3">
              <h4>Rate & Review: {selectedProduct.productName}</h4>
              {createError?.message && (
                <Alert variant="danger" className="mb-2">
                  <Icon icon="lucide:alert-circle" className="h-4 w-4" />
                  {createError?.message}
                </Alert>
              )}
              <div className="mb-3">
                <FormLabel
                  htmlFor="rating"
                  className="block text-sm font-medium text-gray-700"
                >
                  Rating
                </FormLabel>
                <div className="mt-1">{renderRatingStars()}</div>
              </div>
              <div className="mb-3">
                <FormLabel
                  htmlFor="comment"
                  className="block text-sm font-medium text-gray-700"
                >
                  Comment
                </FormLabel>
                <FormControl
                  as="textarea"
                  id="comment"
                  value={rating.comment}
                  onChange={handleCommentChange}
                  rows={3}
                  placeholder="Your review..."
                  disabled={isRated}
                  className="mt-1"
                />
              </div>
              <Button
                onClick={handleSubmit}
                variant="primary"
                disabled={isRated}
              >
                {isRated ? "Rated!" : "Submit Rating"}
              </Button>
              {isRated && (
                <p className="mt-2 text-success font-semibold">
                  Thank you for your rating!
                </p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default UserRatingPage;
