import React, { useState } from "react";
import {
  Alert,
  AppBar,
  Avatar,
  Box,
  Button,
  Container,
  FormControl,
  FormHelperText,
  Grid,
  IconButton,
  InputAdornment,
  InputLabel,
  Link,
  OutlinedInput,
  Paper,
  Snackbar,
  TextField,
  Toolbar,
  Typography,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import { AccountAPI } from "../../api";
import { setLogin } from "../../slices/sessionSlice";
import { useDispatch } from "react-redux";

function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {"Copyright © "}
      <Link color="inherit" href="/">
        Trans-me
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

export default function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [alert, setAlert] = useState({});
  const [username, setUsername] = useState({
    value: "",
    error: false,
    helperText: "",
  });
  const [password, setPassword] = useState({
    value: "",
    show: false,
    error: false,
    helperText: "",
  });

  const handleChangeUsername = (event) => {
    setUsername({
      ...username,
      value: event.target.value,
    });
  };

  const handleChangePassword = (event) => {
    setPassword({ ...password, value: event.target.value });
  };

  const handleClickShowPassword = () => {
    setPassword({ ...password, show: !password.show });
  };

  const handleMouseDownPassword = (event) => {
    event.preventDefault();
  };

  const validate = (value) => {
    // 1. Username/Password must be at least 5 characters long
    // 2. Username/Password must be at most 20 characters long
    // 3. Username/Password must contain only letters, numbers, and underscores
    const reg = new RegExp("^[A-Za-z0-9_]{5,20}$");
    return reg.test(value);
  };

  const handleLogin = (event) => {
    event.preventDefault();
    if (validate(username.value) && validate(password.value)) {
      AccountAPI.getAccount(username.value, password.value).then((response) => {
        if (response.data.data) {
          dispatch(
            setLogin({
              username: username.value,
              password: password.value,
            })
          );
          navigate("/");
        } else {
          setUsername({ ...username, error: false, helperText: "" });
          setPassword({ value: "", error: false, helperText: "" });
          setAlert({
            open: true,
            severity: "error",
            msg: "Incorrect username or password",
          });
        }
      });
    } else {
      if (!validate(username.value)) {
        setUsername({
          ...username,
          error: true,
          helperText: "Username must be 5-20 characters long",
        });
      }
      if (!validate(password.value)) {
        setPassword({
          ...password,
          error: true,
          helperText: "Password must be 5-20 characters long",
        });
      }
    }
  };

  return (
    <>
      <AppBar sx={{ width: "100%", pl: 2, pr: 2 }}>
        <Toolbar disableGutters>
          <Typography
            variant="h4"
            noWrap
            sx={{
              mr: 2,
              display: "flex",
              flexGrow: 1,
              fontFamily: "Playfair Display SC",
              fontWeight: 500,
              color: "inherit",
              textDecoration: "none",
            }}
          >
            TransMe
          </Typography>
        </Toolbar>
      </AppBar>
      <Container component="main" maxWidth="xs">
        <Toolbar />
        <Paper
          sx={{
            marginTop: 15,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            border: 1,
          }}
        >
          <Avatar
            sx={{ width: 100, height: 100, mt: -6.5, mb: 1 }}
            src={require("./img/favicon.png")}
          />

          <Typography component="h1" variant="h5">
            Welcome
          </Typography>

          <Box
            component="form"
            textAlign="center"
            onSubmit={handleLogin}
            noValidate
            sx={{ ml: 5, mr: 5, mb: 3, mt: 3 }}
          >
            <TextField
              fullWidth
              label="Username"
              id="username"
              value={username.value}
              error={username.error}
              helperText={username.helperText}
              onChange={handleChangeUsername}
              sx={{ mb: 2 }}
            />
            <FormControl
              fullWidth
              sx={{ mb: 2 }}
              variant="outlined"
              error={password.error}
            >
              <InputLabel>Password</InputLabel>
              <OutlinedInput
                id="login-password"
                type={password.show ? "text" : "password"}
                value={password.value}
                onChange={handleChangePassword}
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={handleClickShowPassword}
                      onMouseDown={handleMouseDownPassword}
                      edge="end"
                    >
                      {password.show ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                }
                label="Password"
              />
              <FormHelperText id="login-password-helper-text">
                {password.helperText}
              </FormHelperText>
            </FormControl>
            <Button
              type="submit"
              size="large"
              variant="contained"
              fullWidth
              sx={{ mt: 4, mb: 4 }}
            >
              Login
            </Button>

            <Grid container>
              <Grid item sx={{ textAlign: "center", ml: 11.5, fontSize: 12 }}>
                Don't have an account?
                <br />
                <Link href="register" variant="body2" sx={{ fontSize: 12 }}>
                  Register Now
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Paper>
        <Copyright sx={{ mt: 8, mb: 4 }} />
      </Container>
      <Snackbar
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
        open={alert?.open}
        autoHideDuration={5000}
        onClose={() => setAlert({ ...alert, open: false })}
      >
        <Alert variant="filled" severity={alert?.severity}>
          {alert?.msg}
        </Alert>
      </Snackbar>
    </>
  );
}
