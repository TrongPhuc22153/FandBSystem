import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomePage from "./pages/Home";
import UserPage from "./pages/User";
import "./App.css";
import "./App.scss";
import "slick-carousel/slick/slick.css"; // Import Slick CSS
import "slick-carousel/slick/slick-theme.css"; // Import Slick theme CSS
import AuthenticationPage from "./pages/Authentication";
import "react-datepicker/dist/react-datepicker.css";
import AuthProvider from "./hooks/AuthContext";
import { DefaultHomePage } from "./pages/DefaultHome";
import ReservationPage from "./pages/Reservation";
import MenuPage from "./pages/Menu";
import ProductPage from "./pages/Product";
import FoodReservationPage from "./pages/FoodReservationPage";
import CartPage from "./pages/Cart";
import CheckoutOrderPage from "./pages/Checkout";
import PrivateRoute from "./auth/PrivateRoute";
import AdminPage from "./pages/Admin";
import CustomerPage from "./pages/Customer";
import AdminDashboard from "./pages/AdminDashboard";
import OrdersAdmin from "./pages/OrdersAdmin";
import ProductManagement from "./pages/ProductManagement";
import ProductAdmin from "./pages/ProductAdmin";
import TablesAdmin from "./pages/TablesAdmin";
import TableAdmin from "./pages/TableAdmin";
import OrderDetailsAdmin from "./pages/OrderDetailsAdmin";
import CustomersAdmin from "./pages/CustomersAdmin";
import NotificationsAdmin from "./pages/NotificationsAdmin";
import AdminCalendarPage from "./pages/AdminCalendar";
import AdminTableCalendar from "./pages/AdminTableCalendar";
import ResetPasswordPage from "./pages/ResetPassword";
import ForgetPasswordPage from "./pages/ForgetPassword";
import RegisterPage from './pages/Register';
import LoginPage from './pages/Login';
import OrdersCustomer from "./pages/OrdersCustomer";
import CustomerDashboard from "./pages/CustomerDashboard";
import NotificationsCustomer from "./pages/NotificationsCustomer";
import OrderDetailsCustomer from "./pages/OrderDetailsCustomer";
import EmployeePage from "./pages/Employee";
import EmployeeDashboard from "./pages/EmployeeDashboard";
import OrdersEmployee from "./pages/OrdersEmployee";
import NotificationsEmployee from "./pages/NotificationsEmployee";
import OrderDetailsEmployee from "./pages/OrderDetailsEmployee";
import Authorization from "./auth/Authorization";
import { ROLES } from "./constants/RoleName";

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route element={<HomePage />}>
            <Route path="" element={<DefaultHomePage />} />
            <Route path="home" element={<DefaultHomePage />} />
            <Route path="reservation" element={<ReservationPage />} />
            <Route path="menu" element={<MenuPage />} />
            <Route path="foods/:food" element={<ProductPage />} />
            <Route path="food-reservation" element={<FoodReservationPage />} />
            <Route element={<PrivateRoute/>}>
              <Route element={<Authorization roles={[ROLES.CUSTOMER]}/>}>
                <Route path="cart" element={<CartPage />} />
                <Route path="checkout" element={<CheckoutOrderPage />} />
              </Route>
            </Route>
          </Route>
          <Route element={<PrivateRoute />}>
            <Route element={<UserPage />}>
              <Route element={<Authorization roles={[ROLES.ADMIN]} />}>
                <Route path="admin" element={<AdminPage />}>
                  <Route path="" element={<AdminDashboard />} />
                  <Route path="dashboard" element={<AdminDashboard />} />
                  <Route path="orders" element={<OrdersAdmin />} />
                  <Route path="order" element={<OrderDetailsAdmin />} />
                  <Route path="products" element={<ProductManagement />} />
                  <Route path="product/:id" element={<ProductAdmin />} />
                  <Route path="tables" element={<TablesAdmin />} />
                  <Route path="table" element={<TableAdmin />} />
                  <Route path="customers" element={<CustomersAdmin />} />
                  <Route path="notifications" element={<NotificationsAdmin />} />
                  <Route path="calendar" element={<AdminCalendarPage />} />
                  <Route path="table-calendar" element={<AdminTableCalendar />} />
                </Route>
              </Route>
              <Route element={<Authorization roles={[ROLES.CUSTOMER]} />}>
                <Route path="customer" element={<CustomerPage />}>
                  <Route path="" element={<CustomerDashboard />} />
                  <Route path="dashboard" element={<CustomerDashboard />} />
                  <Route path="orders" element={<OrdersCustomer />} />
                  <Route path="order" element={<OrderDetailsCustomer />} />
                  <Route path="notifications" element={<NotificationsCustomer />} />
                </Route>
              </Route>
              <Route element={<Authorization roles={[ROLES.EMPLOYEE]} />}>
                <Route path="employee" element={<EmployeePage />}>
                  <Route path="" element={<EmployeeDashboard />} />
                  <Route path="dashboard" element={<EmployeeDashboard />} />
                  <Route path="orders" element={<OrdersEmployee />} />
                  <Route path="order" element={<OrderDetailsEmployee />} />
                  <Route path="notifications" element={<NotificationsEmployee />} />
                </Route>
              </Route>
            </Route>
          </Route>
          <Route element={<AuthenticationPage />}>
            <Route path="login" element={<LoginPage />} />
            <Route path="register" element={<RegisterPage />} />
            <Route path="forget-password" element={<ForgetPasswordPage />} />
            <Route path="reset-password" element={<ResetPasswordPage />} />
          </Route>
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
