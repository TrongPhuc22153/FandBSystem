import { useCallback, useEffect, useState } from "react";
import TableSelection from "../../components/TableSelection/TableSelection";
import MenuCategories from "../../components/MenuCategories/MenuCategories";
import FoodItems from "../../components/FootItems/FoodItems";
import OrderSummary from "../../components/OrderSummary/OrderSummary";
import styles from "./RestaurantOrderSystemPage.module.css";
import { useReservationTables } from "../../hooks/tableHooks";
import { useProducts } from "../../hooks/productHooks";
import { useCategories } from "../../hooks/categoryHooks";
import Loading from "../../components/Loading/Loading";
import Pagination from "../../components/Pagination/Pagination";
import { useNavigate, useSearchParams } from "react-router-dom";
import { EMPLOYEE_PLACE_ORDERS_URI } from "../../constants/routes";
import { useOrderActions } from "../../hooks/orderHooks";
import { useAlert } from "../../context/AlertContext";
import { useModal } from "../../context/ModalContext";
import { ORDER_TYPES } from "../../constants/webConstant";

export default function RestaurantOrderSystem() {
  const navigate = useNavigate();
  const [selectedTable, setSelectedTable] = useState(null);
  const [orderItems, setOrderItems] = useState([]);

  const [searchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const categoryIdFromURL = searchParams.get("categoryId") || "";
  const searchTermFromURL = searchParams.get("search") || "";

  const [currentPage, setCurrentPage] = useState(currentPageFromURL);

  useEffect(() => {
    const pageFromURL = parseInt(searchParams.get("page")) || 1;
    setCurrentPage(pageFromURL - 1);
  }, [searchParams]);

  const { handlePlaceOrder, placeSuccess, placeError, resetPlace } =
    useOrderActions();

  useEffect(() => {
    if (placeSuccess) {
      showNewAlert({
        message: placeSuccess,
        action: resetPlace,
      });
    }
  }, [placeSuccess, resetPlace]);

    useEffect(() => {
    if (placeError) {
      showNewAlert({
        message: placeError?.message,
        action: resetPlace,
      });
    }
  }, [placeError, resetPlace]);

  //tables
  const {
    data: tablesData,
    isLoading: loadingTablesData,
    error: tablesDataError,
  } = useReservationTables();
  const tables = tablesData?.content || [];

  // products
  const {
    data: productsData,
    isLoading: loadingProductsData,
    error: productsDataError,
  } = useProducts({
    page: currentPage,
    categoryId: categoryIdFromURL,
    search: searchTermFromURL,
  });
  const menuItems = productsData?.content || [];
  const totalPages = productsData?.totalPages || 0;

  // categories
  const {
    data: categoriesData,
    isLoading: loadingCategoriesData,
    error: categoriesError,
  } = useCategories();
  const categories = categoriesData?.content || [];

  const { showNewAlert } = useAlert();
  const { onOpen } = useModal();

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
    setSelectedTable(null);
  }, []);

  const showPlaceOrderModal = () => {
    if (!selectedTable) {
      showNewAlert({
        message: "Please select a table first!",
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
    const order = {
      tableId: selectedTable,
      orderDetails: orderItems.map((item) => {
        return {
          productId: item.food.productId,
          quantity: item.quantity,
        };
      }),
    };

    const response = await handlePlaceOrder(order, ORDER_TYPES.DINE_IN);
    if (response) {
      clearOrder();
    }
  }, [selectedTable, orderItems, clearOrder, handlePlaceOrder]);

  const showClearModal = () => {
    onOpen({
      title: "Clear order",
      message: "Do you want to clear this order?",
      onYes: clearOrder,
    });
  };

  const handleSelectCategory = useCallback((categoryId) => {
    navigate(`${EMPLOYEE_PLACE_ORDERS_URI}?categoryId=${categoryId}`);
  }, []);

  if (
    !categoriesData ||
    loadingCategoriesData ||
    !productsData ||
    loadingProductsData ||
    !tablesData ||
    loadingTablesData
  )
    return <Loading />;

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h1>Restaurant Order System</h1>
      </div>

      <div className={styles.content}>
        <div className={styles.menuSection}>
          <div className={styles.card}>
            <div className={styles.cardHeader}>
              <h4>Table Selection</h4>
            </div>
            <div className={styles.cardBody}>
              <TableSelection
                tables={tables}
                selectedTable={selectedTable}
                onSelectTable={setSelectedTable}
              />
            </div>
          </div>

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
                Order Summary {selectedTable ? `- Table ${selectedTable}` : ""}
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
                disabled={!selectedTable || orderItems.length === 0}
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
        </div>
      </div>
    </div>
  );
}
