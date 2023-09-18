import React, { useEffect, useState } from 'react';
import axios from 'axios';

import './showUserImg.scss';

function ShowUserImg({ imageUrl }) {
  const [image, setImage] = useState('');

  const fetchUserDocs = () => {
    const adminToken = JSON.parse(sessionStorage.getItem('jhi-authenticationToken'));

    if (imageUrl) {
      fetch(`https://wallet.remita.net/api/images/${imageUrl}`, {
        //   fetch(`https://walletdemo.remita.net/api/images/${imageUrl}`, {
        //   fetch(`/api/images/${imageUrl}`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${adminToken}`,
        },
      })
        .then(response => {
          response
            .blob()
            .then(blobResponse => {
              const dataBlob = blobResponse;
              // eslint-disable-next-line no-console
              console.log(dataBlob);

              if (dataBlob.size) {
                const reader = new FileReader();
                reader.readAsDataURL(dataBlob);
                reader.onloadend = () => {
                  setImage(reader.result as string);
                };
              } else {
                setImage('');
              }

              // const urlCreator = window.URL || window.webkitURL;
              // window.scroll({ behavior: 'smooth', top: 0 });
              // setImage(urlCreator.createObjectURL(dataBlob));
            })
            .catch(err => {
              alert('Error getting image');
            });
        })
        .catch(err => console.error(err));
    } else {
      setImage('');
    }
  };

  useEffect(() => fetchUserDocs(), []);

  return <div>{image ? <img src={image} alt="user image" width="200px" height="200px" /> : <small>No image found!</small>}</div>;
}

export default ShowUserImg;
