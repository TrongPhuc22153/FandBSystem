const ShippingAddressForm = ({ address, setAddress, fieldErrors, selectedAddressId, handleAddressChange, addresses }) => {
  const handleInputChange = (e) => {
    const { id, value } = e.target;
    setAddress((prev) => ({ ...prev, [id]: value }));
  };

  return (
    <div className="space-y-6">
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Select Address</label>
        <select
          className="w-full p-2 border rounded-md focus:ring-2 focus:ring-blue-500"
          value={selectedAddressId || ""}
          onChange={handleAddressChange}
        >
          <option value="" disabled>Choose an address</option>
          {addresses?.length > 0 ? (
            addresses.map((addr) => (
              <option key={addr.id} value={addr.id}>
                {addr.shipName}, {addr.shipAddress}, {addr.shipCity}
              </option>
            ))
          ) : (
            <option disabled>No saved addresses</option>
          )}
          <option value="new">Enter new address</option>
        </select>
      </div>

      {['shipName', 'shipAddress', 'shipCity', 'shipDistrict', 'shipWard', 'phone'].map((field) => (
        <div key={field} className="mb-3">
          <label className="block text-sm font-medium text-gray-700 mb-1 capitalize">{field.replace('ship', '')}</label>
          <input
            type={field === 'phone' ? 'tel' : 'text'}
            id={field}
            className={`w-full p-2 border rounded-md ${fieldErrors[field] ? 'border-red-500' : ''}`}
            value={address[field]}
            onChange={handleInputChange}
            disabled={selectedAddressId && selectedAddressId !== "new"}
          />
          {fieldErrors[field]?.map((error, index) => (
            <p key={index} className="text-red-500 text-sm mt-1">{error}</p>
          ))}
        </div>
      ))}
    </div>
  );
};
export default ShippingAddressForm
