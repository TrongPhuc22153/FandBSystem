import { faPenToSquare } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";

const food = {
  category: "Women",
  title: "Black dress for Women",
  oldPrice: 100,
  discount: 20,
  newPrice: 80,
  description:
    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Placeat excepturi odio recusandae aliquid ad impedit autem commodi earum voluptatem laboriosam?",
  mainImage:
    "https://cdn.pixabay.com/photo/2015/07/24/18/40/model-858753_960_720.jpg",
  previewImages: [
    "https://cdn.pixabay.com/photo/2015/07/24/18/40/model-858754_960_720.jpg",
    "https://cdn.pixabay.com/photo/2015/07/24/18/38/model-858749_960_720.jpg",
    "https://cdn.pixabay.com/photo/2015/07/24/18/37/model-858748_960_720.jpg",
    "https://cdn.pixabay.com/photo/2015/07/24/18/39/model-858751_960_720.jpg",
  ],
  available_stock: 1784,
  number_of_orders: 5458,
  revenue: "$8,57,014",
};
export default function ProductAdmin() {
  const [product, setProduct] = useState(food);
  const [quantity, setQuantity] = useState(1);

  const handleQuantityChange = (event) => {
    const value = Math.max(0, Math.min(5, Number(event.target.value))); // Ensure value is between 0 and 5
    setQuantity(value);
  };

  return (
    <div className="container-fluid">
      <h1 className="h3 my-3">
        <strong>Product Details</strong>
      </h1>
      <div className="mb-5">
        <div className="row">
          <div className="col-md-5">
            <div className="main-img">
              <img
                className="img-fluid"
                src={product.mainImage}
                alt="Product"
              />
              <div className="row my-3 previews">
                {product.previewImages.map((image, index) => (
                  <div className="col-md-3" key={index}>
                    <img className="w-100" src={image} alt="Preview" />
                  </div>
                ))}
              </div>
            </div>
          </div>

          <div className="col-md-7 d-flex flex-column justify-content-between">
            <div className="main-description px-2">
              <div className="category text-bold">
                Category: {product.category}
              </div>

              <div className="product-title text-bold my-3">
                {product.title}
                <button className="btn">
                  <FontAwesomeIcon icon={faPenToSquare} />
                </button>
              </div>

              <div className="price-area my-4">
                <p className="old-price mb-1">
                  <del>${product.oldPrice}</del>{" "}
                  <span className="old-price-discount text-danger">
                    ({product.discount}% off)
                  </span>
                </p>
                <p className="new-price text-bold mb-1">${product.newPrice}</p>
                <p className="text-secondary mb-1">
                  (Additional tax may apply on checkout)
                </p>
              </div>

              <div className="buttons d-flex my-5">
                <div className="block">
                  <a href="#" className="shadow btn custom-btn">
                    Wishlist
                  </a>
                </div>
                <div className="block">
                  <button className="shadow btn custom-btn">Add to cart</button>
                </div>

                <div className="block quantity">
                  <input
                    type="number"
                    className="form-control"
                    id="cart_quantity"
                    value={quantity}
                    onChange={handleQuantityChange}
                    min="0"
                    max="5"
                    placeholder="Enter quantity"
                    name="cart_quantity"
                  />
                </div>
              </div>

              <div className="product-details my-4">
                <p className="details-title text-color mb-1">Product Details</p>
                <p className="description">{product.description}</p>
              </div>

              <div className="row questions bg-light p-3">
                <div className="col-md-1 icon">
                  <i className="fa-brands fa-rocketchat questions-icon"></i>
                </div>
                <div className="col-md-11 text">
                  Have a question about our products at E-Store? Feel free to
                  contact our representatives via live chat or email.
                </div>
              </div>

              <div className="delivery my-4">
                <p className="font-weight-bold mb-0">
                  <span>
                    <i className="fa-solid fa-truck"></i>
                  </span>{" "}
                  <b>Delivery done in 3 days from date of purchase</b>
                </p>
                <p className="text-secondary">
                  Order now to get this product delivery
                </p>
              </div>
              <div className="delivery-options my-4">
                <p className="font-weight-bold mb-0">
                  <span>
                    <i className="fa-solid fa-filter"></i>
                  </span>{" "}
                  <b>Delivery options</b>
                </p>
                <p className="text-secondary">View delivery options here</p>
              </div>
            </div>
            <div className="my-4">
              <div className="row">
                <div className="col-md-4">
                  <h5 className="font-weight-bold mb-0">Available Stock:</h5>
                  <p>{product.available_stock}</p>
                </div>
                <div className="col-md-4">
                  <h5 className="font-weight-bold mb-0">Number of Orders:</h5>
                  <p>{product.number_of_orders}</p>
                </div>
                <div className="col-md-4">
                  <h5 className="font-weight-bold mb-0">Revenue:</h5>
                  <p>{product.revenue}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
