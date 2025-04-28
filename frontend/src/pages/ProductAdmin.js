import { faPenToSquare, faSave, faTimes, faImage } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState, useRef } from "react";
import { useParams } from "react-router";
import axios from "axios";
import { PRODUCT_URL } from "../constants/ApiEndpoints";
import { getDefaultFood } from "../services/ImageService";

export default function ProductAdmin() {
  const { id } = useParams();
  const [product, setProduct] = useState({
    productName: '',
    picture: '',
    previewImages: [],
    category: { categoryName: '' },
    unitPrice: 0,
    oldPrice: 0,
    discount: 0,
    description: '',
    unitsInStock: 0,
  });
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [updatedProduct, setUpdatedProduct] = useState({ ...product });
  const [mainImageFile, setMainImageFile] = useState(null);
  const [mainImagePreview, setMainImagePreview] = useState('');
  const fileInputRef = useRef(null);

  useEffect(() => {
    const fetchProduct = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await axios.get(`${PRODUCT_URL}/${id}`);
        setProduct(res.data);
        setUpdatedProduct(res.data);
        setMainImagePreview(res.data.picture || getDefaultFood());
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id]);

  const handleQuantityChange = (event) => {
    const value = Math.max(0, Math.min(5, Number(event.target.value)));
    setQuantity(value);
  };

  const handleEditClick = () => {
    setIsEditing(true);
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    setUpdatedProduct(product);
    setMainImageFile(null);
    setMainImagePreview(product.picture || getDefaultFood());
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setUpdatedProduct(prevProduct => ({
      ...prevProduct,
      [name]: value,
    }));
  };

  const handleCategoryChange = (event) => {
    setUpdatedProduct(prevProduct => ({
      ...prevProduct,
      category: { ...prevProduct.category, categoryName: event.target.value },
    }));
  };

  const handleMainImageChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setMainImageFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setMainImagePreview(reader.result);
      };
      reader.readAsDataURL(file);
      // If you want to immediately update the updatedProduct state with a temporary URL
      // setUpdatedProduct(prevProduct => ({ ...prevProduct, picture: reader.result }));
    }
  };

  const handleUploadButtonClick = () => {
    fileInputRef.current.click();
  };

  const handleSaveClick = async () => {
    try {
      setLoading(true);
      setError(null);

      const formData = new FormData();
      formData.append('productName', updatedProduct.productName || '');
      formData.append('categoryName', updatedProduct.category.categoryName || '');
      formData.append('unitPrice', updatedProduct.unitPrice || 0);
      formData.append('oldPrice', updatedProduct.oldPrice || 0);
      formData.append('discount', updatedProduct.discount || 0);
      formData.append('description', updatedProduct.description || '');
      formData.append('unitsInStock', updatedProduct.unitsInStock || 0);
      if (mainImageFile) {
        formData.append('picture', mainImageFile);
      } else if (updatedProduct.picture && updatedProduct.picture !== product.picture) {
        // If the user didn't upload a new file but the picture URL changed (how?), you might handle it here.
        // This scenario is less likely with a file upload flow.
        formData.append('picture', updatedProduct.picture);
      }

      const response = await axios.put(`${PRODUCT_URL}/${id}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      if (response.status === 200) {
        setProduct(response.data); // Assuming the server returns the updated product
        setUpdatedProduct(response.data);
        setMainImageFile(null);
        setMainImagePreview(response.data.picture || getDefaultFood());
        setIsEditing(false);
      } else {
        setError("Failed to update product.");
      }
    } catch (err) {
      setError(err.message || "An error occurred while updating the product.");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <p>Loading product details...</p>;
  }

  if (error) {
    return <p>Error fetching product: {error}</p>;
  }

  return (
    <div className="container-fluid">
      <h1 className="h3 my-3">
        <strong>Product Details</strong>
      </h1>
      <div className="mb-5">
        <div className="row">
          <div className="col-md-5">
            <div className="main-img">
              <img
                className="img-fluid"
                src={mainImagePreview}
                alt={isEditing ? updatedProduct.productName : product.productName}
              />
              {isEditing && (
                <div className="mt-2">
                  <button className="btn btn-sm btn-outline-secondary" onClick={handleUploadButtonClick}>
                    <FontAwesomeIcon icon={faImage} /> Upload Image
                  </button>
                  <input
                    type="file"
                    ref={fileInputRef}
                    style={{ display: 'none' }}
                    onChange={handleMainImageChange}
                    accept="image/*"
                  />
                </div>
              )}
              {!isEditing && product.previewImages && product.previewImages.length > 0 && (
                <div className="row my-3 previews">
                  {product.previewImages.map((image, index) => (
                    <div className="col-md-3" key={index}>
                      <img className="w-100" src={image} alt={`Preview ${index + 1}`} />
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>

          <div className="col-md-7 d-flex flex-column justify-content-between">
            <div className="main-description px-2">
              <div className="category text-bold">
                Category: {isEditing ? (
                  <input
                    type="text"
                    className="form-control form-control-sm"
                    name="categoryName"
                    value={updatedProduct.category.categoryName || ''}
                    onChange={handleCategoryChange}
                  />
                ) : (
                  product.category.categoryName
                )}
              </div>

              <div className="product-title text-bold my-3 d-flex align-items-center justify-content-between">
                {isEditing ? (
                  <input
                    type="text"
                    className="form-control"
                    name="productName"
                    value={updatedProduct.productName || ''}
                    onChange={handleInputChange}
                  />
                ) : (
                  product.productName
                )}
                {!isEditing ? (
                  <button className="btn btn-sm btn-outline-secondary ms-4" onClick={handleEditClick}>
                    <FontAwesomeIcon icon={faPenToSquare} /> Edit
                  </button>
                ) : (
                  <div>
                    <button className="btn btn-sm btn-success me-2" onClick={handleSaveClick} disabled={loading}>
                      <FontAwesomeIcon icon={faSave} /> Save
                    </button>
                    <button className="btn btn-sm btn-danger" onClick={handleCancelEdit} disabled={loading}>
                      <FontAwesomeIcon icon={faTimes} /> Cancel
                    </button>
                  </div>
                )}
              </div>

              <div className="price-area my-4">
                {isEditing ? (
                  <>
                    <div className="mb-2">
                      <label htmlFor="oldPrice" className="form-label small">Old Price</label>
                      <input type="number" className="form-control form-control-sm" id="oldPrice" name="oldPrice" value={updatedProduct.oldPrice || ''} onChange={handleInputChange} />
                    </div>
                    <div className="mb-2">
                      <label htmlFor="discount" className="form-label small">Discount (%)</label>
                      <input type="number" className="form-control form-control-sm" id="discount" name="discount" value={updatedProduct.discount || ''} onChange={handleInputChange} />
                    </div>
                    <div className="mb-2">
                      <label htmlFor="unitPrice" className="form-label small">New Price</label>
                      <input type="number" className="form-control form-control-sm" id="unitPrice" name="unitPrice" value={updatedProduct.unitPrice || ''} onChange={handleInputChange} />
                    </div>
                  </>
                ) : (
                  <>
                    {product.oldPrice !== undefined && product.discount !== undefined && product.unitPrice !== undefined ? (
                      <>
                        <p className="old-price mb-1">
                          <del>${product.oldPrice}</del>{" "}
                          <span className="old-price-discount text-danger">
                            ({product.discount}% off)
                          </span>
                        </p>
                        <p className="new-price text-bold mb-1">${product.unitPrice}</p>
                        <p className="text-secondary mb-1">
                          (Price as listed)
                        </p>
                      </>
                    ) : (
                      product.unitPrice !== undefined && (
                        <p className="new-price text-bold mb-1">${product.unitPrice}</p>
                      )
                    )}
                  </>
                )}
              </div>

              <div className="product-details my-4">
                <p className="details-title text-color mb-1">Product Details</p>
                {isEditing ? (
                  <textarea
                    className="form-control"
                    name="description"
                    rows="3"
                    value={updatedProduct.description || ''}
                    onChange={handleInputChange}
                  />
                ) : (
                  <p className="description">{product.description}</p>
                )}
              </div>

              {/* ... rest of the component ... */}

              <div className="my-4">
                <div className="row">
                  <div className="col-md-4">
                    <h5 className="font-weight-bold mb-0">Available Stock:</h5>
                    {isEditing ? (
                      <input
                        type="number"
                        className="form-control form-control-sm"
                        name="unitsInStock"
                        value={updatedProduct.unitsInStock || ''}
                        onChange={handleInputChange}
                      />
                    ) : (
                      <p>{product.unitsInStock}</p>
                    )}
                  </div>
                  {/* ... other admin info ... */}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}