import axios from 'axios';

export const validatePhone = async phone => {
  try {
    const theUser = await axios.get(`api/profiles/${'+234' + phone.slice(phone.length - 10)}`);
    const { firstName, lastName } = theUser.data.user;
    return `${firstName} ${lastName}`;
    // setUser({...user, phone, details: `${firstName} ${lastName}`});
  } catch (error) {
    return false;
    // setUser({...user, phone, details:'User not found!'});
  }
};
