import { useContext, createContext, useState } from "react";
import { useNavigate } from "react-router";
import { Login, Logout } from "../api/AuthAPI";
import { HOME_URI, LOGIN_URI } from "../constants/WebPageURI";
import Cookies from 'js-cookie';
import axios from "axios";
import { AUTHENTICATED_USER_URL, LOGOUT_URL } from "../constants/ApiEndpoints";

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
    const storedUser = localStorage.getItem('user');
    const initialUser = storedUser ? JSON.parse(storedUser) : {
        userId: "",
        username: "",
        email: "",
        isAuth: false,
        roles: [],
        permissions: [],
    };
    const [user, setUser] = useState(initialUser);
    const [token, setToken] = useState(Cookies.get("accessToken") || "");
    const navigate = useNavigate();

    const updateUserState = (userData) => {
        setUser(userData);
        localStorage.setItem('user', JSON.stringify(userData));
    };

    const loginAction = async (data) => {
        try {
            const res = await Login(data);
            if (res.data) {
                const userData = res.data.data;
                setToken(userData.accessToken);
                Cookies.set("accessToken", userData.accessToken, { expires: 7, path: '/', sameSite: 'strict' });

                // Fetch additional user data after successful login
                try {
                    const additionalUserDataResponse = await axios(AUTHENTICATED_USER_URL, {
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${userData.accessToken}`
                        }
                    });
                    if (additionalUserDataResponse.data) {
                        const infodata = additionalUserDataResponse.data;

                        // 2.  Create an empty array to store the transformed roles.
                        const transformedRoles = [];

                        // 3.  Iterate over the 'roles' array.
                        infodata.roles.forEach(roleObject => {
                            // Get the roleName from each object.
                            const roleName = roleObject.roleName;
                            // Add the roleName to the transformedRoles array.
                            transformedRoles.push(roleName);
                        });

                        updateUserState({
                            userId: infodata.userId,
                            username: infodata.username,
                            email: infodata.email,
                            isAuth: true,
                            roles: transformedRoles, // Update with actual roles from the response
                            permissions: [] // Update with actual permissions
                        });
                    } else {
                        console.warn('No additional user data received.');
                    }
                } catch (additionalDataErr) {
                    console.error('Error fetching additional user data:', additionalDataErr);
                    // Optionally handle this error, e.g., show a warning to the user
                }
                
                navigate(HOME_URI);
                return;
            }
        } catch (err) {
            console.error(err);
        }
    };

    const logout = async () => {
        updateUserState({
            username: "",
            email: "",
            isAuth: false,
            roles: [],
            permissions: [],
        });
        setToken("");
        Cookies.remove("accessToken", { path: '/' });
        localStorage.removeItem('user'); // Clear user data from local storage
        try {
            const res = await axios.post(LOGOUT_URL, {}, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                }
            });
            if (res.data) {
                navigate(LOGIN_URI);
                return;
            }
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <AuthContext.Provider value={{ token, user, loginAction, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;

export const useAuth = () => {
    return useContext(AuthContext);
};