import React from "react";
import Logo from "./Logo";
import { IoCartOutline } from "react-icons/io5";
import Wrapper from "../css/header";
import SearchBar from "./SearchBar";
import User from "./User";
import { Outlet } from "react-router-dom";
const Header = () => {
  return (
    <div>
      <Wrapper>
        <div className="navbar">
          <Logo />
          <div className="searchDiv">
            <SearchBar />
          </div>
          <div className="userDiv">
            <User />
          </div>
          <div className="cartDiv">
            <IoCartOutline />
          </div>
        </div>
      </Wrapper>
      <Outlet />
    </div>
  );
};

export default Header;
