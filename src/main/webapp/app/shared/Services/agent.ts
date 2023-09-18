import Axios from 'axios';

const getAgentByStatus = async (status: string) => {
  const superAgents = await Axios.get(`/api/super-agents/status/${status}`);
  return superAgents;
};

const changeAgentByStatus = async (status: string, phoneNumber: string) => {
  const superAgents = await Axios.post(`/api/super-agents/status/${phoneNumber}/${status}`);
  return superAgents;
};

export default {
  getAgentByStatus,
  changeAgentByStatus,
};
