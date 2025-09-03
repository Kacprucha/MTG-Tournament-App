// app/tournaments/[id]/results/page.tsx
"use client";

import React, { useState, useEffect } from 'react';
import { Spin, Alert } from 'antd';
import TournamentSummaryCard from '../components/playerStats/TournamentSummaryCard';
import PlayerStatsList from '../components/playerStats/PlayerStatsList';
import { useTournament } from '@/context/TournamentContext';
import { useSession } from 'next-auth/react';
import { MatchSummary, TournamentDetails } from '@/types/tournament';
import axios, { AxiosError } from 'axios';

interface SummaryData {
  tournamentName: string;
  gamesPlayed: number;
  totalPoints: number;
}
interface DetailedStat {
  label: string;
  value: number;
}

const PlayerResultsPage = () => {
  const { tournamentId } = useTournament();
  const { data: session } = useSession();

  const [summaryData, setSummaryData] = useState<SummaryData | null>(null);
  const [detailedStats, setDetailedStats] = useState<DetailedStat[] | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const currentPlayerName = session?.user?.username;

  useEffect(() => {
    if (tournamentId && currentPlayerName && session?.accessToken) {
      const fetchDataAndProcess = async () => {
        try {
          setLoading(true);

          const apiClient = axios.create({
            baseURL: "http://localhost:8080",
            headers: { Authorization: `Bearer ${session.accessToken}` },
          });
          
          const [tournamentResponse, matchesResponse] = await Promise.all([
            apiClient.get<TournamentDetails>(`/tournaments/${tournamentId}`),
            apiClient.get<MatchSummary[]>(`/matches?tournamentId=${tournamentId}&participantUsername=${currentPlayerName}`)
          ]);
          
          const tournamentData = tournamentResponse.data;
          const achievementsList = tournamentData.achievements; 
          const playedMatchesData = matchesResponse.data.filter(m => m.status === 'COMPLETED');
          
          const playerBoard = tournamentData.scoreboard.find(
            (player) => player.username === currentPlayerName
          );

          if (!playerBoard) {
            throw new Error(`Nie znaleziono danych w tabeli wyników dla gracza: ${currentPlayerName}`);
          }

          const summary: SummaryData = {
            tournamentName: tournamentData.name,
            gamesPlayed: playedMatchesData.length,
            totalPoints: playerBoard.points,
          };
          setSummaryData(summary);
          
          const detailed: DetailedStat[] = Object.entries(playerBoard.achievements).map(
            ([achievementId, achievementValue]) => {
              const achievementDefinition = achievementsList.find(
                (ach) => ach.id.toString() === achievementId
              );
              return {
                label: achievementDefinition ? achievementDefinition.name : `Nieznane osiągnięcie #${achievementId}`,
                value: achievementValue,
              };
            }
          );
          setDetailedStats(detailed);
          setError(null);

        } catch (err: unknown) {
          if (err instanceof AxiosError) {
            setError(err.response?.data?.message || "Wystąpił nieoczekiwany błąd podczas pobierania danych.");
          } else {
            setError("Wystąpił nieoczekiwany błąd.");
          }
          console.error(err);
        } finally {
          setLoading(false);
        }
      };

      fetchDataAndProcess();
    } else {
      setLoading(false); 
    }
  }, [tournamentId, currentPlayerName, session]);

  if (loading) {
    return <div className="flex justify-center items-center h-screen"><Spin size="large" /></div>;
  }
  if (error) {
    return (
      <div className="min-h-screen w-full bg-[#293132] flex items-center justify-center text-white">
        <Alert message="Błąd" description={error} type="error" showIcon />
      </div>
    );
  }

  if (!tournamentId || !currentPlayerName) {
    return (
      <div className="min-h-screen w-full bg-[#293132] flex items-center justify-center text-white">
        <Alert message="Brak danych" description="Nie wybrano aktywnego turnieju lub nie jesteś zalogowany." type="warning" showIcon />
      </div>
    );
  }

  if (!summaryData || !detailedStats) {
    return <Alert message="Brak danych do wyświetlenia." type="info" />;
  }
  
  return (
    <div className="bg-[#293132] min-h-screen p-4 sm:p-8 text-white">
      <div className="container mx-auto flex flex-col lg:flex-row gap-8">
        <div className="w-full lg:w-1/3">
          <TournamentSummaryCard {...summaryData} />
        </div>
        <div className="w-full lg:w-2/3">
          <PlayerStatsList stats={detailedStats} />
        </div>
      </div>
    </div>
  );
};

export default PlayerResultsPage;