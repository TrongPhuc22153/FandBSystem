import { useParams } from "react-router-dom";
import { useProfile } from "../../hooks/profileHooks";
import Loading from "../../components/Loading/Loading";
import ErrorDisplay from "../../components/ErrorDisplay/ErrorDisplay";
import { formatDate } from "../../utils/datetimeUtils";
import SingleImageDisplay from "../../components/SingleImageDisplay/SingleImageDisplay";

const AdminUserProfilePage = () => {
  const { userId } = useParams();
  const { data: profile, isLoading, error } = useProfile(userId);

  if (isLoading) return <Loading />;

  if (error) {
    return <ErrorDisplay message={error.message} />;
  }

  return (
    <div className="container">
      <h1 className="mb-3">User Profile</h1>
      <div className="form-section">
        <h2 id="form-title">View User Profile</h2>
        <div className="form-container">
          <div className="form-left">
            <SingleImageDisplay imageUrl={profile.picture} />
          </div>
          <div className="form-right">
            <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label htmlFor="username">Username</label>
                  <input
                    type="text"
                    id="username"
                    name="username"
                    className="form-control"
                    value={profile.user?.username}
                    disabled
                  />
                </div>
              </div>

              <div className="col-md-6">
                <div className="form-group">
                  <label htmlFor="email">Email</label>
                  <input
                    type="text"
                    id="email"
                    name="email"
                    className="form-control"
                    value={profile.user?.email}
                    disabled
                  />
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label htmlFor="firstname">First Name</label>
                  <input
                    type="text"
                    id="firstname"
                    name="firstname"
                    className="form-control"
                    value={profile.user?.firstName}
                    disabled
                  />
                </div>
              </div>
              <div className="col-md-6">
                <div className="form-group">
                  <label htmlFor="lastname">Last Name</label>
                  <input
                    type="text"
                    id="lastname"
                    name="lastname"
                    className="form-control"
                    value={profile.user?.lastName}
                    disabled
                  />
                </div>
              </div>
            </div>

            <div className="row">
              <div className="col-lg-6">
                <div className="form-group">
                  <label>User Created On:</label>
                  <input
                    type="text"
                    id="user-createdon"
                    name="user-createdon"
                    className="form-control"
                    value={formatDate(profile.user?.createdOn)}
                    disabled
                  />
                </div>
              </div>
              <div className="col-lg-6">
                <div className="form-group">
                  <div className="form-group">
                    <label>User Last Updated On:</label>
                    <input
                      type="text"
                      id="user-lastUpdatedOn"
                      name="user-lastUpdatedOn"
                      className="form-control"
                      value={formatDate(profile.user?.lastUpdatedOn)}
                      disabled
                    />
                  </div>
                </div>
              </div>
            </div>

            <hr />

            <div className="row">
              <div className="col-md-12">
                <div className="form-group">
                  <label htmlFor="address">Address</label>
                  <input
                    type="text"
                    id="address"
                    name="address"
                    className="form-control"
                    value={profile.address}
                    disabled
                  />
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label htmlFor="phone">Phone</label>
                  <input
                    type="text"
                    id="phone"
                    name="phone"
                    className="form-control"
                    value={profile.phone}
                    disabled
                  />
                </div>
              </div>
              <div className="col-md-6">
                <div className="form-group">
                  <label htmlFor="city">City</label>
                  <input
                    type="text"
                    id="city"
                    name="city"
                    className="form-control"
                    value={profile.city}
                    disabled
                  />
                </div>
              </div>
            </div>

            <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label htmlFor="district">District</label>
                  <input
                    type="text"
                    id="district"
                    name="district"
                    className="form-control"
                    value={profile.district}
                    disabled
                  />
                </div>
              </div>
              <div className="col-md-6">
                <div className="form-group">
                  <label htmlFor="ward">Ward</label>
                  <input
                    type="text"
                    id="ward"
                    name="ward"
                    className="form-control"
                    value={profile.ward}
                    disabled
                  />
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-lg-6">
                <div className="form-group">
                  <label>Profile Created On:</label>
                  <input
                    type="text"
                    id="profile-createdon"
                    name="profile-createdon"
                    className="form-control"
                    value={formatDate(profile.createdOn)}
                    disabled
                  />
                </div>
              </div>
              <div className="col-lg-6">
                <div className="form-group">
                  <div className="form-group">
                    <label>Profile Last Updated On:</label>
                    <input
                      type="text"
                      id="profile-lastUpdatedOn"
                      name="profile-lastUpdatedOn"
                      className="form-control"
                      value={formatDate(profile.lastUpdatedOn)}
                      disabled
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminUserProfilePage;
