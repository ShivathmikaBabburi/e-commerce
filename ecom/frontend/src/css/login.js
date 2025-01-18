import styled from "styled-components";

const Wrapper = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh; /* Full viewport height */
  padding: 0 20px; /* Optional padding */
  box-sizing: border-box;
  .login-form {
    flex: 1;
    font-size: 2rem;
    max-width: 600px; /* Adjust as needed */
    margin-right: 20px; /* Space between image and form */
  }
  .login-image {
    max-width: 50%; /* Adjust as needed */
    height: auto;
  }

  .form-input {
    height: 2rem;
  }
  .form-group {
    margin-bottom: 15px;
  }

  .login-button {
    padding: 10px 20px;
    background-color: #f19103; /* Button background color */
    color: black;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 2rem;
  }

  .login-button:hover {
    background-color: #0056b3; /* Button hover color */
  }
`;

export default Wrapper;
