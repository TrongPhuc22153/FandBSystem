import { useEffect, useState } from "react";
import { useAuth } from "../hooks/AuthContext";
import axios from "axios";
import { CUSTOMER_USER_PROFILE_URL } from "../constants/ApiEndpoints";
import { getDefaultUser } from "../services/ImageService";

export default function EmployeeDashboard() {
  // 1. Define the state using useState
  const [userProfile, setUserProfile] = useState({
    customerId: '',
    contactName: '',
    profile: {
      address: '',
      ward: '',
      district: '',
      city: '',
      phone: '',
      picture: '',
      user: {
        userId: '',
        username: '',
        email: '',
        firstName: '',
        lastName: ''
      },
    },
  });
  const [isEditing, setIsEditing] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const { token } = useAuth();


  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserProfile(prevProfile => ({
      ...prevProfile,
      [name]: value,
    }));
  };

  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setUserProfile((prevProfile) => ({
      ...prevProfile,
      profile: {
        ...prevProfile.profile,
        [name]: value,
      },
    }));
  };

  const submitForm = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    try {
      const response = await axios.put(CUSTOMER_USER_PROFILE_URL, JSON.stringify(userProfile), {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      });
      setIsLoading(false);
      setIsEditing(false); // Disable editing after successful save
      alert('Profile updated successfully!');
      // Optionally, update the local userProfile with the response data
      setUserProfile(response.data); // Assuming the server returns the updated profile
    } catch (err) {
      console.error('Error updating profile:', err);
      setError(err);
      alert('Failed to update profile.');
      // Optionally, handle specific error codes from the server
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    const fetchCustomerProfile = async () => {
      try {
        const res = await axios.get(CUSTOMER_USER_PROFILE_URL, {
          headers: {
            'Content-Type': 'application/json',
            // Include the Authorization header with the token if needed
            Authorization: `Bearer ${token}`, 
          },
        });

        // 2. Update the state with the fetched data
        const data = res.data;
        setUserProfile({
          customerId: data.customerId || '',
          contactName: data.contactName || '',
          profile: {
            address: data.profile?.address || '',
            ward: data.profile?.ward || '',
            district: data.profile?.district || '',
            city: data.profile?.city || '',
            phone: data.profile?.phone || '',
            picture: data.profile?.picture || '',
            user: {
              userId: data.profile?.user?.userId || '',
              username: data.profile?.user?.username || '',
              email: data.profile?.user?.email || '',
              firstName: data.profile?.user?.firstName || '',
              lastName: data.profile?.user?.lastName || '',
            },
          },
        });


      } catch (error) {
        console.error('Error fetching employee profile:', error);
        setError(error);
        // Handle errors appropriately (e.g., show a message to the user)
        setUserProfile({
          customerId: 'err',
          contactName: 'err',
          profile: {
            address: 'err',
            ward: 'err',
            district: 'err',
            city: 'err',
            phone: 'err',
            picture: 'err',
            user: {
              userId: 'err',
              username: 'err',
              email: 'err',
              firstName: 'err',
              lastName: 'err',
              enabled: false,
              emailVerified: false,
              roles: [],
            },
          },
        });
      }
    };

    fetchCustomerProfile();
  }, []);

  if (error) {
    return <div>Error: {error.message}</div>; // Or custom error message
  }

  // Render the user profile data
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
                  src={userProfile.profile.picture || getDefaultUser()}
                  alt={`${userProfile.profile.user.firstName} ${userProfile.profile.user.lastName}`}
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
                      value={userProfile.profile.user.username}
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
                        value={userProfile.profile.user.firstName}
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
                        value={userProfile.profile.user.lastName}
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
                        name="address"
                        value={userProfile.profile.address}
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
                        value={userProfile.profile.city}
                        onChange={handleAddressChange}
                        disabled={!isEditing}
                      />
                    </div>
                  </div>

                  <div className="row gx-3 mb-3">
                    <div className="col-md-6">
                      <label className="small mb-1" htmlFor="inputState">
                        District
                      </label>
                      <input
                        required
                        className="form-control"
                        id="inputState"
                        type="text"
                        placeholder="Enter your state"
                        name="district"
                        value={userProfile.profile.district}
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
                        value={userProfile.profile.ward}
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
                      value={userProfile.profile.user.email}
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
                        value={userProfile.profile.phone}
                        onChange={handleAddressChange}
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
                      {isLoading ? 'Saving...' : 'Save changes'}
                    </button>
                    <button
                      className="btn btn-secondary"
                      type="button"
                      onClick={() => setIsEditing(!isEditing)}
                    >
                      {isEditing ? 'Cancel' : 'Edit'}
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
