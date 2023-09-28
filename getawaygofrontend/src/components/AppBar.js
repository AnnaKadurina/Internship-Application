import React, { useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import logo from '../images/logo.png';
import { Link } from 'react-router-dom';
import { getUser } from '../api/authorizationApis';
import LoginIcon from '@mui/icons-material/Login';
import { useNavigate } from "react-router-dom";
import ChatIcon from '@mui/icons-material/Chat';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import AddBoxIcon from '@mui/icons-material/AddBox';
import MapsHomeWorkIcon from '@mui/icons-material/MapsHomeWork';
import BarChartIcon from '@mui/icons-material/BarChart';
import LogoutIcon from '@mui/icons-material/Logout';
import GroupIcon from '@mui/icons-material/Group';
import ReviewsIcon from '@mui/icons-material/Reviews';
import EventIcon from '@mui/icons-material/Event';

function ResponsiveAppBar() {
  const [pages, setPages] = useState([{ name: 'All listings', href: 'home' }, { name: 'Register', href: 'register', id: "register" }]);
  const [settings, setSettings] = useState([]);

  const [anchorElNav, setAnchorElNav] = React.useState(null);
  const [anchorElUser, setAnchorElUser] = React.useState(null);
  const [userData, setUserData] = useState(null);

  const navigate = useNavigate();

  const LogoutClick = (param) => {
    if (param === 'LOGOUT') {
      localStorage.clear();
      console.log('User has successfully logged out')
      navigate("/home");
      window.location.reload();
    }
  }

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  React.useEffect(() => {
    const userId = localStorage.getItem('id');
    if (userId) {
      getUser(userId)
        .then((response) => {
          if (response != null) {
            setUserData(response.data);

            if (response.data.role === "ADMIN") {
              setSettings([{ name: <><ManageAccountsIcon sx={{ fontSize: '1rem' }} /> Settings</>, href: 'settings' }, { name: <><GroupIcon sx={{ fontSize: '1rem' }} /> Users</>, href: 'admin/overview/users' }, { name: <><MapsHomeWorkIcon sx={{ fontSize: '1rem' }} /> Properties</>, href: 'admin/overview/properties' }, { name: <><ReviewsIcon sx={{ fontSize: '1rem' }} /> Reviews</>, href: 'admin/overview/reviews' }, { name: <><BarChartIcon sx={{ fontSize: '1rem' }} /> Statistics</>, href: 'admin/statistics' }]);
              setPages([{ name: 'All listings', href: 'home' }]);
            } else if (response.data.role === "HOST") {
              setSettings([{ name: <><ManageAccountsIcon sx={{ fontSize: '1rem' }} /> Settings</>, href: 'settings' }, { name: <><AddBoxIcon sx={{ fontSize: '1rem' }} /> Add new listing</>, href: 'host/create' }, { name: <><MapsHomeWorkIcon sx={{ fontSize: '1rem' }} /> Manage my properties</>, href: 'host/my/properties' }, { name: <><ChatIcon sx={{ fontSize: '1rem' }} /> My chats</>, href: 'chatroom' }, { name: <><BarChartIcon sx={{ fontSize: '1rem' }} /> Statistics</>, href: 'host/statistics' }]);
              setPages([{ name: 'All listings', href: 'home' }]);
            } else if (response.data.role === "GUEST") {
              setSettings([{ name: <><ManageAccountsIcon sx={{ fontSize: '1rem' }} /> Settings</>, href: 'settings', id: 'settings' }, { name: <><EventIcon sx={{ fontSize: '1rem' }} /> My bookings</>, href: 'guest/bookings' }, { name: <><ChatIcon sx={{ fontSize: '1rem' }} /> My chats</>, href: 'chatroom' }]);
              setPages([{ name: 'All listings', href: 'home' }, { name: 'GetawayGo your home', href: 'guest/become/host', id: 'becomeHostBtn' }]);
            }
          }
        })
        .catch((e) => {
          console.error('Error:', e);
        });
    } else {
    }
  }, []);



  return (
    <AppBar position="static" sx={{ backgroundColor: '#C3ACBA' }}>
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          <Link to="/home" aria-label="Home">
            <img src={logo} alt="" style={{ width: 140, height: 100 }} sx={{ display: { xs: 'none', md: 'flex' }, mr: 1 }} />
          </Link>
          <Typography
            variant="h6"
            noWrap
            component="a"
            href="/"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
          </Typography>

          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none' },
              }}
            >
              {pages.map((page) => (
                <MenuItem key={page.name} onClick={handleCloseNavMenu}>
                  <Typography textAlign="center" href="/" >
                    <Button variant="contained" className={page.id} id={page.id} key={page.name} href={"/" + page.href}>
                      {page.name}
                    </Button>
                  </Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>

          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
            {pages.map((page) => (
              <Button
                key={page.name}
                onClick={handleCloseNavMenu}
                sx={{ my: 2, color: 'white', display: 'block' }}
                href={"/" + page.href}
              >
                {page.name}
              </Button>
            ))}
          </Box>

          {userData?.username != null ?
            <Box sx={{ flexGrow: 0 }}>
              <Tooltip title="Open settings">
                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }} id="profile">
                  <Avatar alt="Profile picture" src={userData.photo} sx={{ width: 60, height: 60, }} />
                </IconButton>
              </Tooltip>
              <Menu sx={{ mt: '45px' }} id="menu-appbar" anchorEl={anchorElUser} anchorOrigin={{ vertical: 'top', horizontal: 'right', }} keepMounted transformOrigin={{ vertical: 'top', horizontal: 'right', }} open={Boolean(anchorElUser)} onClose={handleCloseUserMenu}>
                {settings.map((setting) => (
                  <MenuItem key={setting.name}>
                    <Link to={setting.href} style={{ textDecoration: 'none' }} >
                      <Typography variant="body1" sx={{ color: 'black' }}>{setting.name}</Typography>
                    </Link>
                  </MenuItem>
                ))}
                <MenuItem onClick={() => LogoutClick('LOGOUT')}>
                  <LogoutIcon />
                  <Typography variant="body1">Logout</Typography>
                </MenuItem>
              </Menu>

            </Box>
            : <Button variant="contained" sx={{ backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }} href={"/login"}>Login<LoginIcon /></Button>
          }
        </Toolbar>
      </Container>
    </AppBar>
  );
}
export default ResponsiveAppBar;