import React from "react";
import logo from "../logo.svg";
import Wrapper from "../css/logo";
const Logo = () => {
  return (
    <Wrapper>
      <img className="logoimg" src={logo} alt="ecom" />
    </Wrapper>
  );
};

export default Logo;
