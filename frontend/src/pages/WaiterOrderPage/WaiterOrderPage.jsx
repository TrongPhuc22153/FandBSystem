import React, { useState, useEffect } from "react";
import styles from "./WaiterOrderpage.module.css"; // Create this CSS module
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faSearch,
  faPlus,
  faMinus,
  faArrowRight,
} from "@fortawesome/free-solid-svg-icons";

const WaiterOrderPage = () => {
  const [tables, setTables] = useState([]); // Array of available tables
  const [selectedTable, setSelectedTable] = useState(null);
  const [menu, setMenu] = useState({}); // Object with categories as keys and arrays of items as values
  const [searchTerm, setSearchTerm] = useState("");
  const [cart, setCart] = useState([]); // Array of items in the current order
  const [specialRequests, setSpecialRequests] = useState("");
  const [orderStatus, setOrderStatus] = useState("Pending"); // Example status

  // Mock data - replace with your actual API calls
  useEffect(() => {
    // Fetch available tables
    setTables([
      { id: 1, name: "Table 1" },
      { id: 2, name: "Table 2" },
      { id: 3, name: "Table 3" },
      { id: 4, name: "Table 4" },
    ]);

    // Fetch menu
    setMenu({
      Appetizers: [
        { id: "app1", name: "Garlic Bread", price: 4.99 },
        { id: "app2", name: "Soup of the Day", price: 5.99 },
      ],
      Mains: [
        { id: "main1", name: "Burger", price: 12.99 },
        { id: "main2", name: "Pasta Carbonara", price: 14.99 },
      ],
      Desserts: [{ id: "dessert1", name: "Chocolate Cake", price: 7.99 }],
      Drinks: [{ id: "drink1", name: "Soda", price: 2.99 }],
    });
  }, []);

  const handleTableSelect = (tableId) => {
    setSelectedTable(tableId);
    // Optionally fetch existing order for the table
    setCart([]); // Clear cart when selecting a new table for simplicity
  };

  const handleTransferTable = (newTableId) => {
    if (selectedTable && newTableId) {
      console.log(
        `Transferring order from Table ${selectedTable} to Table ${newTableId}`
      );
      // Implement your logic to transfer the order
      setSelectedTable(newTableId);
      // Optionally update UI after transfer
    }
  };

  const handleAddToOrder = (item) => {
    const existingItem = cart.find((cartItem) => cartItem.id === item.id);
    if (existingItem) {
      setCart(
        cart.map((cartItem) =>
          cartItem.id === item.id
            ? { ...cartItem, quantity: cartItem.quantity + 1 }
            : cartItem
        )
      );
    } else {
      setCart([...cart, { ...item, quantity: 1 }]);
    }
  };

  const handleIncreaseQuantity = (itemId) => {
    setCart(
      cart.map((cartItem) =>
        cartItem.id === itemId
          ? { ...cartItem, quantity: cartItem.quantity + 1 }
          : cartItem
      )
    );
  };

  const handleDecreaseQuantity = (itemId) => {
    setCart(
      cart.map((cartItem) =>
        cartItem.id === itemId
          ? { ...cartItem, quantity: Math.max(1, cartItem.quantity - 1) }
          : cartItem
      )
    );
  };

  const handleRemoveFromOrder = (itemId) => {
    setCart(cart.filter((cartItem) => cartItem.id !== itemId));
  };

  const handleSearch = (event) => {
    setSearchTerm(event.target.value);
  };

  const filteredMenu = Object.keys(menu).reduce((acc, category) => {
    const filteredItems = menu[category].filter((item) =>
      item.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
    if (filteredItems.length > 0) {
      acc[category] = filteredItems;
    }
    return acc;
  }, {});

  const handleSubmitOrder = () => {
    if (selectedTable && cart.length > 0) {
      const orderData = {
        tableId: selectedTable,
        items: cart.map((item) => ({ id: item.id, quantity: item.quantity })),
        specialRequests: specialRequests,
        // ... other relevant details
      };
      console.log("Submitting Order:", orderData);
      // Call your API to submit the order
      // On success, maybe clear the cart and update UI
      setCart([]);
      setOrderStatus("Preparing"); // Example status update
    } else {
      alert("Please select a table and add items to the order.");
    }
  };

  const calculateTotal = () => {
    return cart
      .reduce((total, item) => total + item.price * item.quantity, 0)
      .toFixed(2);
  };

  return (
    <div className={styles.waiterOrderPage}>
      {/* Table Selection Area */}
      <div className={styles.tableSelection}>
        <h2>Select Table</h2>
        <div className={styles.tableButtons}>
          {tables.map((table) => (
            <button
              key={table.id}
              className={selectedTable === table.id ? styles.selected : ""}
              onClick={() => handleTableSelect(table.id)}
            >
              {table.name}
            </button>
          ))}
        </div>
        {selectedTable && (
          <div className={styles.transferTable}>
            <select
              onChange={(e) => handleTransferTable(parseInt(e.target.value))}
            >
              <option value="">Transfer to Table...</option>
              {tables
                .filter((table) => table.id !== selectedTable)
                .map((table) => (
                  <option key={table.id} value={table.id}>
                    {table.name}
                  </option>
                ))}
            </select>
          </div>
        )}
      </div>

      {/* Customer Details (Optional) - Placeholder */}
      {/* <div className={styles.customerDetails}>
                <h2>Customer Notes</h2>
                <textarea placeholder="Special requests, allergies..." />
            </div> */}

      {/* Menu Categories & Quick Search */}
      <div className={styles.menuArea}>
        <h2>Menu</h2>
        <div className={styles.searchBar}>
          <FontAwesomeIcon icon={faSearch} className={styles.searchIcon} />
          <input
            type="text"
            placeholder="Search menu items..."
            value={searchTerm}
            onChange={handleSearch}
          />
        </div>
        <div className={styles.menuCategories}>
          {Object.keys(filteredMenu).map((category) => (
            <div key={category} className={styles.categorySection}>
              <h3>{category}</h3>
              <ul className={styles.categoryItems}>
                {filteredMenu[category].map((item) => (
                  <li
                    key={item.id}
                    className={styles.menuItem}
                    onClick={() => handleAddToOrder(item)}
                  >
                    <span>{item.name}</span>
                    <span className={styles.price}>
                      ${item.price.toFixed(2)}
                    </span>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      </div>

      {/* Order Entry & Cart View */}
      <div className={styles.cartSidebar}>
        <h2>Your Order</h2>
        {cart.length === 0 ? (
          <p>Your cart is empty.</p>
        ) : (
          <ul className={styles.cartItems}>
            {cart.map((item) => (
              <li key={item.id} className={styles.cartItem}>
                <div className={styles.itemDetails}>
                  <span>{item.name}</span>
                  <div className={styles.quantityControls}>
                    <button onClick={() => handleDecreaseQuantity(item.id)}>
                      <FontAwesomeIcon icon={faMinus} />
                    </button>
                    <span>{item.quantity}</span>
                    <button onClick={() => handleIncreaseQuantity(item.id)}>
                      <FontAwesomeIcon icon={faPlus} />
                    </button>
                  </div>
                </div>
                <span className={styles.itemTotal}>
                  ${(item.price * item.quantity).toFixed(2)}
                </span>
                <button
                  className={styles.removeItem}
                  onClick={() => handleRemoveFromOrder(item.id)}
                >
                  &times;
                </button>
              </li>
            ))}
          </ul>
        )}
        {cart.length > 0 && (
          <div className={styles.cartTotal}>
            <strong>Total:</strong> ${calculateTotal()}
          </div>
        )}

        {/* Notes & Special Requests */}
        <div className={styles.specialRequests}>
          <h3>Special Requests</h3>
          <textarea
            placeholder="Dietary restrictions, cooking preferences..."
            value={specialRequests}
            onChange={(e) => setSpecialRequests(e.target.value)}
          />
        </div>

        {/* Submit Order Button */}
        {selectedTable && cart.length > 0 && (
          <button className={styles.submitOrder} onClick={handleSubmitOrder}>
            Submit Order <FontAwesomeIcon icon={faArrowRight} />
          </button>
        )}
        {!selectedTable && cart.length > 0 && (
          <p className={styles.warning}>
            Please select a table to submit the order.
          </p>
        )}
        {selectedTable && cart.length === 0 && (
          <p className={styles.info}>Add items to the order.</p>
        )}
      </div>

      {/* Status Tracking & Kitchen Notifications - Placeholder */}
      {/* <div className={styles.orderStatus}>
                <h2>Order Status</h2>
                <p>Status: {orderStatus}</p>
                {orderStatus !== 'Served' && (
                    <p>Live updates from the kitchen will appear here.</p>
                )}
            </div> */}

      {/* Payment & Checkout (Optional for Waiters) - Placeholder */}
      {/* <div className={styles.paymentArea}>
                <h2>Checkout</h2>
                <button>Bill Later</button>
            </div> */}
    </div>
  );
};

export default WaiterOrderPage;
