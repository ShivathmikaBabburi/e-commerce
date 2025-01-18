import React from "react";
import loginImg from "../undraw_secure_login_pdn4.svg";
import { redirect, Form } from "react-router-dom";
import Wrapper from "../css/login";
import axiosInstance from "../axiosConfig";

export const action = async ({ request }) => {
  const formData = await request.formData();
  const data = Object.fromEntries(formData);

  try {
    console.log(data);

    const response = await axiosInstance.post("/api/auth/signin", data, {
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.status !== 200) {
      throw new Error("Failed to sign in");
    }

    return redirect("/");
  } catch (error) {
    console.log("Error during login:", error.message);
    return { error: error.message };
  }
};

const Login = () => {
  return (
    <div>
      <Wrapper>
        <Form method="POST" className="login-form">
          <div className="form-group">
            <label htmlFor="username">Username:</label>
            <input
              className="form-input"
              type="text"
              name="username"
              id="username"
              aria-label="username"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password:</label>
            <input
              type="password"
              name="password"
              id="password"
              aria-label="password"
              className="form-input"
              required
            />
          </div>
          <button type="submit" className="login-button">
            Submit
          </button>
        </Form>
        <img src={loginImg} alt="Login" className="login-image" />
      </Wrapper>
    </div>
  );
};

export default Login;
