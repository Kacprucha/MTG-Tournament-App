"use client";

import ControlButtons from "@/app/components/match/ControlButtons";
import GameStatus from "@/app/components/match/GameStatus";
import LifeCounter from "@/app/components/match/LifeCounter";
import MatchInfo from "@/app/components/match/MatchInfo";
import StatsPanel from "@/app/components/match/StatsPanel";
import Timer from "@/app/components/match/Timer";
import WinnerSelection from "@/app/components/match/WinnerSelectionProps";
import OuterContainer from "@/app/components/OuterContainer";

const containerStyles: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  gap: '40px',
  padding: '50px',
  backgroundColor: '#293132', // Ciemne tło, aby komponenty były widoczne
  minHeight: '100vh',
};

const playerContainerStyles: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    gap: '20px'
}

export default function MatchPage({ params }: { params: { id: string } }) {
  const matchId = params.id;

  // Tymczasowe dane - docelowo pobierane z API
  const mockMatch = {
    id: matchId,
    round: 3,
    totalRounds: 10,
    opponent: "Kacper K",
    tableNumber: 5,
    participants: ["Mateusz H", "Kacper K"],
    status: "przed rozpoczeciem",
  };

  return (
    <div className="min-h-screen w-full bg-[#293132] relative">
      <OuterContainer>
        <div className="flex flex-1 gap-4 p-4">
          {/* Lewa sekcja */}
          <div className="flex flex-col gap-4 w-1/4">
            <MatchInfo
              round={mockMatch.round}
              totalRounds={mockMatch.totalRounds}
              opponent={mockMatch.opponent}
              tableNumber={mockMatch.tableNumber}
            />
            <ControlButtons />
          </div>

          {/* Środek */}
          <div className="flex-1">
            <StatsPanel />
          </div>

          {/* Prawa sekcja */}
          <div className="flex flex-col gap-4 w-1/4">
            <Timer />
            <LifeCounter />
            <WinnerSelection participants={mockMatch.participants} />
            <GameStatus status={mockMatch.status} />
          </div>
        </div>
      </OuterContainer>
    </div>
  );
}
