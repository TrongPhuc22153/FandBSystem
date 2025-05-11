import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";

const QuantityInput = ({ quantity, unitsInStock, onQuantityChange }) => {
  const handleDecrement = () => {
    if (quantity > 1) {
      onQuantityChange(quantity - 1);
    }
  };

  const handleIncrement = () => {
    if (quantity < unitsInStock) {
      onQuantityChange(quantity + 1);
    }
  };

  const handleInputChange = (event) => {
    const value = parseInt(event.target.value);
    if (!isNaN(value) && value >= 1 && value <= unitsInStock) {
      onQuantityChange(value);
    } else if (isNaN(value) || value < 1) {
      onQuantityChange(1);
    } else if (value > unitsInStock) {
      onQuantityChange(unitsInStock);
    }
  };

  return (
    <div className="input-group product-qty product-box align-items-center me-3 my-3 my-lg-0 w-auto">
      <span className="input-group-btn">
        <button
          type="button"
          className="quantity-left-minus p-1 p-lg-2 btn btn-outline-primary btn-number"
          data-type="minus"
          onClick={handleDecrement}
          disabled={quantity <= 1}
        >
          <FontAwesomeIcon icon={faMinus} />
        </button>
      </span>
      <input
        type="text"
        id="quantity"
        name="quantity"
        className="form-control input-number border-primary text-center p-2 mx-1"
        value={quantity}
        onChange={handleInputChange}
      />
      <span className="input-group-btn">
        <button
          type="button"
          className="quantity-right-plus p-1 p-lg-2 btn btn-outline-primary btn-number"
          data-type="plus"
          data-field=""
          onClick={handleIncrement}
          disabled={quantity >= unitsInStock}
        >
          <FontAwesomeIcon icon={faPlus} />
        </button>
      </span>
    </div>
  );
};

export default QuantityInput;
