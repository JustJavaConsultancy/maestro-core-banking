import axios from 'axios';

const getScemes = async phone => {
  const theSchemes = await axios.get(`/api/schemes/${'234' + phone.slice(phone.length - 10)}`);
  //  const { firstName, lastName } = theUser.data.user;
  return theSchemes;
  // setUser({...user, phone, details: `${firstName} ${lastName}`});
};

export default getScemes;
