import React, { useState, useEffect } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import '../styles/textfields.css';
import { getAllPropertiesForHost, deleteProperty, getProperty } from '../api/propertyApis';
import MuiAlert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import { useNavigate } from 'react-router-dom';
import Pagination from '@mui/material/Pagination';

const theme = createTheme();

const Alert = React.forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function SubmitChanges() {
    const navigate = useNavigate();

    const [propertyData, setPropertyData] = useState(null);
    const [open, setOpen] = useState(false);
    const [openDialog, setOpenDialog] = useState(false);
    const [isUpdated, setIsUpdated] = useState(false);
    const [message, setMessage] = useState("");
    const [page, setPage] = useState(1);

    const handleOpenDialog = () => {
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    const handleShowMore = (id) => {
        getProperty(id).then(response => {
            navigate(`/host/update/property/${id}`, { state: response.data });
        }).catch(error => {
            console.log(error.data);
        });
    }

    const handlePlanning = (id) => {
        getProperty(id).then(response => {
            navigate(`/host/planning/${id}`, { state: response.data });
        }).catch(error => {
            console.log(error.data);
        });
    }


    useEffect(() => {
        const id = localStorage.getItem('id');

        getAllPropertiesForHost(id)
            .then((response) => {
                const allProperties = response.data.allProperties.map((property) => ({
                    id: property.propertyId,
                    ...property,
                }));
                setPropertyData(allProperties)
                setIsUpdated(false)
            })
            .catch((error) => {
                console.log(error);
            });
    }, [isUpdated]);


    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
        setMessage("");
    }

    const handleDelete = (id) => {
        deleteProperty(id).then((response) => {
            setOpen(true);
            setMessage(`Successfully deleted property with ID: ${id}`)
            setIsUpdated(true)
            setOpenDialog(false)
            console.log(response)
        })
            .catch((error) => console.error(error));
    }

    return (
        <ThemeProvider theme={theme}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '30px 0', }}>
                <CssBaseline />

                {propertyData && propertyData.length > 0 ? (
                    <>
                        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flexWrap: 'wrap', gap: '30px', width: '80%' }} id='properties'>
                            {propertyData.slice((page - 1) * 6, page * 6).map((property) => (
                                <Paper
                                    key={property.id}
                                    elevation={3}
                                    sx={{
                                        p: 2,
                                        backgroundColor: "#4B8D97",
                                        border: "2px solid white",
                                        boxShadow: "3px 3px 5px rgba(255,255,255,1)",
                                        minWidth: '200px',
                                        maxWidth: '400px',
                                        textAlign: 'center',
                                        margin: '10px',
                                    }}
                                >
                                    <Typography variant="h6" gutterBottom sx={{ color: "white" }}>
                                        {property.name}
                                    </Typography>
                                    <Typography variant="body1" gutterBottom sx={{ color: "white" }}>
                                        <LocationOnIcon sx={{ color: '#8E3B46', fontSize: 25 }} />
                                        {property.town}, {property.country}
                                    </Typography>
                                    <Stack spacing={2} direction="row" style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap' }}>

                                        <Button variant="contained" sx={{ color: '#284C52', backgroundColor: 'white', '&:hover': { backgroundColor: '#C3ACBA', color: 'white' }, }} onClick={() => handleShowMore(property.id)}>Update</Button>
                                        <Button variant="contained" sx={{ color: '#284C52', backgroundColor: 'white', '&:hover': { backgroundColor: '#C3ACBA', color: 'white' }, }} onClick={() => handlePlanning(property.id)} id='planning'>Planning</Button>
                                        <Button variant="contained" sx={{ color: '#284C52', backgroundColor: 'white', '&:hover': { backgroundColor: '#C3ACBA', color: 'white' }, }} onClick={() => handleOpenDialog(property.id)}>Delete</Button>
                                    </Stack>
                                    <Dialog open={openDialog} onClose={handleClose}>
                                        <DialogTitle sx={{ backgroundColor: '#D4DCDE' }}>Delete a property</DialogTitle>
                                        <DialogContent sx={{ backgroundColor: '#D4DCDE' }}>
                                            <DialogContentText>
                                                By clicking the button confirm, this property will be deleted permanently.
                                            </DialogContentText>
                                        </DialogContent>
                                        <DialogActions sx={{ backgroundColor: '#D4DCDE' }}>
                                            <Button onClick={handleCloseDialog} color="primary" sx={{ color: 'white', backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }}>
                                                Cancel
                                            </Button>
                                            <Button onClick={() => handleDelete(property.id)} color="primary" sx={{ color: 'white', backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }}>
                                                Confirm
                                            </Button>
                                        </DialogActions>
                                    </Dialog>
                                </Paper>
                            ))}

                        </div>
                    </>

                ) : (
                    <p style={{ marginTop: 220 }}>No properties found.</p>
                )}

                <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                    <Alert
                        onClose={handleClose}
                        severity="success"
                        sx={{ width: "100%", backgroundColor: "#4B8D97" }}
                    >
                        {message}
                    </Alert>
                </Snackbar>
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column' }}>
                {propertyData && propertyData.length > 0 && (

                    <Stack spacing={2} sx={{ my: 2, justifyContent: 'center', textAlign: 'center' }}>
                        <Pagination
                            count={Math.ceil(propertyData.length / 6)}
                            page={page}
                            onChange={(event, value) => setPage(value)}
                            sx={{ "& .Mui-selected": { backgroundColor: "#C3ACBA", color: "#fff" } }}
                        />
                    </Stack>
                )}

            </div>

        </ThemeProvider>

    );

}
