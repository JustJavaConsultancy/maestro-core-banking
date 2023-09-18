import './header.scss';
import React, { useEffect, useState } from 'react';
import { Navbar, Nav } from 'react-bootstrap';
import { NavLink, Redirect } from 'react-router-dom';
// import logo from 'content/images/logo.png';
import { withCookies } from 'react-cookie';
import { AiOutlineMenu } from 'react-icons/ai';
import { useHistory } from 'react-router-dom';
//
function header(props: any): JSX.Element {
  const [state, setState] = useState({
    isAuthenticated: false,
    user: {
      user: {
        firstName: '',
      },
    },
  });
  const history = useHistory();

  useEffect(() => {
    setState({ ...state, isAuthenticated: props.isAuthenticated });
    console.error('header Thread');
  }, [props.auth]);

  const logout = () => {
    const { cookies } = props;
    cookies.remove('token', { path: '/' });
    // const goto:Location = Location(new "/logout")
    history.push('/logout');
    // window.location = "/logout"
    // props.logout();
  };

  return (
    <Navbar className="navContainer" expand="lg">
      {window.innerWidth < 995 && state.isAuthenticated ? (
        <AiOutlineMenu onClick={props.toggleMenu} size={40} color="black" />
      ) : (
        <NavLink to="/" style={{ marginLeft: '-1rem' }}>
          <img style={{ width: '120px' }} src="content/images/pouchii.svg" />
        </NavLink>
      )}
      {!state.isAuthenticated && <Navbar.Toggle aria-controls="basic-navbar-nav" />}
      <Navbar.Collapse id="responsive-navbar-nav">
        <Nav className="mr-auto"></Nav>
        {!state.isAuthenticated && (
          <Nav style={{ background: window.innerWidth < 995 ? '#fff' : 'transaprent', padding: '10px' }}>
            <NavLink className="navLinks" to="/">
              About Us
            </NavLink>
            <NavLink className="navLinks" to="/">
              FAQs
            </NavLink>
            <NavLink className="navLinks" to="/login">
              Login
            </NavLink>
            <button onClick={() => history.push('/registeration-step-one')} className="round-padding">
              Sign Up
            </button>
          </Nav>
        )}
      </Navbar.Collapse>
      <>
        {state.isAuthenticated && (
          <div>
            <NavLink className="navLinks" to="/">
              {state.user.user && state.user.user.firstName}
            </NavLink>
            <button onClick={logout} className="round-padding">
              Logout
            </button>
          </div>
        )}
      </>
    </Navbar>
  );
}

export default withCookies(header);
