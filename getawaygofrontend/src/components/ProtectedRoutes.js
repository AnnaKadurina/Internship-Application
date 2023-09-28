import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import jwt_decode from 'jwt-decode';


const useAuth = () => {
    var jwt_token = localStorage.getItem('encodedAccessToken')
    if (jwt_token) {
        const decodedToken = jwt_decode(jwt_token);
        if (decodedToken) {
            return {
                auth: true,
                role: decodedToken.role,
            };
        }
    } else {

        return {
            auth: false,
            role: null,
        };
    }

};

const ProtectedRoutes = (props) => {
    const { auth, role } = useAuth();

    if (props.roleRequired) {
        return auth ? (
            props.roleRequired === role ? (
                <Outlet />
            ) : (
                <Navigate to="/unauthorized" />
            )
        ) : (
            <Navigate to="/login" />
        );
    } else {
        return auth ? <Outlet /> : <Navigate to="/login" />;
    }
};

export default ProtectedRoutes;
