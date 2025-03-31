import { useState } from "react"
import config from "../../../../config/WebConfig"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router"
import { faEye, faPenToSquare, faPlus, faTrash } from "@fortawesome/free-solid-svg-icons";

const dummyFoods = [
  {
    "id": 1, // Added id
    "product": "Cheeseburger",
    "category": "Fast Food",
    "added_date": "2025-03-31",
    "price": 5.99,
    "quantity": 100,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=Cheeseburger",
    "action": "Edit/Delete"
  },
  {
    "id": 2, // Added id
    "product": "Pepperoni Pizza",
    "category": "Fast Food",
    "added_date": "2025-03-30",
    "price": 12.99,
    "quantity": 50,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=Pepperoni+Pizza",
    "action": "Edit/Delete"
  },
  {
    "id": 3, // Added id
    "product": "Sushi Platter",
    "category": "Japanese Cuisine",
    "added_date": "2025-03-29",
    "price": 24.50,
    "quantity": 30,
    "status": "Deactive", // Note: Might want to normalize status values (e.g., 'Inactive' or 'Deactivated')
    "image": "https://via.placeholder.com/150?text=Sushi+Platter",
    "action": "Edit/Delete"
  },
  {
    "id": 4, // Added id
    "product": "Grilled Chicken Salad",
    "category": "Healthy Food",
    "added_date": "2025-03-28",
    "price": 10.99,
    "quantity": 75,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=Grilled+Chicken+Salad",
    "action": "Edit/Delete"
  },
  {
    "id": 5, // Added id
    "product": "Chocolate Cake",
    "category": "Desserts",
    "added_date": "2025-03-27",
    "price": 7.99,
    "quantity": 40,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=Chocolate+Cake",
    "action": "Edit/Delete"
  },
  {
    "id": 6, // Added id
    "product": "Spaghetti Carbonara",
    "category": "Italian Cuisine",
    "added_date": "2025-03-26",
    "price": 14.99,
    "quantity": 35,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=Spaghetti+Carbonara",
    "action": "Edit/Delete"
  },
  {
    "id": 7, // Added id
    "product": "Taco Platter",
    "category": "Mexican Cuisine",
    "added_date": "2025-03-25",
    "price": 9.99,
    "quantity": 60,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=Taco+Platter",
    "action": "Edit/Delete"
  },
  {
    "id": 8, // Added id
    "product": "Chicken Biryani",
    "category": "Indian Cuisine",
    "added_date": "2025-03-24",
    "price": 13.50,
    "quantity": 45,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=Chicken+Biryani",
    "action": "Edit/Delete"
  },
  {
    "id": 9, // Added id
    "product": "French Fries",
    "category": "Fast Food",
    "added_date": "2025-03-23",
    "price": 3.99,
    "quantity": 120,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=French+Fries",
    "action": "Edit/Delete"
  },
  {
    "id": 10, // Added id
    "product": "Mango Smoothie",
    "category": "Beverages",
    "added_date": "2025-03-22",
    "price": 4.50,
    "quantity": 80,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=Mango+Smoothie",
    "action": "Edit/Delete"
  },
  {
    "id": 11, // Added id
    "product": "Steak with Mashed Potatoes",
    "category": "Grilled Dishes",
    "added_date": "2025-03-21",
    "price": 19.99,
    "quantity": 25,
    "status": "Deactive", // Note: Might want to normalize status values
    "image": "https://via.placeholder.com/150?text=Steak+with+Mashed+Potatoes",
    "action": "Edit/Delete"
  },
  {
    "id": 12, // Added id
    "product": "Strawberry Cheesecake",
    "category": "Desserts",
    "added_date": "2025-03-20",
    "price": 6.99,
    "quantity": 50,
    "status": "Active",
    "image": "https://via.placeholder.com/150?text=Strawberry+Cheesecake",
    "action": "Edit/Delete"
  }
];

export default function ProductManagement(){
    const [foods, setFoods] = useState(dummyFoods)
    const [selectedItems, setSelectedItems] = useState([]);
    const [searchValue, setSearchValue] = useState("")

    const handleSelectAll = (event) => {
      const isChecked = event.target.checked;
      if (isChecked) {
          // Select all: Get all food IDs and update state
          const allFoodIds = foods.map(food => food.id);
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
          setSelectedItems(prevSelected => [...new Set([...prevSelected, foodId])]);
      } else {
          // Remove the foodId from the selected list
          setSelectedItems(prevSelected => prevSelected.filter(id => id !== foodId));
      }
    };

    const searchProducts = ()=>{

    }

    
    return(
        <main className="content overflow-scroll px-5 py-3">
            <div className="container-fluid p-0">
                <div className="row">
                    <div className="col-12">
                        <div className="card flex-fill">
                          <div className="row mb-3">
                            <div className="col-sm-5">
                              <Link className="btn btn-primary float-start">
                                <FontAwesomeIcon icon={faPlus}/>&nbsp;
                                Add Products
                              </Link>
                            </div>
                            <div className="col-sm-7">
                              <Link className="btn btn-info float-end">
                                Import
                              </Link>
                              <Link className="me-2 btn btn-info float-end">
                                Export
                              </Link>
                            </div>
                          </div>
                          <div className="row mb-3">
                            <div className="col-md-8">

                            </div>
                            <div className="col-md-4">
                              <form className="form-inline d-flex">
                                <input 
                                  className="form-control mr-sm-2" 
                                  type="search" 
                                  placeholder="Search" 
                                  aria-label="Search" 
                                  value={searchValue}
                                  onChange={(e) => setSearchValue(e.target.value)}
                                />
                                <button 
                                  className="btn btn-outline-success my-2 my-sm-0" 
                                  onClick={searchProducts}  
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
                                      style={{width:"20px", height:"20px", padding:"0"}} 
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
                                <tr key={index} className={selectedItems.includes(food.id) ? 'table-active' : ''}>
                                  <td>
                                    <div className="form-check">
                                      <input 
                                        className="form-check-input" 
                                        style={{width:"20px", height:"20px", padding:"0"}} 
                                        type="checkbox" 
                                        value={food.id}
                                        checked={selectedItems.includes(food.id)}
                                        onChange={(e)=>handleSelectItem(e, food.id)}
                                      />
                                    </div>
                                  </td>
                                  <td className="d-flex">
                                    <img className="me-3" src={food.image} alt={food.product} style={{minHeight:"40px"}}/>
                                    <p>{food.product}</p>
                                  </td>
                                  <td>{food.category}</td>
                                  <td>{food.added_date}</td>
                                  <td>{food.price}</td>
                                  <td>{food.quantity}</td>
                                  <td>
                                    <span
                                      className={
                                        config.productStatusClasses[food.status] || "badge bg-secondary"
                                      }
                                    >
                                      {food.status || "Unknown"}
                                    </span>
                                  </td>
                                  <td>
                                    <button className="btn">
                                      <FontAwesomeIcon icon={faEye}/>
                                    </button>
                                    <button className="btn">
                                      <FontAwesomeIcon icon={faPenToSquare}/>
                                    </button>
                                    <button className="btn">
                                      <FontAwesomeIcon icon={faTrash}/>
                                    </button>
                                  </td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                          <nav className="mt-3">
                            <ul className="pagination float-end">
                              <li className="page-item">
                                <Link className="page-link" to="#" aria-label="Previous">
                                  <span aria-hidden="true">&laquo;</span>
                                  <span className="sr-only">Previous</span>
                                </Link>
                              </li>
                              <li className="page-item"><Link className="page-link" to="#">1</Link></li>
                              <li className="page-item"><Link className="page-link" to="#">2</Link></li>
                              <li className="page-item"><Link className="page-link" to="#">3</Link></li>
                              <li className="page-item">
                                <Link className="page-link" to="#" aria-label="Next">
                                  <span aria-hidden="true">&raquo;</span>
                                  <span className="sr-only">Next</span>
                                </Link>
                              </li>
                            </ul>
                          </nav>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    )
}