import { getDefaultUser } from "../services/ImageService";
import { useAuth } from "../hooks/AuthContext";

export default function UserHeader() {
  const { user } = useAuth();

  return (
    <div className="window-main-header">
      <div className="publish-actions ms-auto">
        <div className="publish-info d-flex align-items-center">
          <img style={{ width: "30px", height: "30px" }}
            src={
              user.image || getDefaultUser()
            }
            className="user-image rounded-3 me-2"
            alt={user.username}
          />
          <span>
            {user.username}
          </span>
        </div>
      </div>
    </div>
  );
}
