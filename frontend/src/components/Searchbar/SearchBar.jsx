import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Form, FormControl, Dropdown } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import styles from "./SearchBar.module.css";
import { SHOP_URI } from "../../constants/routes";
import { useProducts } from "../../hooks/productHooks";

const SearchBar = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [showDropdown, setShowDropdown] = useState(false);

  const { data: searchData } = useProducts({ search: searchTerm });
  const products = searchData?.content || [];

  const searchProducts = () => {
    if (searchTerm.trim()) {
      navigate(`${SHOP_URI}?search=${encodeURIComponent(searchTerm)}`);
    } else {
      navigate(SHOP_URI);
    }
  };

  const handleSearchClick = (e) => {
    e.preventDefault();
    searchProducts();
  };

  const handleKeydown = (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      searchProducts();
    }
  };

  const handleInputChange = (e) => {
    const value = e.target.value;
    setSearchTerm(value);
    setShowDropdown(value.length > 0);
  };

  const handleProductSelect = (product) => {
    setSearchTerm(product.productName);
    setShowDropdown(false);
    navigate(
      `${SHOP_URI}/${encodeURIComponent(
        product.productName
      )}?id=${encodeURIComponent(product.productId)}`
    );
  };

  const handleFocus = () => {
    if (searchTerm.length > 0) {
      setShowDropdown(true);
    }
  };

  const handleBlur = () => {
    setTimeout(() => {
      setShowDropdown(false);
    }, 200);
  };

  return (
    <div className="position-relative d-inline-block">
      <Form className="d-flex mx-3" style={{ maxWidth: "300px" }}>
        <FormControl
          type="text"
          placeholder="Search..."
          className={styles["search-input"]}
          aria-label="Search"
          value={searchTerm}
          onChange={handleInputChange}
          onFocus={handleFocus}
          onBlur={handleBlur}
          onKeyDown={handleKeydown}
        />
        <button
          type="button"
          className={styles["search-button"]}
          onClick={handleSearchClick}
          style={{
            position: "absolute",
            right: "10%",
            top: "50%",
            transform: "translateY(-50%)",
            background: "none",
            border: "none",
            cursor: "pointer",
          }}
        >
          <FontAwesomeIcon icon={faSearch} className={styles["search-icon"]} />
        </button>
      </Form>
      {showDropdown && products.length > 0 && (
        <Dropdown.Menu
          show
          style={{
            position: "absolute",
            top: "100%",
            left: "50%",
            width: "90%",
            zIndex: 1000,
            transform: "translateX(-50%)",
          }}
        >
          {products.map((product) => (
            <Dropdown.Item
              key={product.productId}
              onClick={() => handleProductSelect(product)}
            >
              {product.productName}
            </Dropdown.Item>
          ))}
        </Dropdown.Menu>
      )}
    </div>
  );
};

export default SearchBar;
