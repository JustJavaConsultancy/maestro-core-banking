import { height } from '@fortawesome/free-solid-svg-icons/faSort';
import React from 'react';
//  import p1 from 'src/main/webapp/content/images/p1.png';// '../../../../content/images/p1.png';
//  import p2 from 'src/main/webapp/content/images/p2.png';
import ThreeDot from './ThreeDots';

export const Card1: any = (props: any) => {
  return (
    <div style={{ width: '100%' }}>
      <div className="cardTitle">
        <div style={{ flex: 0.7, display: 'flex' }}>
          <p style={{ marginTop: '5px', fontWeight: 'bold' }}>{props.headtitle}</p>
        </div>
        <div style={{ flex: 0.3, display: 'flex', justifyContent: 'flex-end' }}>
          <ThreeDot />
        </div>
      </div>
      <div style={{ backgroundColor: props.bg, boxShadow: props.shadowColor }} className={`transCard ${props.className}`}>
        <img className="f1" src="content/images/p1.png" />
        <img className="f2" src="content/images/p2.png" />
        <div className="center">
          <div style={{ backgroundColor: 'transaparent' }} className="row">
            <div style={{ marginRight: 15, backgroundColor: 'transaparent' }}>
              <img src={props.src} />
            </div>
            <div className="center">
              <h2 style={{ color: '#fff', fontSize: '32px' }}> {props.title} </h2>
              <p style={{ color: '#fff', fontSize: '12px', lineHeight: '5px' }}> {props.caption} </p>
            </div>
          </div>
          <div>
            <button onClick={props.click} style={{ color: props.bcolor }} className="fancy-button">
              {' '}
              {props.button}{' '}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
