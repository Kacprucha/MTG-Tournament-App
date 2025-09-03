"use client";

import OuterContainer from "@/app/components/OuterContainer";
import ScoreboardBig from "@/app/components/scoreboardPage/ScoreboardBig";
import { useTournament } from "@/context/TournamentContext";
import { Achievement, ScoreboardEntry, TournamentDetails } from "@/types/tournament";
import Alert from "antd/es/alert/Alert";
import axios, { AxiosError } from "axios";
import { useSession } from "next-auth/react";
import { useEffect, useState } from "react";

export default function ScoreboardPage() {
  const { data: session } = useSession();
  const { tournamentId, tournamentName } = useTournament();
  
  const [scoreboardData, setScoreboardData] = useState<ScoreboardEntry[]>([]);
  const [achievementsData, setAchievementsData] = useState<Achievement[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (tournamentId && session?.accessToken) {
      const fetchScoreboardAndAchievements = async () => {
        try {
          setLoading(true);

          const apiClient = axios.create({
            baseURL: "http://localhost:8080",
            headers: {
              Authorization: `Bearer ${session.accessToken}`,
            },
          });

          const [scoreboardResponse, achievementsResponse] = await Promise.all([
            apiClient.get<TournamentDetails>(`/tournaments/${tournamentId}`),
            apiClient.get<Achievement[]>(`/achievements?tournamentId=${tournamentId}`)
          ]);

          setScoreboardData(scoreboardResponse.data.scoreboard);
          setAchievementsData(achievementsResponse.data);
          setError(null);

        } catch (err: unknown) {
          if (err instanceof AxiosError) {
            setError(err.response?.data?.message || "Nie udało się pobrać danych tabeli wyników.");
          } else {
            setError("Wystąpił nieoczekiwany błąd.");
          }
          console.error(err);
        } finally {
          setLoading(false);
        }
      };
      fetchScoreboardAndAchievements();
    } else {
      setLoading(false);
    }
  }, [tournamentId, session]);

  if (!tournamentId) {
    return (
      <div className="min-h-screen w-full bg-[#293132] flex items-center justify-center text-white">
        <Alert message="Brak turnieju" description="Nie wybrano żadnego turnieju." type="warning" showIcon className="mt-4" />
      </div>
    );
  }

  return (
    <main className="min-h-screen w-full bg-[#293132] relative">
      <OuterContainer>
        <ScoreboardBig tournamentName={tournamentName || ''} scoreboard={scoreboardData} achievements={achievementsData}/>
    </OuterContainer>
  </main>
  );
}