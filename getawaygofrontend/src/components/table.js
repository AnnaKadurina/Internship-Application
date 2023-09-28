import React from 'react';
import { DataGrid } from '@mui/x-data-grid';

const Table = ({ columns, rows, onRowSelectionModelChange, selectedRows }) => {
    return (
        <div style={{ height: 500, width: '100%' }}>
            <DataGrid
                rows={rows}
                columns={columns}
                pageSize={5}
                rowsPerPageOptions={[5]}
                onRowSelectionModelChange={onRowSelectionModelChange}
                selectedRows={selectedRows}
            />
        </div>
    );
};

export default Table;