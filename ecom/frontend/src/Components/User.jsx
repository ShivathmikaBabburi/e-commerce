import React from "react";
import { PiUserFill } from "react-icons/pi";
import Wrapper from "../css/user";
import { useLoaderData } from "react-router-dom";
import axiosInstance from "../axiosConfig";
export const Loader = async () => {
  try {
    const user = await axiosInstance.get("/api/auth/user");
    console.log(user);

    return user;
  } catch (error) {
    return error;
  }
};
const User = () => {
  const user = useLoaderData();
  console.log(user.data);
  return (
    <Wrapper>
      <button>
        <PiUserFill />
        {user.data.username}
      </button>
    </Wrapper>
  );
};

export default User;
