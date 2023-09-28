import * as React from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { login } from '../api/userApis';
import { useNavigate } from "react-router-dom";
import jwt_decode from 'jwt-decode';
import MuiAlert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';

const theme = createTheme();

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function SignIn() {
  const [open, setOpen] = React.useState(false);
  const [message, setMessage] = React.useState("");

  const navigate = useNavigate();
  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const userData = {
      username: data.get('username'),
      password: data.get('password')
    };
    try {
      console.log(userData)
      const response = await login(userData);

      const decodedToken = jwt_decode(response.data.accessToken);
      console.log(decodedToken)

      localStorage.setItem('encodedAccessToken', response.data.accessToken);
      localStorage.setItem('id', decodedToken.userId);

      navigate("/home");
      window.location.reload(true);

    } catch (error) {
      console.log(error);
      setOpen(true);
      setMessage(`Failed to login: ${error.response.data.message}`)
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
      <Container component="main" maxWidth="xs">
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
            Log in
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              autoFocus
              sx={{
                '& label': {
                  color: '#4B8D97',
                },
                '& .MuiInputLabel-shrink': {
                  color: '#4B8D97',
                },
                '& .MuiOutlinedInput-root': {
                  '& fieldset': {
                    borderColor: '#4B8D97',
                  },
                  '&:hover fieldset': {
                    borderColor: '#4B8D97',
                  },
                  '&.Mui-focused fieldset': {
                    borderColor: '#4B8D97',
                  },
                },
              }}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              sx={{
                '& label': {
                  color: '#4B8D97',
                },
                '& .MuiInputLabel-shrink': {
                  color: '#4B8D97',
                },
                '& .MuiOutlinedInput-root': {
                  '& fieldset': {
                    borderColor: '#4B8D97',
                  },
                  '&:hover fieldset': {
                    borderColor: '#4B8D97',
                  },
                  '&.Mui-focused fieldset': {
                    borderColor: '#4B8D97',
                  },
                },
              }}
            />

            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{
                mt: 3,
                mb: 2,
                backgroundColor: '#4B8D97',
                '&:hover': {
                  backgroundColor: '#C3ACBA',
                },
              }}            >
              Log In
            </Button>
            <Grid container>
              <Grid item>
                <Link href="/register" variant="body2" style={{ color: '#4B8D97' }}>
                  {"Don't have an account? Sign Up"}
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