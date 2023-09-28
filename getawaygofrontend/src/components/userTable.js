import React, { useState, useEffect } from 'react';
import { deactivateUser, getUsers, activateUser, deleteUser } from '../api/userApis';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Table from './table.js';
import MuiAlert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';

const columns = [
    { field: 'userId', headerName: 'ID', width: 70 },
    { field: 'firstName', headerName: 'First name', width: 130 },
    { field: 'lastName', headerName: 'Last name', width: 130 },
    { field: 'email', headerName: 'Email Address', width: 180 },
    { field: 'username', headerName: 'Username', width: 160, },
    { field: 'phone', headerName: 'Phone number', width: 130 },
    { field: 'address', headerName: 'Home address', width: 130 },
    { field: 'role', headerName: 'Authority', width: 130 },
    { field: 'active', headerName: 'Active status', width: 130 },
    {
        field: 'photo', headerName: 'Profile photo', width: 130, renderCell: (params) => (
            <img src={params.value} alt="Profile" style={{ width: 50, height: 50, borderRadius: '50%' }} />
        ),
    }

];

const Alert = React.forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function DataTable() {
    const [rows, setRows] = useState([]);
    const [selectedRows, setSelectedRows] = useState([]);
    const [isDataUpdated, setIsDataUpdated] = useState(false);
    const [open, setOpen] = React.useState(false);
    const [message, setMessage] = React.useState("");

    useEffect(() => {
        getUsers()
            .then((response) => {
                console.log(response)
                const allUsers = response.data.allUsers.map((user) => ({
                    id: user.userId,
                    ...user,
                }));
                setRows(allUsers);
                setIsDataUpdated(false);
            })
            .catch((error) => console.error('Error: ', error));
    }, [isDataUpdated]);

    const handleDeactivate = () => {
        console.log(rows[selectedRows]);
        if (selectedRows.length > 0) {
            const id = rows[selectedRows[0]].userId;
            console.log(id)
            deactivateUser(id).then((response) => {
                console.log(response.data)
                setIsDataUpdated(true);
                setOpen(true);
                setMessage(`Successfully deactivated user profile with ID: ${id}`)
            })
                .catch((error) => console.error(error));
        }
    }

    const handleActivate = () => {
        console.log(rows[selectedRows].userId);
        if (selectedRows.length > 0) {
            const id = rows[selectedRows[0]].userId;
            activateUser(id).then((response) => {
                console.log(response);
                setIsDataUpdated(true);
                setOpen(true);
                setMessage(`Successfully activated user profile with ID: ${id}`)
            })
                .catch((error) => console.error(error));
        }
    }

    const handleDelete = () => {
        console.log(rows[selectedRows]);
        if (selectedRows.length > 0) {
            const id = rows[selectedRows[0]].userId;
            deleteUser(id).then((response) => {
                console.log(response.data)
                setIsDataUpdated(true);
                setOpen(true);
                setMessage(`Successfully deleted user with ID: ${id}`)
            })
                .catch((error) => console.error(error));
        }
    }

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
        setMessage("");
    }

    return (
        <div style={{ height: 500, width: '100%' }}>
            <Table
                rows={rows}
                columns={columns}
                onRowSelectionModelChange={(newRowSelectionModel) => {
                    setSelectedRows(newRowSelectionModel.map((id) => rows.findIndex((row) => row.id === id)));
                }}
                selectedRows={selectedRows}
            />
            <Stack spacing={2} direction="row" style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap' }}>
                <Button variant="contained" sx={{ backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }} onClick={handleActivate}>Activate</Button>
                <Button variant="contained" sx={{ backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }} onClick={handleDeactivate}>Deactivate</Button>
                <Button variant="contained" sx={{ backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }} onClick={handleDelete}>Delete</Button>
            </Stack>
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="success" sx={{ width: '100%', backgroundColor: '#4B8D97' }}>
                    {message}
                </Alert>
            </Snackbar>

        </div>
    );
}