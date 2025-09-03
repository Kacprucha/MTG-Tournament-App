// app/components/admin/AdminDashboardClient.tsx
"use client";

import React, { useState, useEffect, useCallback } from 'react';
import { useTournament } from '@/context/TournamentContext';
import { useSession } from 'next-auth/react';
import axios, { AxiosError } from 'axios';
import { Spin, Alert } from 'antd';

import { TournamentDetails, MatchSummary } from '@/types/tournament';
import TournamentControlPanel from './TournamentControlPanel';
import MatchList from './MatchList';

interface DashboardData {
  tournamentInfo: {
    id: number;
    currentRound: number;
    totalRounds: number;
    completedGames: number;
    status: string;
  };
  matches: MatchSummary[];
}

const AdminDashboardClient = () => {
  const { tournamentId } = useTournament();
  const { data: session } = useSession();

  const [dashboardData, setDashboardData] = useState<DashboardData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const isAdmin = session?.user?.roles?.includes("ADMIN");

  const fetchData = useCallback(async () => {
    if (!tournamentId || !session?.accessToken) {
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      const apiClient = axios.create({
        baseURL: 'http://localhost:8080',
        headers: { Authorization: `Bearer ${session.accessToken}` },
      });

      const response = await apiClient.get<TournamentDetails>(`/tournaments/${tournamentId}`);
      const tournament = response.data;

      const sortedMatches = tournament.matches.sort((a, b) => {
        if (a.round < b.round) return -1;
        if (a.round > b.round) return 1;
        
        const playerA = a.participantUsernames[0] || '';
        const playerB = b.participantUsernames[0] || '';
        return playerA.localeCompare(playerB);
      });

      const completedMatches = tournament.matches.filter(m => m.status === 'COMPLETED');
      const highestCompletedRound = Math.max(0, ...completedMatches.map(m => m.round));
      const currentRound = highestCompletedRound + 1;

      // Przetwarzamy dane na format oczekiwany przez komponenty
      const processedData: DashboardData = {
        tournamentInfo: {
          id: tournament.id,
          currentRound: currentRound,
          totalRounds: (tournament.participantUsernames?.length || 0) - 1,
          completedGames: tournament.matches.filter(m => m.status === 'COMPLETED').length,
          status: tournament.status,
        },
        matches: sortedMatches,
      };

      setDashboardData(processedData);
      setError(null);
    } catch (err: unknown) {
      if (err instanceof AxiosError) {
        setError(err.response?.data?.message || "Błąd pobierania danych.");
      } else {
        setError("Wystąpił nieoczekiwany błąd.");
      }
    } finally {
      setLoading(false);
    }
  }, [tournamentId, session]);

  useEffect(() => {
    fetchData();
  }, [fetchData]); 

  const handleNewPairing = async () => {
    if (!tournamentId) return;
    try {
      await axios.post(
        `http://localhost:8080/api/tournaments/${tournamentId}/pairings`, 
        {},
        { headers: { Authorization: `Bearer ${session?.accessToken}` } }
      );
      fetchData(); 
    } catch (err) {
      console.error("Błąd generowania nowej rundy:", err);
    }
  };


  if (!isAdmin) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Brak uprawnień" description="Tylko administratorzy mogą tworzyć nowe turnieje." type="error" showIcon className="mt-4" />
      </div>
    );
  }

  if (!tournamentId) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Nie wybrano aktywnego turnieju" type="info" />
      </div>
    );

  }
  if (loading) {
    return <div className="flex justify-center items-center h-screen"><Spin size="large" /></div>;
  }
  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Błąd" description={error} type="error" showIcon />
      </div>
    );
  }
  if (!dashboardData) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Brak danych do wyświetlenia." type="warning" />
      </div>
    );
  }

  return (
    <div className="min-h-screen p-4 sm:p-8 text-white">
      <div className="container mx-auto flex flex-col lg:flex-row gap-8">
        {/* Lewa kolumna - panel kontrolny */}
        <div className="w-full lg:w-1/4">
          <TournamentControlPanel
            // Przekazujemy przetworzone dane
            {...dashboardData.tournamentInfo}
            onNewPairingClick={handleNewPairing}
          />
        </div>

        {/* Prawa, szersza kolumna - lista meczów */}
        <div className="w-full lg:w-3/4">
          <MatchList matches={dashboardData.matches} />
        </div>
      </div>
    </div>
  );
};

export default AdminDashboardClient;