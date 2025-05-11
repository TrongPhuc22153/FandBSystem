import React from "react";
import { Link } from "react-router-dom";
import { SHOP_URI } from "../../constants/routes";
import { getPrimaryProductImage } from "../../utils/imageUtils";
import styles from "./ProductCard.module.css";

function ProductCard({ product }) {
  return (
    <div className={styles["product-item"]}>
      <div
        className={`image-holder text-center position-relative ${styles["image-holder"]}`}
      >
        <img
          src={getPrimaryProductImage(product.picture)}
          alt={product.productName}
          className={`img-fluid rounded-4 ${styles["image"]}`}
        />
      </div>
      <div className={`product-detail ${styles["product-detail"]}`}>
        <h3 className={`fs-2 mb-1 ${styles["fs-2"]} ${styles["mb-1"]}`}>
          <Link
            to={`${SHOP_URI}/${product.productName}?id=${product.productId}`}
            className="text-black display-6 fw-semibold text-decoration-none"
          >
            {product.productName}
          </Link>
        </h3>
        <span
          className={`item-price text-body-tertiary pb-3 fs-5 ${styles["item-price"]} ${styles["pb-3"]} ${styles["fs-5"]}`}
        >
          ${product.unitPrice}
        </span>
        <div className={`stars ${styles["stars"]}`}>
          {Array.from({ length: 5 }).map((_, index) => (
            <svg
              key={index}
              className={`svg-yellow ${styles["svg-yellow"]}`}
              width="20"
              height="20"
            >
              <use xlinkHref="#star"></use>
            </svg>
          ))}
        </div>
      </div>
    </div>
  );
}

export default ProductCard;
