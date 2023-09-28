import React from 'react'
import Missing from '../images/missin.gif';


export default function PageNotFound() {
  return (
    <div>
      <img src={Missing} alt="" style={{ width: 600, height:600, justifyContent: 'center', flexWrap: 'wrap', marginTop: 10  }} />
    </div>
  )
}
