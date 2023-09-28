import React, { useState, useEffect } from 'react';
import { getAllProperties, deleteProperty } from '../api/propertyApis';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Table from './table.js';
import MuiAlert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';

const columns = [
    { field: 'propertyId', headerName: 'ID', width: 70 },
    { field: 'name', headerName: 'Name', width: 130 },
    { field: 'address', headerName: 'Street address', width: 130 },
    { field: 'town', headerName: 'Town', width: 180 },
    { field: 'country', headerName: 'Country', width: 160, },
    { field: 'userId', headerName: 'User ID', width: 130 }
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
        getAllProperties()
            .then((response) => {
                const allProperties = response.data.allProperties.map((property) => ({
                    id: property.propertyId,
                    ...property,
                }));
                setRows(allProperties);
                setIsDataUpdated(false);
            })
            .catch((error) => console.error('Error: ', error));
    }, [isDataUpdated]);


    const handleDelete = () => {
        console.log(rows[selectedRows]);
        if (selectedRows.length > 0) {
            const id = rows[selectedRows[0]].propertyId;
            deleteProperty(id).then((response) => {
                console.log(response.data)
                setIsDataUpdated(true);
                setOpen(true);
                setMessage(`Successfully deleted property with ID: ${id}`)
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
        <div>
            <Table
                columns={columns}
                rows={rows}
                onRowSelectionModelChange={(newRowSelectionModel) => {
                    setSelectedRows(
                        newRowSelectionModel.map((id) =>
                            rows.findIndex((row) => row.id === id)
                        )
                    );
                }}
                selectedRows={selectedRows}
            />
            <Stack spacing={2} direction="row" style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap' }}>
                <Button variant="contained" sx={{ backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA' }, }} onClick={handleDelete}>Delete</Button>
            </Stack>
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="success" sx={{ width: '100%', backgroundColor: '#4B8D97' }}>
                    {message}
                </Alert>
            </Snackbar>
        </div>
    );

}