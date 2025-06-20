import { useCallback, useEffect, useMemo, useState } from "react";
import MenuCategories from "../../components/MenuCategories/MenuCategories";
import FoodItem from "../../components/FoodItem/FoodItem";
import OrderSummary from "../../components/OrderSummary/OrderSummary";
import styles from "./RestaurantOrderSystemPage.module.css";
import { useProducts } from "../../hooks/productHooks";
import { useCategories } from "../../hooks/categoryHooks";
import Loading from "../../components/Loading/Loading";
import Pagination from "../../components/Pagination/Pagination";
import { useNavigate, useSearchParams } from "react-router-dom";
import { EMPLOYEE_PLACE_ORDERS_URI } from "../../constants/routes";
import { useOrderActions } from "../../hooks/orderHooks";
import { useAlert } from "../../context/AlertContext";
import { useModal } from "../../context/ModalContext";
import { debounce } from "lodash";
import {
  ORDER_TYPES,
  TABLE_OCCUPANCY_STATUSES,
} from "../../constants/webConstant";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useTableOccupancies, useTableOccupancy } from "../../hooks/tableOccupancyHooks";
import SelectableWaitingList from "../../components/WaitingList/SelectableWaitingList";
import { Search } from "lucide-react";

export default function RestaurantOrderSystem() {
  const navigate = useNavigate();
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [orderItems, setOrderItems] = useState([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const [currentPage, setCurrentPage] = useState(parseInt(searchParams.get("page")) || 1);
  const categoryId = searchParams.get("categoryId") || "";
  const [searchValue, setSearchValue] = useState(
    searchParams.get("searchValue") || ""
  );

  const { showNewAlert } = useAlert();
  const { onOpen } = useModal();

  const debouncedSearch = useCallback(() =>
    debounce((newSearchValue) => {
      searchParams.set("searchValue", newSearchValue);
      searchParams.set("page", "1");
      setSearchParams(searchParams);
      setCurrentPage(1);
    }, 300),
    [setSearchParams, searchParams]
  );

  const handleSearchInputChange = (e) => {
    const newSearchValue = e.target.value;
    setSearchValue(newSearchValue);
    debouncedSearch(newSearchValue);
  };

  // Fetch waiting lists
  const {
    data: occupanciesData,
    isLoading: loadingOccupancies,
    error: occupanciesError,
    mutate: mutateOccupancies,
  } = useTableOccupancies({
    page: 0,
    size: 20,
    status: TABLE_OCCUPANCY_STATUSES.SEATED,
  });
  const waitingList = useMemo(
    () => occupanciesData?.content || [],
    [occupanciesData]
  );

  // Fetch customer-specific data only when a customer is selected
  const {
    data: occupancyData,
    isLoading: loadingOccupancy,
    error: occupancyError,
  } = useTableOccupancy(selectedCustomer?.id || null);

  // Fetch products
  const {
    data: productsData,
    error: productsError,
  } = useProducts({
    page: currentPage - 1,
    categoryId,
    search: searchValue,
  });
  const menuItems = useMemo(() => productsData?.content || [], [productsData]);
  const totalPages = useMemo(() => productsData?.totalPages || 0, [productsData]);

  // Fetch categories
  const {
    data: categoriesData,
    isLoading: loadingCategories,
    error: categoriesError,
  } = useCategories();
  const categories = useMemo(
    () => categoriesData?.content || [],
    [categoriesData]
  );

  // Order actions
  const {
    handlePlaceOrder,
    placeSuccess,
    placeError,
    resetPlace,
    handleUpdateOrder,
    updateError,
    updateLoading,
    updateSuccess,
    resetUpdate,
  } = useOrderActions();

  const clearOrder = useCallback(() => {
    setOrderItems([]);
    setSelectedCustomer(null);
  }, []);

  // Handle place order success
  useEffect(() => {
    if (placeSuccess) {
      showNewAlert({
        message: placeSuccess,
        action: resetPlace,
      });
      clearOrder();
      navigate(`${EMPLOYEE_PLACE_ORDERS_URI}?page=1`);
    }
  }, [placeSuccess, resetPlace, showNewAlert, navigate, clearOrder]);

  // Handle place order error
  useEffect(() => {
    if (placeError?.message) {
      showNewAlert({
        message: placeError.message,
        variant: "danger",
        action: resetPlace,
      });
    }
  }, [placeError, resetPlace, showNewAlert]);

  // Handle update order success
  useEffect(() => {
    if (updateSuccess) {
      showNewAlert({
        message: updateSuccess,
        action: resetUpdate,
      });
      clearOrder();
      navigate(`${EMPLOYEE_PLACE_ORDERS_URI}?page=1`);
    }
  }, [updateSuccess, resetUpdate, showNewAlert, navigate, clearOrder]);

  // Handle update order error
  useEffect(() => {
    if (updateError?.message) {
      showNewAlert({
        message: updateError.message,
        variant: "danger",
        action: resetUpdate,
      });
    }
  }, [updateError, resetUpdate, showNewAlert]);

  // Handle waiting list data error
  useEffect(() => {
    if (occupancyError) {
      showNewAlert({
        message:
          occupancyError.message || "Failed to load customer order details",
        variant: "danger",
      });
      setSelectedCustomer(null);
      setOrderItems([]);
    }
  }, [occupancyError, showNewAlert]);

  // Clear order items when no customer is selected
  useEffect(() => {
    if (!selectedCustomer) {
      setOrderItems([]);
    }
  }, [selectedCustomer]);

  // Load order details when customer is selected
  useEffect(() => {
    if (occupancyData?.order?.orderDetails && selectedCustomer) {
      const loadedOrderItems = occupancyData.order.orderDetails.map(
        (detail) => ({
          id: detail.id,
          food: {
            productId: detail.product.productId,
            ...(menuItems.find(
              (item) => item.productId === detail.product.productId
            ) || detail.product),
          },
          quantity: detail.quantity,
        })
      );
      setOrderItems(loadedOrderItems);
    } else if (
      occupancyData &&
      !occupancyData?.order?.orderDetails &&
      selectedCustomer
    ) {
      setOrderItems([]);
    }
  }, [occupancyData, menuItems, selectedCustomer]);

  // Validate selected customer
  useEffect(() => {
    if (
      selectedCustomer &&
      !waitingList.find((item) => item.id === selectedCustomer.id)
    ) {
      setSelectedCustomer(null);
      setOrderItems([]);
      showNewAlert({
        message: "Selected customer is no longer in the waiting list",
        variant: "warning",
      });
    }
  }, [waitingList, selectedCustomer, showNewAlert]);

  const addToOrder = useCallback(
    (food) => {
      const existingItem = orderItems.find(
        (item) => item.food.productId === food.productId
      );
      if (existingItem) {
        setOrderItems(
          orderItems.map((item) =>
            item.food.productId === food.productId
              ? { ...item, quantity: item.quantity + 1 }
              : item
          )
        );
      } else {
        setOrderItems([...orderItems, { food, quantity: 1 }]);
      }
    },
    [orderItems]
  );

  const updateQuantity = useCallback(
    (foodId, quantity) => {
      if (quantity <= 0) {
        setOrderItems(orderItems.filter((item) => item.food.productId !== foodId));
      } else {
        setOrderItems(
          orderItems.map((item) =>
            item.food.productId === foodId ? { ...item, quantity } : item
          )
        );
      }
    },
    [orderItems]
  );

  const showPlaceOrderModal = () => {
    if (!selectedCustomer?.id) {
      showNewAlert({
        message: "Please select a customer from the waiting list first!",
      });
      return;
    }
    if (orderItems.length === 0) {
      showNewAlert({
        message: "Please add items to the order!",
      });
      return;
    }
    const isUpdate = !!occupancyData?.order?.orderId;
    onOpen({
      title: isUpdate ? "Update Order" : "Place Order",
      message: isUpdate
        ? "Do you want to update this order?"
        : "Do you want to place this order?",
      onYes: placeOrder,
    });
  };

  const placeOrder = useCallback(async () => {
    if (!selectedCustomer?.id || orderItems.length === 0) return;

    const order = {
      tableOccupancyId: selectedCustomer.id,
      orderDetails: orderItems.map((item) => ({
        id: item.id,
        productId: item.food.productId,
        quantity: item.quantity,
      })),
      type: ORDER_TYPES.DINE_IN
    };

    let response;
    if (occupancyData?.order?.orderId) {
      // Update existing order
      response = await handleUpdateOrder({
        orderId: occupancyData.order.orderId,
        ...order,
      });
    } else {
      // Place new order
      response = await handlePlaceOrder(order, ORDER_TYPES.DINE_IN);
    }

    if (response) {
      clearOrder();
      navigate(`${EMPLOYEE_PLACE_ORDERS_URI}?page=1`);
    }
  }, [
    selectedCustomer,
    orderItems,
    occupancyData,
    handlePlaceOrder,
    handleUpdateOrder,
    clearOrder,
    navigate,
  ]);

  const showClearModal = () => {
    onOpen({
      title: "Clear Order",
      message: "Do you want to clear this order?",
      onYes: clearOrder,
    });
  };

  const handleSelectCategory = useCallback(
    (categoryId) => {
      navigate(`${EMPLOYEE_PLACE_ORDERS_URI}?categoryId=${categoryId}&page=1`);
    },
    [navigate]
  );

  if (loadingCategories || loadingOccupancies) {
    return <Loading />;
  }

  if (categoriesError || occupanciesError || productsError || occupancyError) {
    const errors = [
      { source: "Categories", message: categoriesError?.message },
      { source: "Waiting List", message: occupanciesError?.message },
      { source: "Products", message: productsError?.message },
      { source: "Customer Order", message: occupancyError?.message },
    ].filter((e) => e.message);
    return (
      <div>
        {errors.map((error, index) => (
          <ErrorDisplay key={index} message={`${error.source}: ${error.message}`} />
        ))}
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h1>Restaurant Order System</h1>
      </div>
      <div className={styles.content}>
        <div className={styles.menuSection}>
          <div className={styles.card}>
            <div className={styles.cardHeader}>
              <div className="d-flex justify-content-between">
                <h4>Menu</h4>
                <div className={styles.searchContainer}>
                  <Search className={styles.searchIcon} />
                  <input
                    type="text"
                    placeholder="Search products..."
                    className={styles.searchInput}
                    value={searchValue}
                    onChange={handleSearchInputChange}
                  />
                </div>
              </div>
            </div>
            <div className={styles.cardBody}>
              <MenuCategories
                categories={[{ categoryId: "", categoryName: "All" }, ...categories]}
                selectedCategoryId={categoryId}
                onSelectCategory={handleSelectCategory}
              />
              <hr className={styles.divider} />
              <div>
                <div className={styles.foodGrid}>
                  {menuItems.map(food => (
                    <FoodItem food={food} onAddToOrder={addToOrder}/>
                  ))}
                </div>
                <Pagination currentPage={currentPage} totalPages={totalPages} />
              </div>
            </div>
          </div>
        </div>
        <div className={styles.orderSection}>
          <div className={styles.card}>
            <div className={styles.cardHeader}>
              <h4>
                Order Summary{" "}
                {selectedCustomer
                  ? `- Customer: ${selectedCustomer.contactName}`
                  : ""}
              </h4>
            </div>
            <div className={styles.cardBody}>
              {updateLoading || loadingOccupancy ? (
                <Loading />
              ) : (
                <OrderSummary
                  items={orderItems}
                  onUpdateQuantity={updateQuantity}
                />
              )}
            </div>
            <div className={styles.cardFooter}>
              <button
                className={`${styles.button} ${styles.buttonSuccess}`}
                onClick={showPlaceOrderModal}
                disabled={
                  !selectedCustomer ||
                  orderItems.length === 0 ||
                  updateLoading ||
                  loadingOccupancy
                }
                aria-label={
                  occupancyData?.order?.orderId
                    ? "Update order for selected customer"
                    : "Place order for selected customer"
                }
              >
                {occupancyData?.order?.orderId ? "Update Order" : "Place Order"}
              </button>
              <button
                className={`${styles.button} ${styles.buttonSecondary}`}
                onClick={showClearModal}
                disabled={orderItems.length === 0 || updateLoading}
                aria-label="Clear current order"
              >
                Clear Order
              </button>
            </div>
          </div>
          <div className={styles.waitingListSection}>
            <div className={styles.card}>
              <div className={styles.cardHeader}>
                <h2 className={styles.cardTitle}>Waiting List</h2>
              </div>
              <div className={styles.cardContent}>
                <p className={styles.waitingCount}>
                  {waitingList.length} parties waiting
                </p>
                {waitingList.length === 0 ? (
                  <p>No customers in the waiting list</p>
                ) : (
                  <SelectableWaitingList
                    waitingList={waitingList}
                    mutate={mutateOccupancies}
                    selectedCustomer={selectedCustomer}
                    setSelectedCustomer={setSelectedCustomer}
                  />
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}