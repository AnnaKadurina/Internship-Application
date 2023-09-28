import React, { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import '../styles/textfields.css';
import { uploadPicture, updateData } from '../api/userApis';
import { getUser } from '../api/authorizationApis';
import MuiAlert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';

const theme = createTheme();

const Alert = React.forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function SubmitChanges() {
    const [selectedPicture, setSelectedPicture] = useState('https://res.cloudinary.com/dgqq1qtef/image/upload/v1678999074/user_wlsmtk.jpg');
    const [userData, setUserData] = useState(null);
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState("");
    const [isUpdated, setIsUpdated] = useState(false)

    useEffect(() => {
        const id = localStorage.getItem('id');

        getUser(id)
            .then((userData) => {
                setUserData(userData);
                setSelectedPicture(userData.data.photo)
                setIsUpdated(false)
            })
            .catch((error) => {
                console.log(error);
            });
    }, [isUpdated]);

    const handlePictureChange = (event) => {
        const picture = URL.createObjectURL(event.target.files[0]);
        setSelectedPicture(picture);
    };

    const handlePictureUpload = (event) => {
        event.preventDefault();
        const photoInput = document.querySelector('#photo');
        const photo = photoInput.files[0];

        const formData = new FormData();
        formData.append('photo', photo);

        console.log(photo);
        try {
            const response = uploadPicture(formData, localStorage.getItem('id'));
            setIsUpdated(true)
            console.log(response.data);
        } catch (error) {
            console.log(error.data);
            setOpen(true);
            setMessage(error.data)
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const data = new FormData(event.currentTarget);
        const userData = {
            username: data.get('username'),
            firstName: data.get('firstName'),
            lastName: data.get('lastName'),
            email: data.get('email'),
            phone: data.get('phone'),
            address: data.get('address')
        };
        try {
            await updateData(userData, localStorage.getItem('id'));
            setUserData(userData)
            setIsUpdated(true)
            setOpen(true);
            setMessage(`Successfully updated!`);
        } catch (error) {
            setOpen(true);
            setMessage(`Failed to update data: ${error.message}`);
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
                    {userData && userData.data && (

                        <Paper elevation={3} sx={{ p: 2, mb: 2, backgroundColor: '#4B8D97', border: '2px solid white', boxShadow: '3px 3px 5px rgba(255,255,255,1)' }}>
                            <Typography variant="h6" gutterBottom sx={{ color: 'white' }}>
                                Logged in as {userData.data.username}
                            </Typography>
                            <Typography variant="body1" gutterBottom sx={{ color: 'white' }}>
                                Email: {userData.data.email}
                            </Typography>
                            <Typography variant="body1" gutterBottom sx={{ color: 'white' }}>
                                First name: {userData.data.firstName}
                            </Typography>
                            <Typography variant="body1" gutterBottom sx={{ color: 'white' }}>
                                Last name: {userData.data.lastName}
                            </Typography>
                            <Typography variant="body1" gutterBottom sx={{ color: 'white' }}>
                                Address: {userData.data.address}
                            </Typography>
                            <Typography variant="body1" gutterBottom sx={{ color: 'white' }}>
                                Phone number: {userData.data.phone}
                            </Typography>
                        </Paper>
                    )}

                    <img src={selectedPicture} alt="Profile" style={{ width: 95, height: 100, borderRadius: '50%' }} />
                    <Box component="form" sx={{ mt: 3 }} onSubmit={handlePictureUpload}>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <TextField
                                    name="photo"
                                    required
                                    fullWidth
                                    id="photo"
                                    type="file"
                                    autoFocus
                                    onChange={handlePictureChange}
                                    className='textFieldStyle'
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            variant="contained"
                            fullWidth
                            sx={{
                                mt: 3,
                                mb: 2,
                                backgroundColor: '#4B8D97',
                                '&:hover': {
                                    backgroundColor: '#C3ACBA',
                                },
                            }}
                        >
                            Upload picture
                        </Button>
                    </Box>
                    <Box component="form" sx={{ mt: 3 }} onSubmit={handleSubmit}>
                        <Grid container spacing={2}>

                            <Grid item xs={12} sm={6}>
                                <TextField
                                    autoComplete="given-name"
                                    name="firstName"
                                    required
                                    fullWidth
                                    id="firstName"
                                    label="FirstName"
                                    autoFocus
                                    className='textFieldStyle'
                                    defaultValue={userData ? userData.data.firstName : ''}
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
                                    defaultValue={userData ? userData.data.lastName : ''}
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
                                    defaultValue={userData ? userData.data.username : ''}
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
                                    defaultValue={userData ? userData.data.email : ''}
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
                                    defaultValue={userData ? userData.data.phone : ''}
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
                                    defaultValue={userData ? userData.data.address : ''}
                                    inputProps={{
                                        maxLength: 100
                                    }}

                                />
                            </Grid>
                        </Grid>
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
                            }}
                        >
                            Submit changes
                        </Button>
                    </Box>
                </Box>
                <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                    <Alert onClose={handleClose} severity="success" sx={{ width: '100%', backgroundColor: '#4B8D97' }}>
                        {message}
                    </Alert>
                </Snackbar>
            </Container>
        </ThemeProvider>
    )
}
