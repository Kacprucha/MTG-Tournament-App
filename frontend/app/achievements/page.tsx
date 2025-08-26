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
import { Achievement } from "@/types/tournament";
import { TournamentStatus } from "@/types/enums";

const { Title } = Typography;

const AchievementsPage = () => {
  const { data: session } = useSession();

  const [achievements, setAchievements] = useState<Achievement[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const { tournamentId, tournamentStatus } = useTournament();

  const isAdmin = session?.user?.roles?.includes("ADMIN");
  const id = tournamentId !== null ? tournamentId : "0"; 

  // Pobieranie danych (osiągnięć i statusu turnieju)
  useEffect(() => {
    if (tournamentId && session?.accessToken) {
      const fetchAchievements = async () => {
        try {
          setLoading(true);
          const response = await axios.get<Achievement[]>(
            `http://localhost:8080/achievements?tournamentId=${tournamentId}`,
            {
              headers: {
                Authorization: `Bearer ${session.accessToken}`,
              },
            }
          );
          setAchievements(response.data);
          setError(null);
        } catch (err: any) {
          setError(err.response?.data?.message || "Nie udało się pobrać danych o osiągnięciach.");
          console.error(err);
        } finally {
          setLoading(false);
        }
      };
      fetchAchievements();
    } else {
      setLoading(false);
    }
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
      dataIndex: 'winnerUsername',
      key: 'winnerUsername',
      render: (winner, record) => {
        if (tournamentStatus !== TournamentStatus.FINISHED && !isAdmin) {
          return <Tag >-</Tag>;
        }
        return winner ? <Tag color="green">{winner}</Tag> : <Tag>Brak</Tag>;
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
                  Status turnieju: <Tag color={tournamentStatus === TournamentStatus.FINISHED ? 'green' : 'blue'}>{tournamentStatus}</Tag>
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