import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage/HomePage";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import ShopPage from "./pages/ShopPage/ShopPage";
import "./App.css";
import SingleProduct from "./pages/SingleProductPage/SingleProduct";
import LoginPage from "./pages/LoginPage/LoginPage";
import RegisterPage from "./pages/RegisterPage/RegisterPage";
import AuthProvider from "./context/AuthContext";
import LayoutPage from "./pages/LayoutPage/LayoutPage";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import Authorization from "./components/Authorization/Authorization";
import { ROLES } from "./constants/roles";
import AuthLayoutPage from "./pages/AuthLayoutPage/AuthLayoutPage";
import AdminUsersPage from "./pages/AdminUsersPage/AdminUsersPage";
import AdminCategoriesPage from "./pages/AdminCategoriesPage/AdminCategoriesPage";
import AdminCreateCategoryPage from "./pages/AdminCreateCategory/AdminCreateCategoryPage";
import AdminUpdateCategoryPage from "./pages/AdminUpdateCategory/AdminUpdateCategoryPage";
import AdminUserProfilePage from "./pages/AdminViewUserProfile/AdminUserProfilePage";
import AdminCreateUserPage from "./pages/AdminCreateUserPage/AdminCreateUserPage";
import ModalProvider from "./context/ModalContext";
import ShoppingCart from "./pages/ShoppingCartPage/ShoppingCart";
import AlertProvider from "./context/AlertContext";
import ShippingAddressesPage from "./pages/ShippingAddressesPage/ShippingAddressesPage";
import CheckoutPage from "./pages/CheckoutPage/CheckoutPage";
import UserOrdersPage from "./pages/UserOrdersPage/UserOrdersPage";
import UserOrderDetailsPage from "./pages/UserOrderDetailsPage/UserOrderDetailsPage";
import AdminOrdersPage from "./pages/AdminOrdersPage/AdminOrdersPage";
import AdminOrderDetailsPage from "./pages/AdminOrderDetailsPage/AdminOrderDetailsPage";
import AdminProductsPage from "./pages/AdminProductsPage/AdminProductsPage";
import AdminCreateProductPage from "./pages/AdminCreateProduct/AdminCreateProductPage";
import AdminUpdateProductPage from "./pages/AdminUpdateProduct/AdminUpdateProductPage";
import AdminDashboardPage from "./pages/AdminDashboard/AdminDashboardPage";
import AdminTablesPage from "./pages/AdminTablesPage/AdminTablePage";
import AdminCreateTablePage from "./pages/AdminCreateTablePage/AdminCreateTablePage";
import AdminUpdateTablePage from "./pages/AdminUpdateTablePage/AdminUpdateTablePage";
import AdminReservationsPage from "./pages/AdminReservationsPage/AdminReservationsPage";
import AdminReservationPage from "./pages/AdminReservationPage/AdminReservationPage";
import AdminKitChenPage from "./pages/AdminKitchenPage/AdminKitchenPage";
import WaiterOrderPage from "./pages/WaiterOrderPage/WaiterOrderPage";
import AdminLayout from "./pages/ProfileLayout/AdminLayout";
import CustomerLayout from "./pages/ProfileLayout/CustomerLayout";
import EmployeeLayout from "./pages/ProfileLayout/EmployeeLayout";
import EmployeeTableManagement from "./pages/TableManagement/EmployeeTableManagement"
import EmployeeKitchenPage from "./pages/EmployeeKitchenPage/EmployeeKitchennPage";
import RestaurantOrderSystem from "./pages/RestaurantOrderSystemPage/RestaurantOrderSystemPage";
import UserReservationsPage from "./pages/UserReservationsPage/UserReservationsPage";
import ReservationFormPage from "./pages/ReservationFormPage/ReservationPage";
import UserReservationDetailsPage from "./pages/UserReservationDetailsPage/UserReservationDetailsPage";
import UserNotificationPage from "./pages/UserNotificationPage/UserNotificationPage";
import WebSocketProvider from "./context/WebSocketContext";
import EmployeeNotificationPage from "./pages/EmployeeNotificationPage/EmployeeNotificationPage";
import CustomerProfilePage from "./pages/CustomerProfilePage/CustomerProfilePage";
import EmployeeProfilePage from "./pages/EmployeeProfilePage/EmployeeProfilePage";
import UserChangePassword from "./pages/UserChangePassword/UserChangePassword";
import EmployeeChangePassword from "./pages/EmployeeChangePassword/EmployeeChangePassword";
import ForgotPasswordPage from "./pages/ForgotPasswordPage/ForgotPasswordPage";
import PaymentProcessingPage from "./pages/PaymentProcessingPage/PaymentProcessingPage";

function App() {
  return (
    <Router>
      <AuthProvider>
        <WebSocketProvider>
          <AlertProvider>
            <ModalProvider>
              <Routes>
                <Route element={<LayoutPage />}>
                  <Route path="" element={<HomePage />} />
                  <Route path="shop" element={<ShopPage />} />
                  <Route path="shop/:foodname" element={<SingleProduct />} />
                  <Route path="payment" element={<PaymentProcessingPage/>} />
                  <Route element={<PrivateRoute />}>
                    <Route element={<Authorization roles={[ROLES.CUSTOMER]} />}>
                      <Route path="cart" element={<ShoppingCart />} />
                      <Route path="checkout" element={<CheckoutPage />} />
                      <Route path="reservation" element={<ReservationFormPage />} />
                    </Route>
                  </Route>
                </Route>

                <Route element={<PrivateRoute />}>
                  <Route path="admin" element={<Authorization roles={[ROLES.ADMIN]} />} >
                    <Route element={<AdminLayout />}>
                      <Route path="dashboard" element={<AdminDashboardPage />} />
                      <Route path="categories" element={<AdminCategoriesPage />} />
                      <Route path="categories/add" element={<AdminCreateCategoryPage />} />
                      <Route path="categories/:id" element={<AdminUpdateCategoryPage />} />
                      <Route path="products" element={<AdminProductsPage />} />
                      <Route path="products/add" element={<AdminCreateProductPage />}/>
                      <Route path="products/:id" element={<AdminUpdateProductPage />}/>
                      <Route path="users" element={<AdminUsersPage />} />
                      <Route path="users/:userId" element={<AdminUserProfilePage />}/>
                      <Route path="users/add" element={<AdminCreateUserPage />} />
                      <Route path="orders" element={<AdminOrdersPage />} />
                      <Route path="orders/:id" element={<AdminOrderDetailsPage />} />
                      <Route path="tables" element={<AdminTablesPage />} />
                      <Route path="tables/add" element={<AdminCreateTablePage />} />
                      <Route path="tables/:id" element={<AdminUpdateTablePage />}/>
                      <Route path="reservations" element={<AdminReservationsPage />} />
                      <Route path="reservations/:id" element={<AdminReservationPage />}/>
                      <Route path="kitchen" element={<AdminKitChenPage />} />
                      <Route path="waiter/orders" element={<WaiterOrderPage />} />
                    </Route>
                  </Route>

                  <Route path="employee" element={<Authorization roles={[ROLES.EMPLOYEE]} />}>
                    <Route element={<EmployeeLayout />}>
                      <Route path="profile" element={<EmployeeProfilePage />} />
                      <Route path="orders/place" element={<RestaurantOrderSystem />}/>
                      <Route path="tables" element={<EmployeeTableManagement/>}/>
                      <Route path="kitchen" element={<EmployeeKitchenPage/>}/>
                      <Route path="notifications" element={<EmployeeNotificationPage/>}/>
                      <Route path="password" element={<EmployeeChangePassword/>}/>
                    </Route>
                  </Route>

                  <Route path="user" element={<Authorization roles={[ROLES.CUSTOMER]} />}>
                    <Route element={<CustomerLayout />}>
                      <Route path="profile" element={<CustomerProfilePage />} />
                      <Route path="addresses" element={<ShippingAddressesPage />}/>
                      <Route path="orders" element={<UserOrdersPage />} />
                      <Route path="orders/:id" element={<UserOrderDetailsPage />}/>
                      <Route path="reservations" element={<UserReservationsPage/>}/>
                      <Route path="reservations/:id" element={<UserReservationDetailsPage/>}/>
                      <Route path="notifications" element={<UserNotificationPage/>}/>
                      <Route path="password" element={<UserChangePassword/>}/>
                    </Route>
                  </Route>
                </Route>

                <Route element={<AuthLayoutPage />}>
                  <Route path="login" element={<LoginPage />} />
                  <Route path="register" element={<RegisterPage />} />
                  <Route path="forgot" element={<ForgotPasswordPage/>}/>
                </Route>
              </Routes>
            </ModalProvider>
          </AlertProvider>
        </WebSocketProvider>
      </AuthProvider>
    </Router>
  );
}

export default App;
