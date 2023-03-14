import React, { useState } from "react";
import {
  Alert,
  AppBar,
  Box,
  Button,
  Container,
  Grid,
  Link,
  Paper,
  Snackbar,
  TextField,
  Toolbar,
  Typography,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { AccountAPI } from "../../api";

function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {"Copyright © "}
      <Link color="inherit" href="register">
        Trans-me
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

export default function Register() {
  const navigate = useNavigate();
  const [alert, setAlert] = useState({});
  const [username, setUsername] = useState({
    value: "",
    error: false,
    helperText: "",
  });
  const [password, setPassword] = useState({
    value: "",
    error: false,
    helperText: "",
  });
  const [repeatPassword, setRepeatPassword] = useState({
    value: "",
    error: false,
    helperText: "",
  });

  const handleChangeUsername = (event) => {
    setUsername({ ...username, value: event.target.value });
  };
  const handleChangePassword = (event) => {
    setPassword({ ...password, value: event.target.value });
  };
  const handleChangeRepeatPassword = (event) => {
    setRepeatPassword({ ...repeatPassword, value: event.target.value });
  };
  const validate = (value) => {
    // 1. Username/Password must be at least 5 characters long
    // 2. Username/Password must be at most 20 characters long
    // 3. Username/Password must contain only letters, numbers, and underscores
    const reg = new RegExp("^[A-Za-z0-9_]{5,20}$");
    return reg.test(value);
  };

  const handleRegister = (event) => {
    event.preventDefault();
    if (
      validate(username.value) &&
      validate(password.value) &&
      validate(repeatPassword.value) &&
      password.value === repeatPassword.value
    ) {
      AccountAPI.postAccount(username.value, password.value).then(
        (response) => {
          if (response.data.msg.status === "success") {
            setAlert({
              open: true,
              severity: "success",
              msg: "Account created successfully",
            });
            setTimeout(() => {
              navigate("/login");
            }, 1000);
          } else {
            setUsername({ value: "", error: false, helperText: "" });
            setPassword({ value: "", error: false, helperText: "" });
            setRepeatPassword({ value: "", error: false, helperText: "" });
            setAlert({
              open: true,
              severity: "error",
              msg: "Username already exists",
            });
          }
        }
      );
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
      if (!validate(repeatPassword.value)) {
        setRepeatPassword({
          ...repeatPassword,
          error: true,
          helperText: "Password must be 5-20 characters long",
        });
      }
      if (password.value !== repeatPassword.value) {
        setRepeatPassword({
          ...repeatPassword,
          error: true,
          helperText: "Passwords do not match",
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
            marginTop: 10,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            border: 1,
          }}
        >
          <Typography component="h1" variant="h5" sx={{ mt: 5 }}>
            Create Account
          </Typography>

          <Box
            component="form"
            textAlign="center"
            onSubmit={handleRegister}
            noValidate
            sx={{ ml: 5, mr: 5, mb: 3, mt: 5 }}
          >
            <TextField
              margin="normal"
              required
              fullWidth
              autoFocus
              label="Username"
              name="username"
              id="username"
              value={username.value}
              error={username.error}
              helperText={username.helperText}
              onChange={handleChangeUsername}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="password"
              label="Password"
              name="password"
              type="password"
              value={password.value}
              error={password.error}
              helperText={password.helperText}
              onChange={handleChangePassword}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="repeatPassword"
              label="Repeat Password"
              type="password"
              id="repeatPassword"
              value={repeatPassword.value}
              error={repeatPassword.error}
              helperText={repeatPassword.helperText}
              onChange={handleChangeRepeatPassword}
            />
            <Button
              type="submit"
              size="large"
              variant="contained"
              fullWidth
              sx={{ mt: 4, mb: 4 }}
            >
              Register
            </Button>

            <Grid container>
              <Grid item sx={{ ml: 11, fontSize: 12 }}>
                Already have an account?
                <br />
                <Link href="login" variant="body2" sx={{ fontSize: 12 }}>
                  Log in
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Paper>
        <Copyright sx={{ mt: 5 }} />
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
