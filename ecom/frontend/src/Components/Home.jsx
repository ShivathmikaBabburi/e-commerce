import React from "react";
import { useLoaderData } from "react-router-dom";
import axiosInstance from "../axiosConfig";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
export const loader = async () => {
  try {
    const categories = await axiosInstance.get("/api/public/categories");
    console.log(categories.data);

    return categories.data;
  } catch (error) {
    return error;
  }
};
const Home = () => {
  const categories = useLoaderData();
  console.log(categories.content);

  return (
    <div>
      {categories.content.map((object) => {
        return (
          <div>
            {object.categoryName}
            <Link>get products</Link>
          </div>
        );
      })}
    </div>
  );
};

export default Home;
