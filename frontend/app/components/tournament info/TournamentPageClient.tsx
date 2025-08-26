"use client";

import { useSession } from "next-auth/react";
import OuterContainerHorizontal from "../OuterContainerHorizontal";
import AdminPanel from "./AdminPanel";
import TournamentInfo from "./TournamentInfo";
import AchievementsList from "./AchievementsList";
import Scoreboard from "./Scoreboard";
import { useTournament } from '@/context/TournamentContext';
import { useEffect, useState } from "react";
import { TournamentDetails } from "@/types/tournament";
import axios from "axios";
import Alert from "antd/es/alert/Alert";
import { Spin } from "antd";

interface TournamentPageClientProps {
  tournamentId: number;
}

export default function TournamentPageClient ({ tournamentId: tournamentID }: TournamentPageClientProps) {
  const { data: session } = useSession();
  const { setCurrentTournament } = useTournament();

  const [tournament, setTournament] = useState<TournamentDetails | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const isAdmin = session?.user?.roles?.includes("ADMIN");

  useEffect(() => {
    if (tournamentID && session?.accessToken) {
      const fetchTournamentDetails = async () => {
        try {
          setLoading(true);
          
          const apiClient = axios.create({
            baseURL: "http://localhost:8080", // Baza dla wszystkich zapytań
            headers: {
              Authorization: `Bearer ${session.accessToken}`,
            },
          });

          const tournamentRequest = apiClient.get<TournamentDetails>(`/tournaments/${tournamentID}`);
          // Te dwa poniżej są opcjonalne, jeśli `GET /tournaments/{id}` zwraca już wszystko
          // const matchesRequest = apiClient.get(`/matches?tournamentId=${tournamentId}`);
          // const achievementsRequest = apiClient.get(`/achievements?tournamentId=${tournamentId}`);
          
          // Wykonujemy zapytanie o główne dane turnieju
          // Wg Twojego DTO, ten jeden endpoint zwraca już wszystko, czego potrzebujemy!
          const response = await tournamentRequest;
          
          setTournament(response.data);
          setCurrentTournament(response.data.id, response.data.name, response.data.status)
          setError(null);

        } catch (err: any) {
          setError(err.response?.data?.message || "Nie udało się pobrać danych turnieju.");
          console.error(err);
        } finally {
          setLoading(false);
        }
      };

      fetchTournamentDetails();
    } else if (!session) {
      setLoading(false); 
    }
  }, [tournamentID, session, setCurrentTournament]);

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

  if (!tournament) {
    return (
      <div className="min-h-screen w-full bg-[#293132] flex items-center justify-center text-white">
        <Alert message="Brak turnieju" description="Nie wybrano żadnego turnieju." type="warning" showIcon className="mt-4" />
      </div>
    );
  }

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
