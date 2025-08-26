"use client";

import { useEffect } from 'react';
import { signOut } from 'next-auth/react';
import { Spin } from 'antd'; 

const LogoutPage = () => {
  useEffect(() => {
    signOut({ redirect: false }).then(() => {
      window.location.assign("/");
    });
  }, []);

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
      <h1>Wylogowywanie ... </h1>
      <Spin size="large"/>
    </div>
  );
};

export default LogoutPage;