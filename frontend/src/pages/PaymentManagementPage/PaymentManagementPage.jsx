import { useState, useMemo, useCallback } from "react";
import styles from "./PaymentManagementPage.module.css";
import StaffSearchForm from "../../components/PaymentManagement/StaffSearchForm/StaffSearchForm";
import StaffOrderList from "../../components/PaymentManagement/StaffOrderList/StaffOrderList";
import { usePayments } from "../../hooks/paymentHooks";
import { PAYMENT_STATUSES } from "../../constants/webConstant";
import { useNavigate, useSearchParams } from "react-router-dom";
import Pagination from "../../components/Pagination/Pagination";
import { PAYMENT_CHECKOUT_URI } from "../../constants/routes";

function PaymentManagementPage() {
  const [searchError, setSearchError] = useState("");
  const [searchCriteria, setSearchCriteria] = useState({
    tableNumber: null,
    phoneNumber: null,
    orderId: null,
  });
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const currentPageFromURL = parseInt(searchParams.get("page")) || 0;
  const [currentPage] = useState(currentPageFromURL);

  const {
    data: paymentsData,
    isLoading: loadingPaymentsData,
    error: paymentsDataError,
  } = usePayments({
    phone: searchCriteria.phoneNumber,
    orderId: searchCriteria.orderId,
    tableNumber: searchCriteria.tableNumber,
    status: PAYMENT_STATUSES.PENDING,
    page: currentPage,
  });
  const payments = useMemo(() => paymentsData?.content || [], [paymentsData]);
  const totalPages = useMemo(
    () => paymentsData?.totalPages || 0,
    [paymentsData]
  );

  const handleSearch = (criteria) => {
    setSearchError("");

    const { tableNumber, phoneNumber, orderId } = criteria;
    setSearchCriteria((prev) => ({
      ...prev,
      tableNumber,
      phoneNumber,
      orderId,
    }));
  };

  const handleClearSearch = () => {
    setSearchError("");
    setSearchCriteria({
      tableNumber: null,
      phoneNumber: null,
      orderId: null,
    });
  };

  const handleProceedToCheckout = useCallback(
    (paymentId) => {
      navigate(`${PAYMENT_CHECKOUT_URI}/${paymentId}`);
    },
    [navigate]
  );

  return (
    <div className={`container-fluid py-4 ${styles.staffPage}`}>
      <h1 className={`text-center mb-4 ${styles.pageTitle}`}>
        Unpaid Payments
      </h1>

      <div className="row justify-content-center">
        <div className="col-xl-10 col-lg-11 col-md-12">
          <StaffSearchForm
            onSearch={handleSearch}
            onClearSearch={handleClearSearch}
            isSearching={loadingPaymentsData}
          />

          {paymentsDataError?.message && (
            <div
              className={`alert alert-danger mt-4 text-center ${styles.errorMessage}`}
              role="alert"
            >
              {paymentsDataError?.message}
            </div>
          )}

          {searchError && !loadingPaymentsData && (
            <div
              className={`alert alert-warning mt-4 text-center ${styles.errorMessage}`}
              role="alert"
            >
              {searchError}
            </div>
          )}

          {!loadingPaymentsData && !paymentsDataError && (
            <StaffOrderList
              payments={payments}
              onProceedToCheckout={handleProceedToCheckout}
            />
          )}
        </div>
      </div>
      {totalPages > 1 && <Pagination />}
    </div>
  );
}

export default PaymentManagementPage;
