import { useState } from "react";
import config from "../../../../../config/WebConfig";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link, useNavigate } from "react-router";
import {
  faEye,
  faPenToSquare,
  faPlus,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";
import { Pagination } from "../../../../layouts/Pagination";

const dummyFoods = [
  {
    id: 1,
    product: "Cheeseburger",
    category: "Fast Food",
    added_date: "2025-03-31",
    price: 5.99,
    quantity: 100,
    status: "Active",
    image: "https://images.unsplash.com/photo-1571091718767-18b5b1457add",
    action: "Edit/Delete",
  },
  {
    id: 2,
    product: "Pepperoni Pizza",
    category: "Fast Food",
    added_date: "2025-03-30",
    price: 12.99,
    quantity: 50,
    status: "Active",
    image: "https://images.unsplash.com/photo-1604382354936-07c5d9983bd3",
    action: "Edit/Delete",
  },
  {
    id: 3,
    product: "Sushi Platter",
    category: "Japanese Cuisine",
    added_date: "2025-03-29",
    price: 24.5,
    quantity: 30,
    status: "Deactive",
    image: "https://images.unsplash.com/photo-1579871494447-9811cf80d66c",
    action: "Edit/Delete",
  },
  {
    id: 4,
    product: "Grilled Chicken Salad",
    category: "Healthy Food",
    added_date: "2025-03-28",
    price: 10.99,
    quantity: 75,
    status: "Active",
    image: "https://images.unsplash.com/photo-1512621776951-a57141f2eefd",
    action: "Edit/Delete",
  },
  {
    id: 5,
    product: "Chocolate Cake",
    category: "Desserts",
    added_date: "2025-03-27",
    price: 7.99,
    quantity: 40,
    status: "Active",
    image: "https://images.unsplash.com/photo-1578985545062-69928b1d9587",
    action: "Edit/Delete",
  },
  {
    id: 6,
    product: "Spaghetti Carbonara",
    category: "Italian Cuisine",
    added_date: "2025-03-26",
    price: 14.99,
    quantity: 35,
    status: "Active",
    image: "https://images.unsplash.com/photo-1588013273468-315fd88ea034", // New valid URL
    action: "Edit/Delete",
  },
  {
    id: 7,
    product: "Taco Platter",
    category: "Mexican Cuisine",
    added_date: "2025-03-25",
    price: 9.99,
    quantity: 60,
    status: "Active",
    image: "https://images.unsplash.com/photo-1599974579688-8e2d93126063", // New valid URL
    action: "Edit/Delete",
  },
  {
    id: 8,
    product: "Chicken Biryani",
    category: "Indian Cuisine",
    added_date: "2025-03-24",
    price: 13.5,
    quantity: 45,
    status: "Active",
    image: "https://images.unsplash.com/photo-1631515243349-7b2ff397159d", // New valid URL
    action: "Edit/Delete",
  },
  {
    id: 9,
    product: "French Fries",
    category: "Fast Food",
    added_date: "2025-03-23",
    price: 3.99,
    quantity: 120,
    status: "Active",
    image: "https://images.unsplash.com/photo-1630384060421-cb20d0e0649d",
    action: "Edit/Delete",
  },
  {
    id: 10,
    product: "Mango Smoothie",
    category: "Beverages",
    added_date: "2025-03-22",
    price: 4.5,
    quantity: 80,
    status: "Active",
    image: "https://images.unsplash.com/photo-1596399908039-2c38a72e19d0",
    action: "Edit/Delete",
  },
  {
    id: 11,
    product: "Steak with Mashed Potatoes",
    category: "Grilled Dishes",
    added_date: "2025-03-21",
    price: 19.99,
    quantity: 25,
    status: "Deactive",
    image: "https://images.unsplash.com/photo-1600891964092-4316c288032e",
    action: "Edit/Delete",
  },
  {
    id: 12,
    product: "Strawberry Cheesecake",
    category: "Desserts",
    added_date: "2025-03-20",
    price: 6.99,
    quantity: 50,
    status: "Active",
    image: "https://images.unsplash.com/photo-1566417713940-fe5c2c32c65b",
    action: "Edit/Delete",
  },
];
export default function ProductManagement() {
  const [foods, setFoods] = useState(dummyFoods);
  const [selectedItems, setSelectedItems] = useState([]);
  const [searchValue, setSearchValue] = useState("");
  const navigate = useNavigate();

  //  Table action
  const handleDeleteItem = (id) => {};
  const handleUpdateItem = (id) => {};
  const handleViewItem = (id) => {};

  // Select Items
  const handleSelectAll = (event) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      // Select all: Get all food IDs and update state
      const allFoodIds = foods.map((food) => food.id);
      setSelectedItems(allFoodIds);
    } else {
      // Deselect all: Clear the selected items state
      setSelectedItems([]);
    }
  };

  const handleSelectItem = (event, foodId) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      // Add the foodId to the selected list (prevent duplicates just in case)
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, foodId]),
      ]);
    } else {
      // Remove the foodId from the selected list
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== foodId)
      );
    }
  };

  const searchProducts = (e) => {
    e.preventDefault();
    const filteredFoods = dummyFoods.filter((food) =>
      food.product.toLowerCase().includes(searchValue.toLowerCase())
    );
    setFoods(filteredFoods);
    setSearchValue(""); // Clear the search input after submission
  };

  return (
    <main className="content overflow-scroll px-5 py-3">
      <h1 className="h3 my-3">
        <strong>Products</strong>
      </h1>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-12">
            <div className="card flex-fill">
              <div className="row mb-3">
                <div className="col-sm-5">
                  <Link className="btn btn-primary float-start">
                    <FontAwesomeIcon icon={faPlus} />
                    &nbsp; Add Products
                  </Link>
                </div>
                <div className="col-sm-7">
                  <Link className="btn btn-info float-end">Import</Link>
                  <Link className="me-2 btn btn-info float-end">Export</Link>
                </div>
              </div>
              <div className="row mb-3">
                <div className="col-md-8"></div>
                <div className="col-md-4">
                  <form className="form-inline d-flex" onSubmit={searchProducts}>
                    <input
                      className="form-control mr-sm-2 me-2"
                      type="search"
                      placeholder="Search"
                      aria-label="Search"
                      value={searchValue}
                      onChange={(e) => setSearchValue(e.target.value)}
                    />
                    <button
                      className="btn btn-outline-success my-2 my-sm-0"
                      type="submit"
                    >
                      Search
                    </button>
                  </form>
                </div>
              </div>

              <table id="products-table" className="table table-hover my-0">
                <thead>
                  <tr>
                    <th>
                      <div className="form-check">
                        <input
                          className="form-check-input"
                          style={{
                            width: "20px",
                            height: "20px",
                            padding: "0",
                          }}
                          type="checkbox"
                          value=""
                          onChange={handleSelectAll}
                        />
                      </div>
                    </th>
                    <th>Product</th>
                    <th>Category</th>
                    <th>Added Date</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Status</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {foods.map((food, index) => (
                    <tr
                      key={index}
                      className={
                        selectedItems.includes(food.id) ? "table-active" : ""
                      }
                    >
                      <td>
                        <div className="form-check">
                          <input
                            className="form-check-input"
                            style={{
                              width: "20px",
                              height: "20px",
                              padding: "0",
                            }}
                            type="checkbox"
                            value={food.id}
                            checked={selectedItems.includes(food.id)}
                            onChange={(e) => handleSelectItem(e, food.id)}
                          />
                        </div>
                      </td>
                      <td className="d-flex">
                        <img
                          className="me-3 rounded"
                          src={food.image}
                          alt={food.product}
                          style={{ height: "50px" }}
                        />
                        <p>{food.product}</p>
                      </td>
                      <td>{food.category}</td>
                      <td>{food.added_date}</td>
                      <td>{food.price}</td>
                      <td>{food.quantity}</td>
                      <td>
                        <span
                          className={
                            config.productStatusClasses[food.status] ||
                            "badge bg-secondary"
                          }
                        >
                          {food.status || "Unknown"}
                        </span>
                      </td>
                      <td>
                        <button
                          className="btn"
                          onClick={() => handleViewItem(food.id)}
                        >
                          <FontAwesomeIcon icon={faEye} />
                        </button>
                        <button
                          className="btn"
                          onClick={() => handleUpdateItem(food.id)}
                        >
                          <FontAwesomeIcon icon={faPenToSquare} />
                        </button>
                        <button
                          className="btn"
                          onClick={() => handleDeleteItem(food.id)}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <Pagination/>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
