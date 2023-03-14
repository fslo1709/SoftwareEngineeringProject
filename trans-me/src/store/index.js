import { configureStore } from "@reduxjs/toolkit";
import sessionReducer from "../slices/sessionSlice";
import globalReducer from "../slices/globalSlice";

export default configureStore({
  reducer: {
    session: sessionReducer,
    global: globalReducer,
  },
});
