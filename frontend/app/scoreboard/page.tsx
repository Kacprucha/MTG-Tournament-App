"use client";

import OuterContainer from "@/app/components/OuterContainer";
import ScoreboardBig from "@/app/components/scoreboardPage/ScoreboardBig";
import { useTournament } from "@/context/TournamentContext";
import Alert from "antd/es/alert/Alert";

// Tymczasowy mock – w przyszłości tutaj podłączysz zapytanie do API
const mockTournaments = {
  "1": {
    id: "1",
    name: "Winter Championship",
    type: "Sealed",
    addon: "dupa",
    date: "02.08.2025",
    participants: ["testuser", "PlayerTwo", "PlayerThree", "PlayerFour", "PlayerFive", "PlayerSix"],
    achievements: [
      "The most amount of flying creatures at any given time",
      "The most amount of mana a player can produce at a given time",
      "The most amount of damage dealt in a single instance",
      "The least amount of turns for a win",
    ],
    scoreboard: [
      { name: "Dupa 123", points: 1237, achievements: { 1: 3, 2: 5, 3: 0, 4: 1 } },
      { name: "Gracz 2", points: 1180, achievements: { 1: 1, 2: 2, 3: 4, 4: 0 } },
    ],
  },
  "2": {
    id: "2",
    name: "Turniej testowy",
    type: "sealed",
    addon: "beta",
    date: "15.08.2025",
    participants: ["Johnny", "Timmy", "CasualCarl", "NewbieNick", "RegularRick"],
    achievements: ["Win in 2 turns", "Most creatures summoned in one turn"],
    scoreboard: [{ name: "Testowy gracz", points: 900, achievements: { 1: 3, 2: 5, 3: 0, 4: 1 } }, ],
  },
};

export default function ScoreboardPage() {
  const { tournamentId } = useTournament();
  
  const id = tournamentId !== null ? tournamentId : "0"; 
  const tournament = mockTournaments[id as keyof typeof mockTournaments];

  if (!tournament && tournamentId !== null) {
    return (
      <div className="min-h-screen w-full bg-[#293132] flex items-center justify-center text-white">
        <Alert message={"Brak komponentu"} description={ `Turniej o id ${tournamentId} nie istnieje.`} type="error" showIcon className="mt-4" />
      </div>
    );
  }

  if (tournamentId === null) {
    return (
      <div className="min-h-screen w-full bg-[#293132] flex items-center justify-center text-white">
        <Alert message="Brak turnieju" description="Nie wybrano żadnego turnieju." type="warning" showIcon className="mt-4" />
      </div>
    );
  }

  return (
    <main className="min-h-screen w-full bg-[#293132] relative">
      <OuterContainer>
        <ScoreboardBig tournamentName={tournament.name} scoreboard={tournament.scoreboard} achievements={tournament.achievements}/>
    </OuterContainer>
  </main>
  );
}