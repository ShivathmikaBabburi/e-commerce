import styled from "styled-components";

const Wrapper = styled.nav`
  height: 4rem;
  display: flex;
  justify-content: space-between;
  align-items: center;

  padding: 0 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Slight shadow for elevation */

  .navbar {
    display: flex;
    align-items: center;
    width: 100%;
  }

  .cartDiv {
    margin-left: auto;
    display: flex;

    align-items: center;
    font-size: 2rem; /* Icon size */
    color: #333; /* Icon color */
    cursor: pointer;
  }
  .searchDiv {
    flex-grow: 1; /* Allows the search bar to take up available space */
    margin: 0 1rem;
  }

  .userDiv {
    margin-left: 25rem;
    display: flex;
    align-items: center;
    margin-right: 1rem;
  }

  .cartDiv:hover {
    color: #007bff; /* Change color on hover */
  }
`;

export default Wrapper;
