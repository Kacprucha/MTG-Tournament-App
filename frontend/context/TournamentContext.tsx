"use client";

import React, { createContext, useState, useContext, ReactNode, useEffect } from 'react';

interface TournamentContextType {
  tournamentId: number | null;
  tournamentName: string | null;
  setCurrentTournament: (id: number | null, name: string | null) => void;
}

const TournamentContext = createContext<TournamentContextType | undefined>(undefined);

export const TournamentProvider = ({ children }: { children: ReactNode }) => {
  const [tournamentId, setTournamentId] = useState<number | null>(null);
  const [tournamentName, setTournamentName] = useState<string | null>(null);

  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true); // Oznaczamy, że komponent jest już w przeglądarce
    try {
      const savedId = localStorage.getItem('tournamentId');
      const savedName = localStorage.getItem('tournamentName');
      if (savedId) {
        setTournamentId(JSON.parse(savedId));
      }
      if (savedName) {
        setTournamentName(savedName);
      }
    } catch (error) {
      console.error("Failed to parse tournament data from localStorage", error);
    }
  }, []); // Pusta tablica zależności = uruchom tylko raz w przeglądarce

  useEffect(() => {
    if (isMounted) {
      if (tournamentId !== null && tournamentName !== null) {
        localStorage.setItem('tournamentId', JSON.stringify(tournamentId));
        localStorage.setItem('tournamentName', tournamentName);
      } else {
        localStorage.removeItem('tournamentId');
        localStorage.removeItem('tournamentName');
      }
    }
  }, [tournamentId, tournamentName, isMounted]);

  const setCurrentTournament = (id: number | null, name: string | null) => {
    setTournamentId(id);
    setTournamentName(name);
  };

  const value = { tournamentId, tournamentName, setCurrentTournament };

  return (
    <TournamentContext.Provider value={value}>
      {children}
    </TournamentContext.Provider>
  );
};

export const useTournament = () => {
  const context = useContext(TournamentContext);
  if (context === undefined) {
    throw new Error('useTournament must be used within a TournamentProvider');
  }
  return context;
};