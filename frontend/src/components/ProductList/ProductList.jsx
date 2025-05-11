import styles from "./ProductList.module.css";

const ProductList = ({ item, itemType }) => {
  if (!item) {
    return (
      <div>
        <h3 className="text-lg font-semibold mb-2">Products</h3>
        <p className={styles.textMuted}>
          Select an order or reservation to view its products.
        </p>
      </div>
    );
  }

  return (
    <div>
      <h3 className="text-lg font-semibold mb-2">
        Products in {itemType === "order" ? "Order" : "Reservation"}
      </h3>
      {itemType === "order" && item.items && item.items.length > 0 ? (
        <ul className={styles.listGroup}>
          {item.items.map((product) => (
            <li key={product.id} className={styles.listItem}>
              <img
                src={product.image}
                alt={product.name}
                className={styles.productImage}
              />
              <div>
                <h6 className="mb-0">{product.name}</h6>
                <small className="text-muted">
                  Category: {product.category}
                </small>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p className={styles.textMuted}>
          {itemType === "order"
            ? "No products found in the selected order."
            : "No products associated with reservations."}
        </p>
      )}
      {itemType === "reservation" && (
        <div className="mt-4">
          <p>
            <strong>Customer:</strong> {item.customer}
          </p>
          <p>
            <strong>Time:</strong> {item.time}
          </p>
          <p>
            <strong>Guests:</strong> {item.guests}
          </p>
        </div>
      )}
    </div>
  );
};

export default ProductList;
