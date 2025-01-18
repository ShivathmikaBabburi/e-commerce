import styled from "styled-components";

const Wrapper = styled.div`
  height: 4rem;
  margin-left: auto;
  display: flex;
  align-items: center;
  font-size: 1.5rem; /* Icon size */
  color: #333; /* Icon color */

  .searchInput {
    padding: 0.5rem;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: 4px;
    margin-right: 0.5rem;
  }

  .cartDiv {
    display: flex;
    align-items: center;
  }
`;

export default Wrapper;
