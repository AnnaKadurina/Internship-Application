import * as React from 'react';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import logo from '../images/logo.png';
import { Link } from 'react-router-dom';
import EmailIcon from '@mui/icons-material/Email';

export default function StickyFooter() {
  return (
    <Container style={{ marginTop: 200, display: "flex", justifyContent: "space-between", marginLeft: "auto", marginRight: "auto" }}>
      <div>
        <Link to="/home" aria-label="Home">
          <img src={logo} alt="" style={{ width: 170, height: 130 }} sx={{ display: { xs: 'none', md: 'flex' }, marginRight: 1 }} />
        </Link>
      </div>
      <div style={{ flex: 1, textAlign: 'center' }}>
        <Typography variant="body1">
          GetawayGo © 2023
        </Typography>
        <Typography variant="body1">
          Contact us:
        </Typography>
        <Typography variant="body1">
          a.kadurina@student.fontys.nl
        </Typography>
        <Typography variant="body1">
          Send email:
        </Typography>
        <Typography variant="body1">
          <a href="mailto:a.kadurina@student.fontys.nl" aria-label="email">
            <EmailIcon sx={{ color: '#4B8D97' }} />
          </a>
        </Typography>
      </div>
      <div style={{ textAlign: 'center' }}>
        <Typography variant="body1">
          Privacy policy
        </Typography>
        <Typography variant="body1">
          Terms of use
        </Typography>
        <Typography variant="body1">
          Customer service
        </Typography>
      </div>
    </Container>


  );
}