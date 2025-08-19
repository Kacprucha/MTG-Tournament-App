"use client";

import ControlButtons from "@/app/components/match/ControlButtons";
import GameStatus from "@/app/components/match/GameStatus";
import LifeCounter from "@/app/components/match/LifeCounter";
import MatchInfo from "@/app/components/match/MatchInfo";
import StatsPanel from "@/app/components/match/StatsPanel";
import Timer from "@/app/components/match/Timer";
import TournamentStageCard from "@/app/components/match/TournamentStageCard";
import WinnerSelection from "@/app/components/match/WinnerSelectionProps";
import OuterContainer from "@/app/components/OuterContainer";
import { useSession } from "next-auth/react";

export default function MatchPage({ params }: { params: { id: string } }) {
  const { data: session } = useSession();
  
  const isAdmin = session?.user?.roles?.includes("ADMIN");
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
    type: "normal",
    bestOf: 3,
    matchWinner: ["Mateusz H", "Kacper K", "Mateusz H"],
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
            {(mockMatch.status === "przed rozpoczeciem" || mockMatch.status === "rozpoczęty" || isAdmin) && (
              <ControlButtons />
            )}
          </div>

          {/* Środek */}
          {mockMatch.type === "normal" && (
            <div className="flex-1">
              <StatsPanel />
            </div>
          )}
          {mockMatch.type !== "normal" && (
            <div className="flex-1">
              <TournamentStageCard 
                stageName={mockMatch.type}
                participants={mockMatch.participants}
                matchWinner={mockMatch.matchWinner}
              />
            </div>
          )}


          {/* Prawa sekcja */}
          <div className="flex flex-col gap-4 w-1/4">
            <Timer />
            <LifeCounter />
            {mockMatch.type === "normal" && (
              <WinnerSelection participants={mockMatch.participants} />
            )}
            <GameStatus status={mockMatch.status} />
          </div>
        </div>
      </OuterContainer>
    </div>
  );
}
