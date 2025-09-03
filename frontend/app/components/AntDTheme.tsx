"use client";

import React from 'react';
import { ConfigProvider, theme, type ThemeConfig } from 'antd';

const darkTheme: ThemeConfig = {
  algorithm: theme.darkAlgorithm,
  
  token: {
    // Główne kolory
    colorPrimary: '#22d3ee', // Kolor główny
    colorBgContainer: '#293132', // Tło kontenerów
    colorText: 'rgba(255, 255, 255, 0.85)', // Kolor głównego tekstu
    colorBorderSecondary: '#22d3ee', // Kolor ramek wewnątrz tabeli
  },

  components: {
    Table: {
      headerBg: '#22d3ee',           // Tło nagłówka tabeli
      headerColor: '#474044',        // Kolor tekstu w nagłówku
      borderColor: '#22d3ee',        // Kolor ramek
      headerBorderRadius: 0,        // Ostre rogi nagłówka
      rowHoverBg: '#293132',        // Tło wiersza przy najechaniu
    },
  },
};

const AntDTheme = ({ children }: { children: React.ReactNode }) => {
  return (
    <ConfigProvider theme={darkTheme}>
      {children}
    </ConfigProvider>
  );
};

export default AntDTheme;