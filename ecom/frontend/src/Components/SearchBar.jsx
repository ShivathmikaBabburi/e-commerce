import React from "react";
import { FaSearch } from "react-icons/fa";
import Wrapper from "../css/search";

const SearchBar = () => {
  return (
    <Wrapper>
      <div className="cartDiv">
        <input className="searchInput" type="text" placeholder="search" />
        <FaSearch />
      </div>
    </Wrapper>
  );
};

export default SearchBar;
