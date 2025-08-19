"use client";

import React, { useState, useEffect } from "react";
import { Table, Spin, Alert, Typography, Tag } from "antd";
import type { ColumnsType } from 'antd/es/table';
import axios from "axios";
import { useSession } from "next-auth/react";
import { useTournament } from "@/context/TournamentContext";
import OuterContainer from "../components/OuterContainer";
import AntDTheme from "../components/AntDTheme";
import NavLink from "../components/NavLink";

const { Title } = Typography;

interface Achievement {
  id: number;
  name: string;
  price: string;
  winner: string | null;
}

// Typ dla statusu turnieju
type TournamentStatus = 'nadchodzący' | 'w trakcie' | 'zakończony';

const AchievementsPage = () => {
  const { data: session } = useSession();

  const [achievements, setAchievements] = useState<Achievement[]>([]);
  const [tournamentStatus, setTournamentStatus] = useState<TournamentStatus | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const { tournamentId } = useTournament();

  const isAdmin = session?.user?.roles?.includes("ADMIN");
  const id = tournamentId !== null ? tournamentId : "0"; 

  // Pobieranie danych (osiągnięć i statusu turnieju)
  useEffect(() => {
    const fetchData = async () => {
      if (!session?.accessToken) {
        setLoading(false);
        return; 
      }

      try {
        setLoading(true);

        // --- MOCKOWE DANE (do zastąpienia prawdziwym API) ---
        const mockTournamentStatus: TournamentStatus = String(id) === '1' ? 'zakończony' : 'w trakcie';
        
        const mockAchievements: Achievement[] = [
          { id: 1, name: "Pierwsza Krew", price: "Booster Pack", winner: "PlayerOne" },
          { id: 2, name: "Najwięcej punktów życia", price: "Playmata", winner: "PlayerTwo" },
          { id: 3, name: "Mistrz Kombinacji", price: "Tokeny", winner: null },
          { id: 4, name: "Niepokonany", price: "Specjalna karta promo", winner: "ProPlayer1" },
        ];
        // --------------------------------------------------

        /*
        const tournamentDetailsPromise = axios.get(`/api/tournaments/${tournamentId}`, { headers: { Authorization: `Bearer ${session.accessToken}` } });
        const achievementsPromise = axios.get(`/api/tournaments/${tournamentId}/achievements`, { headers: { Authorization: `Bearer ${session.accessToken}` } });

        const [tournamentResponse, achievementsResponse] = await Promise.all([tournamentDetailsPromise, achievementsPromise]);

        setTournamentStatus(tournamentResponse.data.status);
        setAchievements(achievementsResponse.data);
        */

        setTournamentStatus(mockTournamentStatus);
        setAchievements(mockAchievements);

      } catch (err) {
        setError("Nie udało się pobrać danych o osiągnięciach.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [tournamentId, session]);

  const columns: ColumnsType<Achievement> = [
    {
      title: 'Nazwa Osiągnięcia',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Nagroda',
      dataIndex: 'price',
      key: 'price',
    },
    {
      title: 'Zwycięzca',
      dataIndex: 'winner',
      key: 'winner',
      render: (winner, record) => {
        if (tournamentStatus !== 'zakończony') {
          return <Tag >-</Tag>;
        }
        return winner ? winner : <Tag>Brak</Tag>;
      },
    },
  ];

  if (loading) return <Spin tip="Ładowanie osiągnięć..." size="large" fullscreen />;
  if (error) return <Alert message="Błąd" description={error} type="error" showIcon />;

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
        <AntDTheme>
          <div style={{ padding: '24px' }}>
            <Title level={2}>Tablica Osiągnięć</Title>
              {isAdmin && (
                <p style={{ marginBottom: '16px' }}>
                  Status turnieju: <Tag color={tournamentStatus === 'zakończony' ? 'green' : 'blue'}>{tournamentStatus}</Tag>
                </p>
              )}
              <Table
                columns={columns}
                dataSource={achievements.map(a => ({ ...a, key: a.id }))} 
                pagination={false}
                bordered
              />
          </div>
        </AntDTheme>
        {isAdmin && (
          <div style={{ position: 'fixed', right: 80, bottom: 60}}>
            <NavLink href="/achievements/new">Dodaj</NavLink>
          </div>
        )}
      </OuterContainer>
    </main>
  );
};

export default AchievementsPage;