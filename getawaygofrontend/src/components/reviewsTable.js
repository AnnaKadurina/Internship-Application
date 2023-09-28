import React, { useState, useEffect } from 'react';
import { getReviews, deleteReview } from '../api/reviewApis';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Table from './table.js';
import Rating from '@mui/material/Rating';
import MuiAlert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';

const columns = [
    { field: 'reviewId', headerName: 'ID', width: 70 },
    { field: 'text', headerName: 'Text', width: 200 },
    {
        field: 'date', headerName: 'Date of posting', width: 200, valueFormatter: (params) => {
            const date = new Date(params.value);
            return date.toLocaleString();
        }
    },
    { field: 'propertyId', headerName: 'Property ID', width: 130 },
    { field: 'userId', headerName: 'User ID', width: 130, },
    {
        field: 'rating',
        headerName: 'Rating',
        width: 300,
        renderCell: (params) => (
            <Rating name="read-only" value={params.value} max={10} readOnly />
        ),
    },
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
        getReviews()
            .then((response) => {
                console.log(response)
                const allReviews = response.data.allReviews.map((review) => ({
                    id: review.reviewId,
                    ...review,
                }));
                setRows(allReviews);
                setIsDataUpdated(false);
            })
            .catch((error) => console.error('Error: ', error));
    }, [isDataUpdated]);


    const handleDelete = () => {
        console.log(rows[selectedRows]);
        if (selectedRows.length > 0) {
            const id = rows[selectedRows[0]].reviewId;
            deleteReview(id).then((response) => {
                console.log(response.data)
                setIsDataUpdated(true);
                setOpen(true);
                setMessage(`Successfully deleted review with ID: ${id}`)
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