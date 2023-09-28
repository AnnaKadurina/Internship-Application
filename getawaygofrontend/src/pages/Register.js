import React, { useState } from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { registerUser, login } from '../api/userApis';
import '../styles/textfields.css';
import MuiAlert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import jwt_decode from 'jwt-decode';
import { useNavigate } from "react-router-dom";
import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';


const theme = createTheme();

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function SignUp() {
  const [open, setOpen] = React.useState(false);
  const [message, setMessage] = React.useState("");
  const [showPassword, setShowPassword] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const userData = {
      firstName: data.get('firstName'),
      lastName: data.get('lastName'),
      username: data.get('username'),
      email: data.get('email'),
      photo: 'https://res.cloudinary.com/dgqq1qtef/image/upload/v1678999074/user_wlsmtk.jpg',
      phone: data.get('phone'),
      address: data.get('address'),
      password: data.get('password')
    };
    try {
      const response = await registerUser(userData);
      console.log(response.data);
      try {
        const loginData = {
          username: data.get('username'),
          password: data.get('password')
        };
        const loginResponse = await login(loginData);
        const decodedToken = jwt_decode(loginResponse.data.accessToken);
        console.log(decodedToken)

        localStorage.setItem('encodedAccessToken', loginResponse.data.accessToken);
        localStorage.setItem('id', decodedToken.userId);

        navigate("/home");
        window.location.reload(true);

      } catch (error) {
        console.log(error)
        setOpen(true)
        setMessage("Failed to login into the new account, try again later")
      }
    } catch (error) {
      console.log(error);
      setOpen(true);
      setMessage(`Failed to register: ${error.response.data.message}`)
    }

  };

  const handleClose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    setOpen(false);
    setMessage("");
  }


  return (
    <ThemeProvider theme={theme}>
      <Container component="main" style={{ maxWidth: '500px' }}>
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Typography component="h1" variant="h5" style={{ color: '#4B8D97' }}>
            Sign up to create an account
          </Typography>
          <Box component="form" sx={{ mt: 3 }} onSubmit={handleSubmit}>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  autoComplete="given-name"
                  name="firstName"
                  required
                  fullWidth
                  id="firstName"
                  label="First Name"
                  autoFocus
                  className='textFieldStyle'
                  inputProps={{
                    maxLength: 100
                  }}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id="lastName"
                  label="Last Name"
                  name="lastName"
                  autoComplete="family-name"
                  className='textFieldStyle'
                  inputProps={{
                    maxLength: 100
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="username"
                  label="Username"
                  name="username"
                  autoComplete="username"
                  className='textFieldStyle'
                  inputProps={{
                    maxLength: 50
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  className='textFieldStyle'
                  inputProps={{
                    maxLength: 100
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="phone"
                  label="Phone Number"
                  name="phone"
                  autoComplete="phone"
                  className='textFieldStyle'
                  inputProps={{
                    maxLength: 20
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="address"
                  label="Home Address"
                  name="address"
                  autoComplete="address"
                  className='textFieldStyle'
                  inputProps={{
                    maxLength: 100
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password (min 8 char, 1 cap. letter, 1 num, 1 special)"
                  type={showPassword ? 'text' : 'password'}
                  id="password"
                  autoComplete="new-password"
                  className='textFieldStyle'
                  inputProps={{
                    maxLength: 100
                  }}
                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={() => setShowPassword(!showPassword)}
                          edge="end"
                          style={{ color: '#4B8D97' }}
                          aria-label="toggle password visibility"
                        >
                          {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
                        </IconButton>
                      </InputAdornment>
                    )
                  }}
                />
              </Grid>
            </Grid>
            <Button
              type="submit"
              id="registerBtn"
              fullWidth
              variant="contained"
              sx={{
                mt: 3,
                mb: 2,
                backgroundColor: '#4B8D97',
                '&:hover': {
                  backgroundColor: '#C3ACBA',
                },
              }}
            >
              Sign Up
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link href="/login" variant="body2" style={{ color: '#4B8D97' }}>
                  Already have an account? Sign in
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
          <Alert onClose={handleClose} severity="success" sx={{ width: '100%', backgroundColor: '#4B8D97' }}>
            {message}
          </Alert>
        </Snackbar>
      </Container>
    </ThemeProvider>
  );
}