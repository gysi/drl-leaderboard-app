import axios from "axios";

const fetchCurrentSeason = async () => {
  const response = await axios.get(`${process.env.DLAPP_API_URL}/seasons/current`);
  return response.data;
}

export {fetchCurrentSeason};
