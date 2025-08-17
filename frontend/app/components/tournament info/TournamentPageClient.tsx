"use client";

import { useSession } from "next-auth/react";
import OuterContainerHorizontal from "../OuterContainerHorizontal";
import AdminPanel from "./AdminPanel";
import TournamentInfo from "./TournamentInfo";
import AchievementsList from "./AchievementsList";
import Scoreboard from "./Scoreboard";
import { useTournament } from '@/context/TournamentContext';
import { useEffect } from "react";

interface TournamentPageClientProps {
  tournament: any;
}

export default function Tournament ({ tournament }: TournamentPageClientProps) {
  const { data: session } = useSession();

  const isAdmin = session?.user?.roles?.includes("ADMIN");

  const { setCurrentTournament } = useTournament();
  const tournamentId = parseInt(tournament.id, 10);

  useEffect(() => {
    // W prawdziwej aplikacji, tutaj pobrałbyś nazwę turnieju z API
    // const tournamentData = await fetchTournamentById(tournamentId);
    // const tournamentName = tournamentData.name;

    // Użyjemy mockowej nazwy
    const mockTournamentName = tournament.name || "Mock Tournament Name";

    setCurrentTournament(tournamentId, mockTournamentName);

  }, [tournamentId, setCurrentTournament]);

  return (
    <div className="min-h-screen w-full bg-[#293132] relative">
      <OuterContainerHorizontal footer={isAdmin ? <AdminPanel/> : undefined}>
        {/* lewa kolumna */}
        <TournamentInfo tournament={tournament} isAdmin={isAdmin} />
        {/* środek */}
        <AchievementsList achievements={tournament.achievements} />
        {/* prawa kolumna */}
        <Scoreboard scoreboard={tournament.scoreboard} achievements={tournament.achievements}/>
      </OuterContainerHorizontal>
    </div>
  );
}
