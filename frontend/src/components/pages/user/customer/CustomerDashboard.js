import { useState } from "react";

const userData = {
  id: 1,
  name: "Jane Doe",
  username: "janedoe123",
  firstName: "Jane",
  lastName: "Doe",
  email: "jane.doe@example.com",
  phone: "+1-555-123-4567",
  address: {
    street: "123 Maple Street",
    city: "Springfield",
    state: "IL",
    ward: "Ward 5",
    zipcode: "62704",
  },
  birthday: "1990-05-15",
  avatarUrl: "https://randomuser.me/api/portraits/men/45.jpg",
  isActive: true,
  createdAt: "2023-07-10T10:15:30Z",
  roles: ["user", "editor"],
};

export default function CustomerDashboard() {
  const [user, setUser] = useState({ ...userData });
  const [isEditing, setIsEditing] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const submitForm = (e) => {
    e.preventDefault();
    setIsLoading(true);
    // Simulate an API call to update user data
    setTimeout(() => {
      setIsLoading(false);
      setIsEditing(false); // Exit editing mode after submission
      alert("User data updated successfully!");
    }, 2000);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setUser((prev) => ({
      ...prev,
      address: {
        ...prev.address,
        [name]: value,
      },
    }));
  };

  return (
    <div className="container">
      <div className="container-xl px-4 mt-4">
        <div className="row">
          <div className="col-xl-4">
            <div className="card mb-4 mb-xl-0">
              <div className="card-header">
                <h5 className="card-title mb-0">Profile Picture</h5>
              </div>
              <div className="card-body text-center d-flex flex-column align-items-center justify-content-center">
                <img
                  className="img-account-profile rounded-circle mb-2"
                  src={user.avatarUrl}
                  alt={`${user.firstName} ${user.lastName}`}
                />
                <div className="small font-italic text-muted mb-4">
                  JPG or PNG no larger than 5 MB
                </div>
                <button className="btn btn-primary" type="button">
                  Upload new image
                </button>
              </div>
            </div>
          </div>
          <div className="col-xl-8">
            <div className="card mb-4">
              <div className="card-header">
                <h5 className="card-title mb-0">Account</h5>
              </div>
              <div className="card-body">
                <form onSubmit={submitForm}>
                  <div className="mb-3">
                    <label className="small mb-1" htmlFor="inputUsername">
                      Username
                    </label>
                    <input
                      required
                      className="form-control"
                      id="inputUsername"
                      type="text"
                      placeholder="Enter your username"
                      name="username"
                      value={user.username}
                      onChange={handleChange}
                      disabled={!isEditing}
                    />
                  </div>
                  <div className="row gx-3 mb-3">
                    <div className="col-md-6">
                      <label className="small mb-1" htmlFor="inputFirstName">
                        First name
                      </label>
                      <input
                        required
                        className="form-control"
                        id="inputFirstName"
                        type="text"
                        placeholder="Enter your first name"
                        name="firstName"
                        value={user.firstName}
                        onChange={handleChange}
                        disabled={!isEditing}
                      />
                    </div>
                    <div className="col-md-6">
                      <label className="small mb-1" htmlFor="inputLastName">
                        Last name
                      </label>
                      <input
                        required
                        className="form-control"
                        id="inputLastName"
                        type="text"
                        placeholder="Enter your last name"
                        name="lastName"
                        value={user.lastName}
                        onChange={handleChange}
                        disabled={!isEditing}
                      />
                    </div>
                  </div>
                  <div className="row gx-3 mb-3">
                    <div className="col-md-6">
                      <label className="small mb-1" htmlFor="inputAddress">
                        Address
                      </label>
                      <input
                        required
                        className="form-control"
                        id="inputAddress"
                        type="text"
                        placeholder="Enter your address"
                        name="street"
                        value={user.address.street}
                        onChange={handleAddressChange}
                        disabled={!isEditing}
                      />
                    </div>
                    <div className="col-md-6">
                      <label className="small mb-1" htmlFor="inputCity">
                        City
                      </label>
                      <input
                        required
                        className="form-control"
                        id="inputCity"
                        type="text"
                        placeholder="Enter your city"
                        name="city"
                        value={user.address.city}
                        onChange={handleAddressChange}
                        disabled={!isEditing}
                      />
                    </div>
                  </div>

                  <div className="row gx-3 mb-3">
                    <div className="col-md-6">
                      <label className="small mb-1" htmlFor="inputState">
                        State
                      </label>
                      <input
                        required
                        className="form-control"
                        id="inputState"
                        type="text"
                        placeholder="Enter your state"
                        name="state"
                        value={user.address.state}
                        onChange={handleAddressChange}
                        disabled={!isEditing}
                      />
                    </div>
                    <div className="col-md-6">
                      <label className="small mb-1" htmlFor="inputWard">
                        Ward
                      </label>
                      <input
                        required
                        className="form-control"
                        id="inputWard"
                        type="text"
                        placeholder="Enter your ward"
                        name="ward"
                        value={user.address.ward}
                        onChange={handleAddressChange}
                        disabled={!isEditing}
                      />
                    </div>
                  </div>

                  <div className="mb-3">
                    <label className="small mb-1" htmlFor="inputEmailAddress">
                      Email address
                    </label>
                    <input
                      required
                      className="form-control"
                      id="inputEmailAddress"
                      type="email"
                      placeholder="Enter your email address"
                      name="email"
                      value={user.email}
                      onChange={handleChange}
                      disabled={!isEditing}
                    />
                  </div>
                  <div className="row gx-3 mb-3">
                    <div className="col-md-6">
                      <label className="small mb-1" htmlFor="inputPhone">
                        Phone number
                      </label>
                      <input
                        required
                        className="form-control"
                        id="inputPhone"
                        type="tel"
                        placeholder="Enter your phone number"
                        name="phone"
                        value={user.phone}
                        onChange={handleChange}
                        disabled={!isEditing}
                      />
                    </div>
                    <div className="col-md-6">
                      <label className="small mb-1" htmlFor="inputBirthday">
                        Birthday
                      </label>
                      <input
                        required
                        className="form-control"
                        id="inputBirthday"
                        type="date"
                        name="birthday"
                        value={user.birthday}
                        onChange={handleChange}
                        disabled={!isEditing}
                      />
                    </div>
                  </div>
                  <div className="d-flex justify-content-between">
                    <button
                      className="btn btn-primary"
                      type="submit"
                      disabled={isLoading || !isEditing}
                    >
                      {isLoading ? "Saving..." : "Save changes"}
                    </button>
                    <button
                      className="btn btn-secondary"
                      type="button"
                      onClick={() => setIsEditing(!isEditing)}
                    >
                      {isEditing ? "Cancel" : "Edit"}
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
