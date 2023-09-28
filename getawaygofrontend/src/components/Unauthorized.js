import React from 'react'
import Unauthorized from '../images/401.jpg';


export default function PageNotFound() {
    return (
        <div>
            <img src={Unauthorized} alt="" style={{ width: 600, height: 600, justifyContent: 'center', flexWrap: 'wrap', marginTop: 10 }} />
        </div>
    )
}
