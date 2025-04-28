import { useEffect, useState } from "react";
import config from "../config/WebConfig";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import axios from "axios";
import { Link, useNavigate, useSearchParams } from "react-router";
import {
  faEye,
  faPenToSquare,
  faPlus,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";
import { Pagination } from "../components/Pagination";
import { PRODUCT_URL } from "../constants/ApiEndpoints";
import { getDefaultFood } from "../services/ImageService";
import { ADMIN_PRODUCT_URI } from "../constants/WebPageURI";

export default function ProductManagement() {
  const [productData, setProductData] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);
  const [searchValue, setSearchValue] = useState("");
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [pageable, setPageable] = useState({
    pageNumber: 0,
    totalPages: 0,
  });
  const [searchParams] = useSearchParams();
  const currentPage = parseInt(searchParams.get('page') || '0');

  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await axios.get(`${PRODUCT_URL}?page=${currentPage}`);
        const data = response.data;
        setProductData(data.content);
        setPageable({
          pageNumber: data.pageable.pageNumber,
          totalPages: data.totalPages,
        });
      } catch (error) {
        setError(error.message || "An error occurred while fetching products.");
        console.error("Error fetching products:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [currentPage]);

  const handleDeleteItem = (id) => {
    setProductData((prevProducts) => prevProducts.filter((product) => product.productId !== id));
    setSelectedItems((prevSelectedItems) =>
      prevSelectedItems.filter((itemId) => itemId !== id)
    );
  };
  const handleUpdateItem = (id) => {
    navigate(`${ADMIN_PRODUCT_URI}/${id}`);
  };
  const handleViewItem = (id) => {
    navigate(`/products/view/${id}`);
  };

  const handleSelectAll = (event) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      const allFoodIds = productData.map((product) => product.productId);
      setSelectedItems(allFoodIds);
    } else {
      setSelectedItems([]);
    }
  };

  const handleSelectItem = (
    event,
    productId
  ) => {
    const isChecked = event.target.checked;
    if (isChecked) {
      setSelectedItems((prevSelected) => [
        ...new Set([...prevSelected, productId]),
      ]);
    } else {
      setSelectedItems((prevSelected) =>
        prevSelected.filter((id) => id !== productId)
      );
    }
  };

  const searchProducts = (e) => {
    e.preventDefault();
    const filteredProducts = productData.filter((product) =>
      product.productName.toLowerCase().includes(searchValue.toLowerCase())
    );
    setProductData(filteredProducts);
    setSearchValue("");
  };

  if (loading) {
    return (
      <div className="content overflow-scroll px-5 py-3">
        <p>Loading products...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="content overflow-scroll px-5 py-3">
        <p className="text-danger">Error: {error}</p>
      </div>
    );
  }

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
                  <Link to="/products/add" className="btn btn-primary float-start">
                    <FontAwesomeIcon icon={faPlus} />
                    &nbsp; Add Products
                  </Link>
                </div>
                <div className="col-sm-7">
                  <Link to="/products/import" className="btn btn-info float-end">
                    Import
                  </Link>
                  <Link to="/products/export" className="me-2 btn btn-info float-end">
                    Export
                  </Link>
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

              <div className="table-responsive">
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
                      <th>Price</th>
                      <th>Quantity</th>
                      <th>Image</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {productData.map((product) => (
                      <tr
                        key={product.productId}
                        className={
                          selectedItems.includes(product.productId) ? "table-active" : ""
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
                              value={product.productId}
                              checked={selectedItems.includes(product.productId)}
                              onChange={(e) => handleSelectItem(e, product.productId)}
                            />
                          </div>
                        </td>
                        <td>{product.productName}</td>
                        <td>{product.category.categoryName}</td>
                        <td>{product.unitPrice}</td>
                        <td>{product.unitsInStock}</td>
                        <td>
                          <img
                            className="rounded"
                            src={product.picture || getDefaultFood()}
                            alt={product.productName}
                            style={{ height: "50px" }}
                          />
                        </td>
                        <td>
                          <button
                            className="btn"
                            onClick={() => handleViewItem(product.productId)}
                          >
                            <FontAwesomeIcon icon={faEye} />
                          </button>
                          <button
                            className="btn"
                            onClick={() => handleUpdateItem(product.productId)}
                          >
                            <FontAwesomeIcon icon={faPenToSquare} />
                          </button>
                          <button
                            className="btn"
                            onClick={() => handleDeleteItem(product.productId)}
                          >
                            <FontAwesomeIcon icon={faTrash} />
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <Pagination
                totalPages={pageable.totalPages}
                currentPage={currentPage}
              />
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}

