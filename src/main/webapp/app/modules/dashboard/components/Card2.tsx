import './component.scss';
import React from 'react';
import ThreeDot from './ThreeDots';

export const Card2: any = (props: any) => {
  return (
    <div>
      <div className="cardTitle">
        <div style={{ flex: 0.7, display: 'flex' }}>
          <p style={{ marginTop: '5px', fontWeight: 'bold' }}>{props.headtitle}</p>
        </div>
        <div style={{ flex: 0.3, display: 'flex', justifyContent: 'flex-end' }}>
          <ThreeDot />
        </div>
      </div>
      <div className="card2">
        <img style={{ position: 'absolute', bottom: 0, alignSelf: 'center' }} src="content/images/grad.png" />
        <img style={{ position: 'absolute', bottom: '23px', alignSelf: 'center' }} src="content/images/v.png" />
        <div style={{ position: 'absolute', left: '10px', top: '10px', color: '#fff', fontSize: '11px' }}>{/* last 7 days */}All times</div>
        <div style={{ position: 'absolute', right: '10px', color: '#fff', top: '10px' }}>
          <span>
            {props.count}
            <br />
          </span>{' '}
          <span style={{ fontSize: '11px', display: 'block' }}>customers</span>
          <span style={{ fontSize: '11px', display: 'block' }}></span>
        </div>
        <div style={{ position: 'absolute', alignSelf: 'center', color: '#fff', fontSize: '12px', bottom: '0px' }}>
          <p>
            <b>Busiest day:</b> Estimating..
          </p>
        </div>
      </div>
    </div>
  );
};
