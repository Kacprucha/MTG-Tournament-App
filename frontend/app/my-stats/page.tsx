// app/tournaments/[id]/results/page.tsx
"use client";

import React, { useState, useEffect } from 'react';
import { Spin, Alert } from 'antd';
import TournamentSummaryCard from '../components/playerStats/TournamentSummaryCard';
import PlayerStatsList from '../components/playerStats/PlayerStatsList';

// --- Mockowane Dane (zgodnie z Twoją specyfikacją) ---
const MOCK_TOURNAMENT_DETAILS = {
  id: "1",
  name: "Turniej 2", 
  type: "Sealed",
  addon: "dupa",
  date: "02.08.2025",
  participants: ["testuser", "Dupa 123", "Gracz 2"],
  scoreboard: [
    { name: "Dupa 123", points: 1237, achievements: { "1": 3, "2": 5, "3": 0, "4": 1, "5": 8 } },
    { name: "Gracz 2", points: 1180, achievements: { "1": 1, "2": 2, "3": 4, "4": 0, "5": 2 } },
  ],
};

const MOCK_ACHIEVEMENTS_LIST = [
  { id: 1, name: "Ilość życia zdobytego w danej grze" },
  { id: 2, name: "Ilość stworzonych tokenów w danej grze" },
  { id: 3, name: "Ilość poświęconych jednostek w danej grze" },
  { id: 4, name: "Największa ilość jednostek na bordzie w trakcie gry" },
  { id: 5, name: "Największa ilość kart na ręce w trakcie gry" },
];

const MOCK_PLAYED_MATCHES = [
  { matchId: 101, result: "Win" },
  { matchId: 102, result: "Win" },
  { matchId: 103, result: "Loss" },
];

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
  const [summaryData, setSummaryData] = useState<SummaryData | null>(null);
  const [detailedStats, setDetailedStats] = useState<DetailedStat[] | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const currentPlayerName = "Dupa 123";

  useEffect(() => {
    const fetchDataAndProcess = async () => {
      try {
        setLoading(true);
        const tournamentData = await new Promise<typeof MOCK_TOURNAMENT_DETAILS>(resolve => 
          setTimeout(() => resolve(MOCK_TOURNAMENT_DETAILS), 500)
        );
        const achievementsList = await new Promise<typeof MOCK_ACHIEVEMENTS_LIST>(resolve => 
          setTimeout(() => resolve(MOCK_ACHIEVEMENTS_LIST), 500)
        );
        const playedMatchesData = await new Promise<typeof MOCK_PLAYED_MATCHES>(resolve => 
          setTimeout(() => resolve(MOCK_PLAYED_MATCHES), 500)
        );

        // --- Logika Przetwarzania Danych ---
        
        const playerBoard = tournamentData.scoreboard.find(
          (player) => player.name === currentPlayerName
        );

        if (!playerBoard) {
          throw new Error(`Nie znaleziono danych dla gracza: ${currentPlayerName}`);
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

      } catch (err: any) {
        setError(err.message || "Wystąpił nieoczekiwany błąd.");
      } finally {
        setLoading(false);
      }
    };

    fetchDataAndProcess();
  }, [currentPlayerName]); 

  if (loading) {
    return <div className="flex justify-center items-center h-screen"><Spin size="large" /></div>;
  }
  if (error) {
    return <Alert message="Błąd" description={error} type="error" showIcon />;
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