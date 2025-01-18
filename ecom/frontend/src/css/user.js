import styled from "styled-components";

const Wrapper = styled.nav`
  width: 6rem;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;

  border-radius: 8px;

  button {
    display: flex;
    align-items: center;
    background-color: #000;
    color: #f19103;
    border: none;
    border-radius: 4px;
    padding: 0.5rem 1rem;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  button:hover {
    background-color: #fff;
  }

  svg {
    margin-right: 0.5rem;
    font-size: 1.5rem;
  }
`;

export default Wrapper;
