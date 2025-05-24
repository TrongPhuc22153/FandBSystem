import React, { useCallback, useEffect, useState } from "react";
import styles from "./ReservationPage.module.css";
import { useProducts } from "../../hooks/productHooks";
import { useReservationTables } from "../../hooks/tableHooks";
import Loading from "../../components/Loading/Loading";
import Pagination from "../../components/Pagination/Pagination";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useSearchParams } from "react-router-dom";
import { getImageSrc } from "../../utils/imageUtils";
import { useModal } from "../../context/ModalContext";
import { useAlert } from "../../context/AlertContext";

function ReservationPage() {
  // State for form inputs, error, and current tab
  const [formData, setFormData] = useState({
    startTime: "",
    endTime: "",
    numberOfGuests: 1,
    notes: "",
    tableId: "AUTO",
    menuItems: [],
  });
  const [timeError, setTimeError] = useState("");
  const [currentTab, setCurrentTab] = useState(0);

  const [searchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  const {
    data: menuItemsData,
    isLoading: loadingMenuItems,
    error: menuItemsError,
  } = useProducts({ page: currentPage, pageSize: 2 });
  const {
    data: tablesData,
    isLoading: loadingTables,
    error: tablesError,
  } = useReservationTables({ pageNumber: 0, pageSize: 10 });

  const { onOpen } = useModal();
  const { showNewAlert } = useAlert();

  const tables = tablesData?.content || [];
  const menuItems = menuItemsData?.content || [];
  const totalPages = menuItemsData?.totalPages || 0;

  // Initialize menuItems in formData when menuItemsData is available
  useEffect(() => {
    if (menuItems.length > 0 && formData.menuItems.length === 0) {
      setFormData((prev) => ({
        ...prev,
        menuItems: menuItems.map((item) => ({
          productId: item.productId,
          quantity: 0,
        })),
      }));
    }
  }, [menuItems]);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
    const newSearchParams = new URLSearchParams(searchParams);
    newSearchParams.set("page", pageNumber);
    window.history.pushState(null, "", `?${newSearchParams.toString()}`);
  };

  // Get current date and time
  const getCurrentDateTime = () => {
    const now = new Date("2025-05-04T00:00:00");
    return now.toISOString().slice(0, 16); // Format for datetime-local
  };

  // Handle input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    const now = new Date("2025-05-04T00:00:00");

    if (name === "startTime" && value) {
      // Validate startTime is not in the past
      const start = new Date(value);
      if (start < now) {
        setTimeError("Start time cannot be in the past.");
        setFormData((prev) => ({ ...prev, startTime: "", endTime: "" }));
        return;
      }
      // Calculate default endTime (startTime + 3 hours)
      const end = new Date(start);
      end.setHours(start.getHours() + 3);
      setFormData((prev) => ({
        ...prev,
        startTime: value,
        endTime: end.toISOString().slice(0, 16), // Format for datetime-local
      }));
      setTimeError("");
    } else if (name === "endTime" && value && formData.startTime) {
      // Validate endTime is not before startTime and not in the past
      const start = new Date(formData.startTime);
      const end = new Date(value);
      if (end < start) {
        setTimeError("End time cannot be before start time.");
        setFormData((prev) => ({ ...prev, endTime: "" }));
      } else if (end < now) {
        setTimeError("End time cannot be in the past.");
        setFormData((prev) => ({ ...prev, endTime: "" }));
      } else {
        setFormData((prev) => ({ ...prev, endTime: value }));
        setTimeError("");
      }
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
      if (name === "endTime") {
        setTimeError("");
      }
    }
  };

  // Handle menu item quantity changes
  const handleQuantityChange = (productId, quantity) => {
    setFormData((prev) => ({
      ...prev,
      menuItems: prev.menuItems.map((item) =>
        item.productId === productId
          ? { ...item, quantity: parseInt(quantity) || 0 }
          : item
      ),
    }));
  };

  // Add item to cart (increment quantity by 1)
  const handleAddToCart = (productId) => {
    setFormData((prev) => ({
      ...prev,
      menuItems: prev.menuItems.map((item) =>
        item.productId === productId
          ? { ...item, quantity: item.quantity + 1 }
          : item
      ),
    }));
  };

  // Remove item from cart (set quantity to 0)
  const handleRemoveFromCart = (productId) => {
    setFormData((prev) => ({
      ...prev,
      menuItems: prev.menuItems.map((item) =>
        item.productId === productId ? { ...item, quantity: 0 } : item
      ),
    }));
  };

  // Clear cart (set all quantities to 0)
  const handleClearCart = () => {
    setFormData((prev) => ({
      ...prev,
      menuItems: prev.menuItems.map((item) => ({ ...item, quantity: 0 })),
    }));
  };

  // Calculate total cart cost
  const calculateTotalCost = () => {
    return formData.menuItems
      .reduce((total, item) => {
        const product = menuItems.find((p) => p.productId === item.productId);
        return total + (product ? item.quantity * product.unitPrice : 0);
      }, 0)
      .toFixed(2);
  };

  // Validate that startTime and endTime are on the same day
  const sameDayValidation = (start, end) => {
    if (!start || !end) return true; // Skip if either is empty
    const startDate = new Date(start);
    const endDate = new Date(end);
    return (
      startDate.getFullYear() === endDate.getFullYear() &&
      startDate.getMonth() === endDate.getMonth() &&
      startDate.getDate() === endDate.getDate()
    );
  };

  // Validate that endTime is not before startTime
  const isEndTimeValid = (start, end) => {
    if (!start || !end) return true; // Skip if either is empty
    return new Date(end) >= new Date(start);
  };

  // Validate that startTime and endTime are not in the past
  const isNotPast = (time) => {
    if (!time) return true; // Skip if empty
    const now = new Date("2025-05-04T00:00:00");
    return new Date(time) >= now;
  };

  // Get min and max for endTime based on startTime
  const getEndTimeConstraints = () => {
    if (!formData.startTime) return { min: getCurrentDateTime(), max: "" };
    const start = new Date(formData.startTime);
    const now = new Date("2025-05-04T00:00:00");
    const min =
      start > now
        ? start.toISOString().slice(0, 16)
        : now.toISOString().slice(0, 16);
    const max = new Date(start);
    max.setHours(23, 59); // End of the same day
    return { min, max: max.toISOString().slice(0, 16) };
  };

  // Validate current tab's required fields
  const validateTab = (tabIndex) => {
    if (tabIndex === 0) {
      // Reservation Details: startTime, endTime, numberOfGuests, tableId
      if (
        !formData.startTime ||
        !formData.endTime ||
        !formData.numberOfGuests
      ) {
        showNewAlert({
          message: "Please fill in all required fields in Reservation Details.",
          variant: "danger",
        });
        return false;
      }
      if (
        formData.tableId !== "AUTO" &&
        !tables.some((table) => table.tableId === formData.tableId)
      ) {
        showNewAlert({
          message: "Please select a valid table or choose Auto-Assign.",
          variant: "danger",
        });
        return false;
      }
      if (!isNotPast(formData.startTime)) {
        setTimeError("Start time cannot be in the past.");
        return false;
      }
      if (!isNotPast(formData.endTime)) {
        setTimeError("End time cannot be in the past.");
        return false;
      }
      if (!sameDayValidation(formData.startTime, formData.endTime)) {
        setTimeError("Start time and end time must be on the same day.");
        return false;
      }
      if (!isEndTimeValid(formData.startTime, formData.endTime)) {
        setTimeError("End time cannot be before start time.");
        return false;
      }
    } else if (tabIndex === 1) {
      // Cart: At least one item must have quantity > 0
      if (!formData.menuItems.some((item) => item.quantity > 0)) {
        showNewAlert({
          message: "Please add at least one item to the menu.",
          variant: "danger",
        });
        return false;
      }
    }
    return true;
  };

  // Handle tab navigation
  const handleNext = () => {
    if (validateTab(currentTab)) {
      setCurrentTab((prev) => Math.min(prev + 1, 1));
    }
  };

  const handlePrevious = () => {
    setCurrentTab((prev) => Math.max(prev - 1, 0));
  };

  const handleTabClick = (tabIndex) => {
    // Allow backward navigation or same tab without validation
    if (tabIndex <= currentTab) {
      setCurrentTab(tabIndex);
    } else {
      // Validate all tabs up to the target tab
      for (let i = currentTab; i < tabIndex; i++) {
        if (!validateTab(i)) {
          return;
        }
      }
      setCurrentTab(tabIndex);
    }
  };

  const handlePlaceReservation = useCallback(() => {
    // Validate all tabs
    for (let i = 0; i <= 1; i++) {
      if (!validateTab(i)) {
        setCurrentTab(i);
        return;
      }
    }

    // Construct JSON output
    const selectedTable =
      formData.tableId === "AUTO"
        ? null
        : tables.find((table) => table.tableId === formData.tableId);
    const selectedMenuItems = formData.menuItems
      .filter((item) => item.quantity > 0)
      .map((item, index) => {
        const product = menuItems.find((p) => p.productId === item.productId);
        return {
          id: `ITEM${index + 1}`,
          product: {
            productId: product.productId,
            productName: product.productName,
            unitPrice: product.unitPrice,
            unitsInStock: 100, // Placeholder
            picture: product.picture,
            description: product.description,
            category: {
              categoryId: product.category.categoryId,
              categoryName: product.category.categoryName,
              description: "Placeholder",
              picture: "",
            },
          },
          price: product.unitPrice,
          quantity: item.quantity,
        };
      });

    const reservationJson = {
      reservationId: `RES${Date.now()}`, // Placeholder ID
      numberOfGuests: parseInt(formData.numberOfGuests),
      startTime: new Date(formData.startTime).toISOString(),
      endTime: new Date(formData.endTime).toISOString(),
      notes: formData.notes,
      status: "PENDING",
      tableId: formData.tableId, // "AUTO" or specific tableId
      ...(selectedTable && {
        table: {
          tableId: selectedTable.tableId,
          tableNumber: selectedTable.tableNumber,
          location: selectedTable.location,
          status: "OCCUPIED",
          capacity: selectedTable.capacity,
        },
      }),
      menuItems: selectedMenuItems,
      // Note: If tableId is "AUTO", the API should assign an available table based on numberOfGuests, startTime, and endTime
      // Note: Customer information is assumed to be handled server-side via authentication
    };
    showNewAlert({
      message: "Reservation submitted successfully!",
    });
  }, []);

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();

    onOpen({
      title: "Confirm Reservation",
      message: "Do you want to submit the reservation?",
      onYes: handlePlaceReservation,
    });
  };

  // Handle loading and error states
  if (loadingMenuItems || loadingTables) {
    return <Loading />;
  }

  if (menuItemsError || tablesError) {
    return (
      <div>
        {menuItemsError && <ErrorDisplay error={menuItemsError.message} />}
        {tablesError && <ErrorDisplay error={tablesError.message} />}
      </div>
    );
  }

  const { min: endTimeMin, max: endTimeMax } = getEndTimeConstraints();
  const currentDateTime = getCurrentDateTime();

  return (
    <div className={styles.container}>
      <h2 className={styles.formHeading}>Make a Reservation</h2>
      <div className={styles.tabContainer}>
        <div
          className={`${styles.tab} ${
            currentTab === 0 ? styles.tabActive : ""
          }`}
          onClick={() => handleTabClick(0)}
        >
          Reservation Details
        </div>
        <div
          className={`${styles.tab} ${
            currentTab === 1 ? styles.tabActive : ""
          }`}
          onClick={() => handleTabClick(1)}
        >
          Cart
        </div>
      </div>
      <form onSubmit={handleSubmit} className="space-y-6">
        {currentTab === 0 && (
          <div className={styles.section}>
            <h3 className={styles.sectionTitle}>Reservation Details</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className={styles.inputGroup}>
                <label className={styles.inputLabel}>Start Time *</label>
                <input
                  type="datetime-local"
                  name="startTime"
                  value={formData.startTime}
                  onChange={handleInputChange}
                  min={currentDateTime}
                  className={styles.inputField}
                  required
                />
              </div>
              <div className={styles.inputGroup}>
                <label className={styles.inputLabel}>
                  End Time * (Defaults to 3 hours later)
                </label>
                <input
                  type="datetime-local"
                  name="endTime"
                  value={formData.endTime}
                  onChange={handleInputChange}
                  min={endTimeMin}
                  max={endTimeMax}
                  className={styles.inputField}
                  required
                />
                {timeError && (
                  <p className={styles.errorMessage}>{timeError}</p>
                )}
              </div>
              <div className={styles.inputGroup}>
                <label className={styles.inputLabel}>Number of Guests *</label>
                <input
                  type="number"
                  name="numberOfGuests"
                  value={formData.numberOfGuests}
                  onChange={handleInputChange}
                  min="1"
                  className={styles.inputField}
                  required
                />
              </div>
            </div>
            <div className="mt-4">
              <label className={styles.inputLabel}>Table *</label>
              <select
                name="tableId"
                value={formData.tableId}
                onChange={handleInputChange}
                className={`${styles.inputField} ${styles.selectField}`}
                required
              >
                <option value="AUTO">Auto-Assign Table</option>
                {tables.length > 0 ? (
                  tables.map((table) => (
                    <option key={table.tableId} value={table.tableId}>
                      Table {table.tableNumber} ({table.location}, Capacity:{" "}
                      {table.capacity})
                    </option>
                  ))
                ) : (
                  <option value="" disabled>
                    No tables available
                  </option>
                )}
              </select>
            </div>
            <div className="mt-4">
              <label className={styles.inputLabel}>Notes</label>
              <textarea
                name="notes"
                value={formData.notes}
                onChange={handleInputChange}
                className={`${styles.inputField} ${styles.textareaField}`}
                rows="4"
              />
            </div>
            <div className={styles.buttonGroup}>
              <div></div> {/* Empty div for spacing */}
              <button
                type="button"
                className={styles.nextButton}
                onClick={handleNext}
              >
                Next
              </button>
            </div>
          </div>
        )}
        {currentTab === 1 && (
          <div className={styles.section}>
            {menuItems.length > 0 ? (
              <>
                <h4 className="text-lg font-semibold mb-4">Menu Items</h4>
                <div className="space-y-4">
                  {menuItems.map((item) => (
                    <div key={item.productId} className={styles.menuItem}>
                      <img
                        src={item.picture || getImageSrc()}
                        alt={item.productName}
                        className={styles.menuItemImage}
                      />
                      <div className="flex-1">
                        <p className={styles.menuItemName}>
                          {item.productName}
                        </p>
                        <p className={styles.menuItemPrice}>
                          ${item.unitPrice.toFixed(2)}
                        </p>
                        <p className={styles.menuItemDescription}>
                          {item.description}
                        </p>
                      </div>
                      <button
                        type="button"
                        className={`${styles.addButton} ms-auto`}
                        onClick={() => handleAddToCart(item.productId)}
                      >
                        Add to Cart
                      </button>
                    </div>
                  ))}
                  {totalPages > 1 && (
                    <Pagination
                      currentPage={currentPage + 1}
                      totalPages={totalPages}
                      onPageChange={handlePageChange}
                    />
                  )}
                </div>
                <h4 className="text-lg font-semibold mt-6 mb-4">
                  Menu Summary
                </h4>
                <button
                  type="button"
                  className={styles.clearButton}
                  onClick={handleClearCart}
                >
                  Clear Menu
                </button>
                {formData.menuItems.some((item) => item.quantity > 0) ? (
                  <>
                    <table className={styles.cartTable}>
                      <thead>
                        <tr className={styles.cartTableHeader}>
                          <th className={styles.cartTableCell}>Item Name</th>
                          <th className={styles.cartTableCell}>Quantity</th>
                          <th className={styles.cartTableCell}>Unit Price</th>
                          <th className={styles.cartTableCell}>Total Price</th>
                          <th className={styles.cartTableCell}>Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        {formData.menuItems
                          .filter((item) => item.quantity > 0)
                          .map((item) => {
                            const product = menuItems.find(
                              (p) => p.productId === item.productId
                            );
                            return (
                              <tr
                                key={item.productId}
                                className={styles.cartTableRow}
                              >
                                <td className={styles.cartTableCell}>
                                  {product.productName}
                                </td>
                                <td className={styles.cartTableCell}>
                                  <input
                                    type="number"
                                    min="0"
                                    value={item.quantity}
                                    onChange={(e) =>
                                      handleQuantityChange(
                                        item.productId,
                                        e.target.value
                                      )
                                    }
                                    className={`${styles.inputField} ${styles.quantityInput}`}
                                  />
                                </td>
                                <td className={styles.cartTableCell}>
                                  ${product.unitPrice.toFixed(2)}
                                </td>
                                <td className={styles.cartTableCell}>
                                  $
                                  {(item.quantity * product.unitPrice).toFixed(
                                    2
                                  )}
                                </td>
                                <td className={styles.cartTableCell}>
                                  <button
                                    type="button"
                                    className={styles.removeButton}
                                    onClick={() =>
                                      handleRemoveFromCart(item.productId)
                                    }
                                  >
                                    Remove
                                  </button>
                                </td>
                              </tr>
                            );
                          })}
                      </tbody>
                    </table>
                    <p className={styles.cartTotal}>
                      Total: ${calculateTotalCost()}
                    </p>
                  </>
                ) : (
                  <p>Your menu is empty.</p>
                )}
              </>
            ) : (
              <p>No menu items available.</p>
            )}
            <div className={styles.buttonGroup}>
              <button
                type="button"
                className={styles.previousButton}
                onClick={handlePrevious}
              >
                Previous
              </button>
              <button type="submit" className={styles.submitButton}>
                Submit Reservation
              </button>
            </div>
          </div>
        )}
      </form>
    </div>
  );
}

export default ReservationPage;
