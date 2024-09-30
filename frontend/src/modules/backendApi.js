import axios from "axios";

const fetchCurrentSeason = async () => {
  const response = await axios.get(
    `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}/seasons/current`
  );
  return response.data;
}

const fetchPreviousSeason = async () => {
  const response = await axios.get(
    `${process.env.DLAPP_PROTOCOL}://${window.location.hostname}${process.env.DLAPP_API_PORT}${process.env.DLAPP_API_PATH}/seasons/previous`
  );
  return response.data;
}

export { fetchCurrentSeason, fetchPreviousSeason };
