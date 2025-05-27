import { useCallback, useEffect, useMemo, useState } from "react";
import MenuCategories from "../../components/MenuCategories/MenuCategories";
import FoodItems from "../../components/FoodItems/FoodItems";
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
import {
  ORDER_TYPES,
  WAITING_LIST_STATUSES,
} from "../../constants/webConstant";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { useWaitingList, useWaitingLists } from "../../hooks/waitingListHooks";
import SelectableWaitingList from "../../components/WaitingList/SelectableWaitingList";
import { usePaymentActions } from "../../hooks/paymentHooks";

export default function RestaurantOrderSystem() {
  const navigate = useNavigate();
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [orderItems, setOrderItems] = useState([]);
  const [searchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 1;
  const categoryIdFromURL = searchParams.get("categoryId") || "";
  const searchTermFromURL = searchParams.get("search") || "";
  const [currentPage, setCurrentPage] = useState(currentPageFromURL - 1);

  const { showNewAlert } = useAlert();
  const { onOpen } = useModal();

  // Fetch waiting lists
  const {
    data: waitingListsData,
    isLoading: loadingWaitingListsData,
    error: waitingListsDataError,
    mutate: mutateWaitingList,
  } = useWaitingLists({
    page: 0,
    size: 20,
    status: WAITING_LIST_STATUSES.SEATED,
  });
  const waitingList = useMemo(
    () => waitingListsData?.content || [],
    [waitingListsData]
  );

  // Fetch customer-specific data only when a customer is selected
  const {
    data: waitingListData,
    isLoading: loadingWaitingListData,
    error: waitingListDataError,
  } = useWaitingList({ id: selectedCustomer?.id || null });

  // Fetch products
  const {
    data: productsData,
    isLoading: loadingProductsData,
    error: productsDataError,
  } = useProducts({
    page: currentPage,
    categoryId: categoryIdFromURL,
    search: searchTermFromURL,
  });
  const menuItems = useMemo(() => productsData?.content || [], [productsData]);
  const totalPages = productsData?.totalPages || 0;

  // Fetch categories
  const {
    data: categoriesData,
    isLoading: loadingCategoriesData,
    error: categoriesDataError,
  } = useCategories();
  const categories = useMemo(
    () => categoriesData?.content || [],
    [categoriesData]
  );

  // Order actions
  const { handlePlaceOrder, placeSuccess, placeError, resetPlace } =
    useOrderActions();

  // Sync pagination with URL
  useEffect(() => {
    setCurrentPage(parseInt(searchParams.get("page")) || 1 - 1);
  }, [searchParams]);

  // Handle place order success
  useEffect(() => {
    if (placeSuccess) {
      showNewAlert({
        message: placeSuccess,
        action: resetPlace,
      });
    }
  }, [placeSuccess, resetPlace, showNewAlert]);

  // Handle place order error
  useEffect(() => {
    if (placeError?.message) {
      showNewAlert({
        message: placeError?.message,
        variant: "danger",
        action: resetPlace,
      });
    }
  }, [placeError, resetPlace, showNewAlert]);

  // Handle waiting list data error
  useEffect(() => {
    if (waitingListDataError) {
      showNewAlert({
        message:
          waitingListDataError.message ||
          "Failed to load customer order details",
        variant: "danger",
      });
      setSelectedCustomer(null);
      setOrderItems([]);
    }
  }, [waitingListDataError, showNewAlert]);

  // Clear order items when no customer is selected
  useEffect(() => {
    if (!selectedCustomer) {
      setOrderItems([]);
    }
  }, [selectedCustomer]);

  // Load order details when customer is selected
  useEffect(() => {
    if (waitingListData?.order?.orderDetails && selectedCustomer) {
      const loadedOrderItems = waitingListData.order.orderDetails.map(
        (detail) => ({
          food: {
            productId: detail.product.productId,
            ...(menuItems.find(
              (item) => item.productId === detail.product.productId
            ) || {}),
          },
          quantity: detail.quantity,
        })
      );
      setOrderItems(loadedOrderItems);
    } else if (
      waitingListData &&
      !waitingListData?.order?.orderDetails &&
      selectedCustomer
    ) {
      setOrderItems([]);
    }
  }, [waitingListData, menuItems, selectedCustomer]);

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
        setOrderItems(orderItems.filter((item) => item.food.id !== foodId));
      } else {
        setOrderItems(
          orderItems.map((item) =>
            item.food.id === foodId ? { ...item, quantity } : item
          )
        );
      }
    },
    [orderItems]
  );

  const clearOrder = useCallback(() => {
    setOrderItems([]);
    setSelectedCustomer(null);
  }, []);

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
    onOpen({
      title: "Place order",
      message: "Do you want to place this order?",
      onYes: placeOrder,
    });
  };

  const placeOrder = useCallback(async () => {
    if (!selectedCustomer?.id || orderItems.length === 0) return;
    const order = {
      waitingListId: selectedCustomer.id,
      orderDetails: orderItems.map((item) => ({
        productId: item.food.productId,
        quantity: item.quantity,
      })),
    };
    const response = await handlePlaceOrder(order, ORDER_TYPES.DINE_IN);
    if (response) {
      clearOrder();
      navigate(`${EMPLOYEE_PLACE_ORDERS_URI}?page=0`);
    }
  }, [selectedCustomer, orderItems, clearOrder, handlePlaceOrder, navigate]);

  const showClearModal = () => {
    onOpen({
      title: "Clear order",
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

  if (
    loadingCategoriesData ||
    loadingProductsData ||
    loadingWaitingListsData
  ) {
    return <Loading />;
  }

  if (
    categoriesDataError ||
    waitingListsDataError ||
    productsDataError ||
    waitingListDataError
  ) {
    return (
      <ErrorDisplay
        message={[
          categoriesDataError?.message,
          waitingListsDataError?.message,
          productsDataError?.message,
          waitingListDataError?.message,
        ]
          .filter(Boolean)
          .join("; ")}
      />
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
              <h4>Menu</h4>
            </div>
            <div className={styles.cardBody}>
              <MenuCategories
                categories={[
                  { categoryId: "", categoryName: "All" },
                  ...categories,
                ]}
                selectedCategoryId={categoryIdFromURL}
                onSelectCategory={handleSelectCategory}
              />
              <hr className={styles.divider} />
              <div>
                <FoodItems items={menuItems} onAddToOrder={addToOrder} />
                <Pagination
                  currentPage={currentPage + 1}
                  totalPages={totalPages}
                />
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
              <OrderSummary
                items={orderItems}
                onUpdateQuantity={updateQuantity}
              />
            </div>
            <div className={styles.cardFooter}>
              <button
                className={`${styles.button} ${styles.buttonSuccess}`}
                onClick={showPlaceOrderModal}
                disabled={!selectedCustomer || orderItems.length === 0}
              >
                Place Order
              </button>
              <button
                className={`${styles.button} ${styles.buttonSecondary}`}
                onClick={showClearModal}
                disabled={orderItems.length === 0}
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
                {waitingListsDataError?.message ? (
                  <ErrorDisplay message={waitingListsDataError.message} />
                ) : (
                  <SelectableWaitingList
                    waitingList={waitingList}
                    mutate={mutateWaitingList}
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
