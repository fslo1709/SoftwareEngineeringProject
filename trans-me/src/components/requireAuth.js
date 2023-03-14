import React from "react";
import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";
import { selectSession } from "../slices/sessionSlice";

export const RequireAuth = ({ children }) => {
  const { username, expirationTime } = useSelector(selectSession);
  const userIsLogged = username && expirationTime > Date.now();

  if (!userIsLogged) {
    return <Navigate to="/login" />;
  }
  return children;
};
