import React from 'react';

export default function(props) {
    return(
        <div>
            <div style = {{height:props.height}} className = 'bar'>
                <div style = {{backgroundColor:'#EE4E4E',borderTopLeftRadius:"10px", borderTopRightRadius:"10px", height: props.outflow}}></div>
                <div style = {{backgroundColor:'#00C49E', borderRadius:"10px", height: props.inflow}} ></div>
            </div>
            <p style = {{fontFamily:'Gilroy-Bold', fontSize:"12px"}}>{props.x}</p>
        </div>
    )
}