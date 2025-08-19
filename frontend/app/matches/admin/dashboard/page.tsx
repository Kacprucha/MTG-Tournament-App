// app/admin/dashboard/page.tsx (przykładowa ścieżka)
"use client";

import MatchList from '@/app/components/match/admin/MatchList';
import TournamentControlPanel from '@/app/components/match/admin/TournamentControlPanel';
import Alert from 'antd/es/alert/Alert';
import { useSession } from 'next-auth/react';
import React from 'react';

// Mockowe dane, które normalnie przyszłyby z API
const MOCK_DASHBOARD_DATA = {
  tournamentInfo: {
    currentRound: 3,
    totalRounds: 10,
    completedGames: 8,
  },
  matches: [
    { id: 1, player1: 'Kacper', player2: 'Homik', round: 3, status: 'Błąd weryfikacji' },
    { id: 2, player1: 'Homik', player2: 'Kacper', round: 3, status: 'Błąd weryfikacji' },
    { id: 3, player1: 'Mateusz', player2: 'Anna', round: 3, status: 'Zakończony' },
    // ... więcej meczów
  ],
};

const AdminDashboardPage = () => {
  const { data: session } = useSession();
  
  const isAdmin = session?.user?.roles?.includes("ADMIN");
  
  if (!isAdmin) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Brak uprawnień" description="Tylko administratorzy mogą tworzyć nowe turnieje." type="error" showIcon className="mt-4" />
      </div>
    );
  }

  const handleNewPairing = () => {
    console.log("Inicjowanie nowego losowania...");
    // Tutaj logika wywołania API do stworzenia nowej rundy
  };

  return (
    <div className="min-h-screen p-4 sm:p-8 text-white">
      <div className="container mx-auto flex flex-col lg:flex-row gap-8">
        {/* Lewa kolumna - panel kontrolny */}
        <div className="w-full lg:w-1/4">
          <TournamentControlPanel 
            {...MOCK_DASHBOARD_DATA.tournamentInfo}
            onNewPairingClick={handleNewPairing}
          />
        </div>

        {/* Prawa, szersza kolumna - lista meczów */}
        <div className="w-full lg:w-3/4">
          <MatchList matches={MOCK_DASHBOARD_DATA.matches} />
        </div>
      </div>
    </div>
  );
};

export default AdminDashboardPage;