import React, { useState } from "react";
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Box from "@mui/material/Box";
import { updateRole } from '../api/userApis';


const BecomeHostPage = () => {
    const [open, setOpen] = useState(false);

    const handleOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleUpdate = () => {
        if (localStorage.getItem('id')) {
            const id = localStorage.getItem('id')
            console.log(id)
            updateRole(id).then((response) => {
                console.log(response);
                localStorage.clear();
                window.location.href = "/home";
            })
                .catch((error) => console.error(error));
        }
    }

    return (
        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '5% 30%' }}>
            <Typography variant="h4" align="center" gutterBottom>
                Do you want to become a host in our platform?
            </Typography>
            <Typography variant="body1" align="center">
                Becoming a host in GetawayGo opens new oppurtunities for you. You can post listings of your properties and manage them easily. You can chat with your guests and see relevant statistics for all your posts, as well as receive reviews from people, who have stayed there. Become a host with just one click!
            </Typography>
            <Button variant="contained" sx={{ backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }} onClick={handleOpen} >
                Become a host
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle sx={{ backgroundColor: '#D4DCDE' }}>Become a Host</DialogTitle>
                <DialogContent sx={{ backgroundColor: '#D4DCDE' }}>
                    <DialogContentText>
                        By clicking confirm, you are agreeing to changing your profile to have the premissions of a host in our platform. After confirming you will be logged out and redirected to the homepage. Please log in again with your username and password to explore all the functionalities of being a host.
                    </DialogContentText>
                </DialogContent>
                <DialogActions sx={{ backgroundColor: '#D4DCDE' }}>
                    <Button onClick={handleClose} color="primary" sx={{ color: 'white', backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }}>
                        Cancel
                    </Button>
                    <Button onClick={handleUpdate} color="primary" sx={{ color: 'white', backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }}>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>
        </Box >
    );
};

export default BecomeHostPage;
