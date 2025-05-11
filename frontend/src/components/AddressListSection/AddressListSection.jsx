import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHome, faPlus, faCheck } from "@fortawesome/free-solid-svg-icons";
import styles from "./AddressListSection.module.css";

const AddressListSection = ({
  addresses,
  selectedAddress,
  handleAddressSelect,
  handleClickCreateAddress,
}) => {
  return (
    <div className="col-xl-4 mb-3">
      <div className="card text-center">
        <div className="card-header">
          <h5 className="card-title mb-0">Address</h5>
        </div>
        <div className="card-body">
          <div className="p-3 border-2">
            {addresses &&
              addresses.map((address) => (
                <div
                  key={address.id}
                  className={`p-3 border rounded-3 mb-3 ${
                    selectedAddress?.id === address.id
                      ? "bg-light border border-primary rounded"
                      : ""
                  }`}
                  style={{ cursor: "pointer" }}
                  onClick={() => handleAddressSelect(address.id)}
                >
                  <div className="d-flex flex-row align-items-center justify-content-between">
                    <div className="d-flex flex-row align-items-center">
                      <div className="d-flex flex-column ms-3">
                        <span></span>
                        <h6 className="fw-bold">
                          <FontAwesomeIcon icon={faHome} />
                          {address.shipName}
                        </h6>
                        <span>
                          {address.shipAddress}, {address.shipCity},
                          {address.shipDistrict}, {address.shipWard}
                        </span>
                        {address.isDefault && (
                          <span className="text-success small">
                            <FontAwesomeIcon icon={faCheck} className="me-1" />
                            Default
                          </span>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              ))}

            <div
              className="p-3 border rounded-3 mt-4 py-4"
              style={{ cursor: "pointer" }}
            >
              <div
                className="d-flex flex-row align-items-center"
                onClick={handleClickCreateAddress}
              >
                <span className={styles["circle"]}>
                  <FontAwesomeIcon icon={faPlus} />
                </span>
                <div className="d-flex flex-column ms-3 mt-1">
                  <h6 className="fw-bold text-primary">Add New Address</h6>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddressListSection;
