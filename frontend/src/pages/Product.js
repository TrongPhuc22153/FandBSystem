import axios from "axios";
import { useEffect, useState } from "react";
import { useParams, useSearchParams } from "react-router";
import { PRODUCT_URL, UPDATE_ITEMS_CART_URL, USER_CART_URL } from "../constants/ApiEndpoints";
import { getDefaultFood } from "../services/ImageService";
import { useAuth } from "../hooks/AuthContext"

const similarProducts = [
  {
    title: "Lovely black dress",
    price: 100,
    image: "https://source.unsplash.com/gsKdPcIyeGg",
  },
  {
    title: "Lovely Dress with patterns",
    price: 85,
    image: "https://source.unsplash.com/sg_gRhbYXhc",
  },
  {
    title: "Lovely fashion dress",
    price: 200,
    image: "https://source.unsplash.com/gJZQcirK8aw",
  },
  {
    title: "Lovely red dress",
    price: 120,
    image: "https://source.unsplash.com/qbB_Z2pXLEU",
  },
];

export default function ProductPage() {
  const { food } = useParams();
  const [productData, setProductData] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchParams] = useSearchParams();
  const productId = parseInt(searchParams.get('id'));
  const [addToCartMessage, setAddToCartMessage] = useState('');
  const [addToCartError, setAddToCartError] = useState('');
  const auth = useAuth();

  useEffect(() => {
    const fetchProduct = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await axios.get(`${PRODUCT_URL}/${productId}`);
        setProductData(res.data);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchProduct();
  }, [food]); // Fetch product details when the 'food' parameter changes

  if (loading) {
    return <p>Loading product details...</p>;
  }

  if (error) {
    return <p>Error fetching product: {error}</p>;
  }

  if (!productData) {
    return <p>No product details found for {food}.</p>;
  }

  const handleQuantityChange = (event) => {
    const value = Math.max(0, Math.min(5, Number(event.target.value))); // Ensure value is between 0 and 5
    setQuantity(value);
  };

  const handleAddToCart = async () => {
    if (!auth.user) {
      setAddToCartError('Please log in to add items to your cart.');
      return;
    }

    setAddToCartMessage('');
    setAddToCartError('');

    const itemToAdd = {
      productId: productData.productId,
      quantity: quantity,
    };

    try {
      const response = await axios.post(UPDATE_ITEMS_CART_URL, JSON.stringify(itemToAdd), {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${auth.token}`,
        },
      });
      if (response.status === 200 || response.status === 201) {
        setAddToCartMessage(`${quantity} x ${productData.productName} added to cart!`);
        setTimeout(() => setAddToCartMessage(''), 3000);
      } else {
        setAddToCartError('Failed to add item to cart.');
        // Optionally log the status code for debugging
        console.error('Add to cart failed with status:', response.status);
      }
    } catch (err) {
      if (err.response && (err.response.status === 401 || err.response.status === 403)) {
        setAddToCartError('Your session might have expired. Please log in again to add to cart.');
        // Optionally, you could redirect the user to the login page here
      } else {
        setAddToCartError(err.message || 'An error occurred while adding to cart.');
      }
    }
  };

  return (
    <>
      <div className="container mb-5" style={{ marginTop: "8rem" }}>
        <div className="row">
          <div className="col-md-5">
            <div className="main-img">
              <img
                className="img-fluid"
                src={productData.picture || getDefaultFood()}
                alt={productData.productName}
              />
              {productData.category && productData.category.picture && (
                <div className="row my-3 previews">
                  <div className="col-md-3">
                    <img className="w-100" src={productData.category.picture || getDefaultFood()} alt="Category Preview" />
                  </div>
                  {/* If you have more preview images in your productData, you would map over them here */}
                </div>
              )}
            </div>
          </div>

          <div className="col-md-7">
            <div className="main-description px-2">
              <div className="category text-bold">
                Category: {productData.category && productData.category.categoryName}
              </div>

              <div className="product-title text-bold my-3">
                {productData.productName}
              </div>

              {/* Assuming these properties exist directly in productData */}
              {productData.oldPrice && productData.discount && productData.newPrice && (
                <div className="price-area my-4">
                  <p className="old-price mb-1">
                    <del>${productData.oldPrice}</del>{" "}
                    <span className="old-price-discount text-danger">
                      ({productData.discount}% off)
                    </span>
                  </p>
                  <p className="new-price text-bold mb-1">${productData.newPrice}</p>
                  <p className="text-secondary mb-1">
                    (Additional tax may apply on checkout)
                  </p>
                </div>
              )}
              {/* If oldPrice, discount, newPrice are not direct properties, adjust accordingly */}
              {!productData.oldPrice && productData.unitPrice && (
                <div className="price-area my-4">
                  <p className="new-price text-bold mb-1">${productData.unitPrice}</p>
                  <p className="text-secondary mb-1">
                    (Price as listed)
                  </p>
                </div>
              )}

              <div className="buttons d-flex my-5">
                <div className="block">
                  {/* <a href="#" className="shadow btn custom-btn">
                    Wishlist
                  </a> */}
                </div>
                <div className="block">
                  <button className="shadow btn custom-btn" onClick={handleAddToCart}>Add to cart</button>
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
                <p className="description">{productData.description}</p>
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

              {/* <div className="delivery my-4">
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
              </div> */}
            </div>
          </div>
        </div>
      </div>

      <div className="container similar-products my-4">
        <hr />
        <p className="display-5">Similar Products</p>

        <div className="row">
          {/* Assuming you have a 'similarProducts' state or prop */}
          {similarProducts && similarProducts.map((similarProduct, index) => (
            <div className="col-md-3" key={index}>
              <div className="similar-product">
                <img className="w-100" src={ getDefaultFood()|| similarProduct.image} alt="Preview" />
                <p className="title">{similarProduct.title}</p>
                <p className="price">${similarProduct.price}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}
