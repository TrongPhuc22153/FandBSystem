import { useState, useEffect, useMemo } from 'react';
import styles from './PaymentManagementPage.module.css';
import StaffSearchForm from '../../components/PaymentManagement/StaffSearchForm/StaffSearchForm';
import Loading from '../../components/Loading/Loading';
import StaffOrderList from '../../components/PaymentManagement/StaffOrderList/StaffOrderList';
import { usePayments } from '../../hooks/paymentHooks';
import { PAYMENT_STATUSES } from '../../constants/webConstant';

// --- Dummy Data (Simulate API Response for Unpaid Orders) ---
const dummyUnpaidOrders = [
  {
    id: 'ORD1001',
    tableNumber: '5',
    phoneNumber: '1234567890',
    timestamp: new Date(Date.now() - 3600 * 1000).toISOString(), // 1 hour ago
    items: [
      { name: 'Spaghetti Carbonara', quantity: 1, price: 18.50 },
      { name: 'Coca-Cola', quantity: 2, price: 3.00 },
    ],
    total: 27.00, // Calculated for simplicity
    status: 'Awaiting Payment',
  },
  {
    id: 'ORD1002',
    tableNumber: '12',
    phoneNumber: '0987654321',
    timestamp: new Date(Date.now() - 7200 * 1000).toISOString(), // 2 hours ago
    items: [
      { name: 'Beef Steak', quantity: 1, price: 32.00 },
      { name: 'Red Wine', quantity: 1, price: 10.00 },
    ],
    total: 48.00,
    status: 'Awaiting Payment',
  },
  {
    id: 'ORD1003',
    tableNumber: '7',
    phoneNumber: '', // No phone for this one
    timestamp: new Date(Date.now() - 1800 * 1000).toISOString(), // 30 mins ago
    items: [
      { name: 'Caesar Salad', quantity: 1, price: 9.00 },
      { name: 'Water', quantity: 1, price: 2.00 },
    ],
    total: 13.00,
    status: 'Awaiting Payment',
  },
  {
    id: 'ORD1004',
    tableNumber: '3',
    phoneNumber: '1122334455',
    timestamp: new Date(Date.now() - 900 * 1000).toISOString(), // 15 mins ago
    items: [
      { name: 'Chicken Burger', quantity: 2, price: 14.00 },
      { name: 'Fries', quantity: 2, price: 5.00 },
    ],
    total: 40.00,
    status: 'Awaiting Payment',
  },
  {
    id: 'ORD1005',
    tableNumber: '5', // Same table as ORD1001, to show multiple orders for one table
    phoneNumber: '1234567890',
    timestamp: new Date(Date.now() - 600 * 1000).toISOString(), // 10 mins ago
    items: [
      { name: 'Dessert Platter', quantity: 1, price: 20.00 },
    ],
    total: 25.00,
    status: 'Awaiting Payment',
  },
];
// --- End Dummy Data ---

function PaymentManagementPage() {
  const [allUnpaidOrders, setAllUnpaidOrders] = useState([]);
  const [filteredOrders, setFilteredOrders] = useState([]);
  const [isLoadingInitialOrders, setIsLoadingInitialOrders] = useState(true);
  const [isSearching, setIsSearching] = useState(false);
  const [searchError, setSearchError] = useState('');
  const [initialFetchError, setInitialFetchError] = useState('');

  const {
    data: paymentsData,
    isLoading: loadingPaymentsData,
    error: paymentsDataError,
  } = usePayments({ status: PAYMENT_STATUSES.PENDING });
  const payments = useMemo(() => paymentsData?.content || [], [paymentsData]);

  // Simulate fetching initial unpaid orders on component mount
  useEffect(() => {
    const fetchOrders = () => {
      setIsLoadingInitialOrders(true);
      setInitialFetchError('');
      setTimeout(() => {
        try {
          // In a real app, this would be an API call to get all unpaid orders
          setAllUnpaidOrders(dummyUnpaidOrders);
          setFilteredOrders(dummyUnpaidOrders); // Initially display all
        } catch (err) {
          setInitialFetchError('Failed to load unpaid orders. Please try again.');
          console.error('Error fetching initial orders:', err);
        } finally {
          setIsLoadingInitialOrders(false);
        }
      }, 1000); // Simulate network delay
    };
    fetchOrders();
  }, []);

  const handleSearch = (criteria) => {
    setSearchError('');
    setIsSearching(true);

    const { tableNumber, phoneNumber, orderCode } = criteria;

    // If all search fields are empty, reset to show all orders
    if (!tableNumber && !phoneNumber && !orderCode) {
      setFilteredOrders(allUnpaidOrders);
      setIsSearching(false);
      return;
    }

    setTimeout(() => {
      const results = allUnpaidOrders.filter(order => {
        const matchesTable = tableNumber ? order.tableNumber === tableNumber : true;
        const matchesPhone = phoneNumber ? order.phoneNumber === phoneNumber : true;
        const matchesOrderCode = orderCode ? order.id.toLowerCase() === orderCode.toLowerCase() : true;

        // An order matches if it satisfies all provided (non-empty) criteria.
        return matchesTable && matchesPhone && matchesOrderCode;
      });

      if (results.length > 0) {
        setFilteredOrders(results);
      } else {
        setFilteredOrders([]);
        setSearchError('No matching orders found. Please refine your search.');
      }
      setIsSearching(false);
    }, 500); // Simulate search delay
  };

  const handleClearSearch = () => {
    setSearchError('');
    setFilteredOrders(allUnpaidOrders); // Reset to show all unpaid orders
  };

  const handleProceedToCheckout = (orderId) => {
    // In a real application, you would use React Router to navigate
    // and pass the orderId as a parameter or state to the checkout page.
    console.log(`Staff proceeding to checkout for Order ID: ${orderId}`);
    alert(`Navigating to Checkout Page for Order ID: ${orderId}`);
    // Example: history.push(`/staff-checkout/${orderId}`);
  };

  return (
    <div className={`container-fluid py-4 ${styles.staffPage}`}>
      <h1 className={`text-center mb-4 ${styles.pageTitle}`}>Unpaid Dine-In Orders</h1>

      <div className="row justify-content-center">
        <div className="col-xl-10 col-lg-11 col-md-12">
          <StaffSearchForm
            onSearch={handleSearch}
            onClearSearch={handleClearSearch}
            isSearching={isSearching}
          />
          
          {loadingPaymentsData && <Loading/>}
          {initialFetchError && (
            <div className={`alert alert-danger mt-4 text-center ${styles.errorMessage}`} role="alert">
              {initialFetchError}
            </div>
          )}

          {searchError && !isSearching && (
            <div className={`alert alert-warning mt-4 text-center ${styles.errorMessage}`} role="alert">
              {searchError}
            </div>
          )}

          {!isLoadingInitialOrders && !isSearching && !initialFetchError && (
            <StaffOrderList
              payments={payments}
              onProceedToCheckout={handleProceedToCheckout}
            />
          )}
        </div>
      </div>
    </div>
  );
}

export default PaymentManagementPage;