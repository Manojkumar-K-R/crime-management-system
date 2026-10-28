import api from "./api";

export const searchComplaints = async (params = {}) => {
  const response = await api.get("/complaints/search", {
    params,
  });

  return response.data;
};