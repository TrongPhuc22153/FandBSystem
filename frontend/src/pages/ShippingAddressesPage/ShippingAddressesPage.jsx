import { useCallback, useEffect, useState } from "react";
import {
  useShippingAddresses,
  useShippingAddressActions,
} from "../../hooks/addressHooks";
import { useModal } from "../../context/ModalContext";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import AddressListSection from "../../components/AddressListSection/AddressListSection";
import AddressProfileSection from "../../components/AddressProfileSection/AddressProfileSection";

// ShippingAddressesPage Component
const ShippingAddressesPage = () => {
  const {
    data: addresses,
    loading: loadingShippingAddresses,
    error: shippingAddressesError,
    mutate,
  } = useShippingAddresses();

  const {
    handleUpdateShippingAddress,
    updateError,
    updateLoading: isUpdateLoading,
    updateSuccess: updateSuccessMessage,
    resetUpdate: resetUpdateForm,
    handleCreateShippingAddress,
    createError,
    createLoading: isCreateLoading,
    createSuccess: createSuccessMessage,
    resetCreate: resetCreateForm,
  } = useShippingAddressActions();

  const { onOpen } = useModal();

  // State to hold the currently selected address for editing
  const [selectedAddress, setSelectedAddress] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});
  const [editingAddressId, setEditingAddressId] = useState(null);
  const [isDefaultAddress, setIsDefaultAddress] = useState(null);

  // Initialize selectedAddress and default address when addresses load
  useEffect(() => {
    if (addresses && addresses.length > 0) {
      const defaultAddress = addresses.find((addr) => addr.isDefault);
      setSelectedAddress(defaultAddress || addresses[0]);
      setIsDefaultAddress(defaultAddress ? defaultAddress.id : null);
    } else {
      setSelectedAddress(null);
      setIsDefaultAddress(null);
    }
  }, [addresses]);

  useEffect(() => {
    setFieldErrors(createError?.fields ?? {});
  }, [createError]);

  useEffect(() => {
    setFieldErrors(updateError?.fields ?? {});
  }, [updateError]);

  useEffect(() => {
    if (createSuccessMessage) {
      const timer = setTimeout(() => {
        resetCreateForm();
      }, 5000);

      return () => {
        clearTimeout(timer);
      };
    }
  }, [createSuccessMessage, resetCreateForm]);

  useEffect(() => {
    if (updateSuccessMessage) {
      const timer = setTimeout(() => {
        resetUpdateForm();
      }, 5000);

      return () => {
        clearTimeout(timer);
      };
    }
  }, [updateSuccessMessage, resetUpdateForm]);

  const handleAddressSelect = (addressId) => {
    const addressToEdit = addresses.find((addr) => addr.id === addressId);
    setSelectedAddress(addressToEdit ? { ...addressToEdit } : null);
    resetUpdateForm();
    resetCreateForm();
    setFieldErrors({});
    setIsEditing(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSelectedAddress((prevAddress) => ({
      ...prevAddress,
      [name]: value,
    }));
  };

  // modal
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedAddress) return;

    onOpen({
      title:
        selectedAddress.id === "new"
          ? "Create Shipping Address"
          : "Update Shipping Address",
      message: "Do you want to continue?",
      onYes: selectedAddress.id === "new" ? handleCreate : handleUpdate,
    });
  };

  const handleCreate = useCallback(async () => {
    const newAddressData = {
      ...selectedAddress,
      id: null,
      isDefault: false,
    };
    const newCreatedAddress = await handleCreateShippingAddress(newAddressData);
    if (newCreatedAddress) {
      mutate();
      setIsEditing(false);
      setFieldErrors({});
    }
  }, [handleCreateShippingAddress, selectedAddress, mutate]);

  const handleUpdate = useCallback(async () => {
    const updatedData = await handleUpdateShippingAddress(
      selectedAddress.id,
      selectedAddress
    );
    if (updatedData) {
      mutate((prevData) => {
        if (!prevData) return prevData;
        return prevData.filter((address) => address.id !== selectedAddress.id);
      }, false);
      mutate();

      setIsEditing(false);
      setFieldErrors({});
    }
  }, [handleUpdateShippingAddress, selectedAddress, mutate]);

  const handleSetDefaultAddress = useCallback(
    async (addressId) => {
      // First, set all addresses to not be the default.
      const updatedAddresses = addresses.map((addr) => ({
        ...addr,
        isDefault: addr.id === addressId, // Set the selected address to default
      }));
      // Find the new default address
      const newDefault = updatedAddresses.find((addr) => addr.id === addressId);

      const updatedData = await handleUpdateShippingAddress(
        addressId,
        newDefault
      );
      if (updatedData) {
        mutate((prevData) => {
          if (!prevData) return prevData;
          return prevData.filter((address) => address.id !== addressId);
        }, false);
        mutate();

        setIsEditing(false);

        setFieldErrors({});
        setSelectedAddress(newDefault);
        setIsDefaultAddress(addressId);
      }
    },
    [handleUpdateShippingAddress, mutate, addresses]
  );

  const handleClickCreateAddress = () => {
    setSelectedAddress({
      id: "new",
      shipName: "",
      phone: "",
      shipAddress: "",
      shipCity: "",
      shipDistrict: "",
      shipWard: "",
      isDefault: false,
    });
    setIsEditing(true);
    resetUpdateForm();
    resetCreateForm();
    setFieldErrors({});
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    resetUpdateForm();
    resetCreateForm();
    setFieldErrors({});
    setSelectedAddress(
      addresses.find((addr) => addr.isDefault) || addresses[0] || null
    );
  };

  if (loadingShippingAddresses) return <Loading />;
  if (shippingAddressesError?.message)
    return <ErrorDisplay message={shippingAddressesError.message} />;

  return (
    <main className="content px-5 py-3">
      <div className="container-fluid">
        <div className="container-xl px-4 mt-4">
          {updateError?.message && (
            <div className="alert alert-danger">{updateError.message}</div>
          )}
          {updateSuccessMessage && (
            <div className="alert alert-success">{updateSuccessMessage}</div>
          )}
          {createError?.message && (
            <div className="alert alert-danger">{createError.message}</div>
          )}
          {createSuccessMessage && (
            <div className="alert alert-success">{createSuccessMessage}</div>
          )}
          <div className="row">
            <AddressListSection
              addresses={addresses}
              selectedAddress={selectedAddress}
              handleAddressSelect={handleAddressSelect}
              handleClickCreateAddress={handleClickCreateAddress}
            />
            <AddressProfileSection
              selectedAddress={selectedAddress}
              isEditing={isEditing}
              fieldErrors={fieldErrors}
              handleInputChange={handleInputChange}
              handleSubmit={handleSubmit}
              isUpdateLoading={isUpdateLoading}
              isCreateLoading={isCreateLoading}
              handleCancelEdit={handleCancelEdit}
              handleSetDefaultAddress={handleSetDefaultAddress}
              setIsEditing={setIsEditing}
              setEditingAddressId={setEditingAddressId}
            />
          </div>
        </div>
      </div>
    </main>
  );
};

export default ShippingAddressesPage;
