"use client";

import { useEffect } from 'react';
import { useSession } from 'next-auth/react';
import { useTournament } from '@/context/TournamentContext';

export const SessionWatcher = () => {
  const { status } = useSession(); 
  const { tournamentId, setCurrentTournament } = useTournament();

  useEffect(() => {
    if (status === 'unauthenticated' && tournamentId !== null) {
      console.log("Sesja wygasła lub użytkownik się wylogował. Czyszczenie stanu turnieju...");
      setCurrentTournament(null, null, null);
    }
  }, [status, tournamentId, setCurrentTournament]);

  return null;
};