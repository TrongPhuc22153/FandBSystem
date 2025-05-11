import styles from "./ProductDetails.module.css";

const ProductDetails = ({ item }) => {
  if (!item) {
    return (
      <div className={styles.content}>
        <p className={styles.placeholder}>
          Select an order or reservation to view details.
        </p>
      </div>
    );
  }

  return (
    <div className={styles.content}>
      <h3 className="text-lg font-semibold mb-4">
        {item.customer}'s {item.items ? "Order" : "Reservation"} Details
      </h3>
      {item.items ? (
        <div>
          <h4 className="font-semibold mb-2">Products:</h4>
          {item.items.map((product, index) => (
            <div key={index} className={styles.productItem}>
              <img
                src={product.image}
                alt={product.name}
                className={styles.productImage}
              />
              <div>
                <p>
                  <strong>Name:</strong> {product.name}
                </p>
                <p>
                  <strong>Category:</strong> {product.category}
                </p>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div>
          <p>
            <strong>Time:</strong> {item.time}
          </p>
          <p>
            <strong>Guests:</strong> {item.guests}
          </p>
          <p>
            <strong>Note:</strong> No products associated with reservations.
          </p>
        </div>
      )}
    </div>
  );
};

export default ProductDetails;
