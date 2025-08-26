"use client"

import React, { useState, useEffect } from "react";
import { Row, Col, Spin, Alert } from "antd"; // Importujemy komponenty z Ant Design
import axios from "axios";
import { useSession } from "next-auth/react";
import TournamentCard from "./TournamentCard";
import OuterContainer from "./OuterContainer";
import Button from "./Button";
import { useRouter } from 'next/navigation';
import { TournamentStatus } from "@/types/enums";

interface Tournament {
  id: number;
  name: string;
  type: string;
  status: TournamentStatus;
  participants?: string[];
}

export default function TournamentsGrid () {
  const { data: session } = useSession();
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const router = useRouter();
  const isAdmin = session?.user?.roles?.includes("ADMIN");
  const currentUsername = session?.user?.username;

  useEffect(() => {
    if (session?.accessToken) {
      const fetchTournaments = async () => {
        try {
          setLoading(true);
          const response = await axios.get<Tournament[]>( 
            "http://localhost:8080/tournaments",
            {
              headers: {
                Authorization: `Bearer ${session.accessToken}`,
              },
            }
          );
          setTournaments(response.data);
          setError(null);
        } catch (err) {
          setError("Nie udało się pobrać danych o turniejach.");
          console.error(err);
        } finally {
          setLoading(false);
        }
      };
      fetchTournaments();
    } else {
        setLoading(false);
    }
  }, [session]);

  // Renderowanie stanu ładowania
  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
        <Spin size="large" />
      </div>
    );
  }

  // Renderowanie błędu
  if (error) {
    return <Alert message="Błąd" description={error} type="error" showIcon />;
  }

  const handleShowClick = (id: number) => {
    console.log(`Przechodzę do szczegółów turnieju o ID: ${id}`);
    router.push(`/tournaments/${id}`);
  };

  const handleJoinClick = (id: number) => {
    console.log(`Przechodzę do szczegółów turnieju o ID: ${id}`);
    router.push(`/tournaments/${id}`);
  };

  const handleCreateClick = () => {
    console.log("Otwieranie formularza tworzenia nowego turnieju...");
    router.push("/tournaments/new");
  };

  return (
    <main className="min-h-screen w-full bg-[#293132] relative">
      <OuterContainer>
        <div className="scrollable-grid-container" style={{ flexGrow: 1, minHeight: 0, padding: '20px 20px 20px 20px' }}>
          <Row gutter={[16, 16]}> 
            {tournaments.map((tournament) => (
              <Col key={tournament.id} xs={24} sm={12} md={8} lg={6} xl={4}>
                {(tournament.status == TournamentStatus.FINISHED || tournament.participants?.includes(currentUsername) || isAdmin) && (
                  <TournamentCard
                    title={tournament.name}
                    type={tournament.type}
                    imageFile={"mtg_logo.svg"}
                    buttonText={"Zobacz"}
                    onButtonClick={() => handleShowClick(tournament.id)}
                  />
                )}
                {(tournament.status == TournamentStatus.PUBLISHED && !tournament.participants?.includes(currentUsername)) && (
                  <TournamentCard
                    title={tournament.name}
                    type={tournament.type}
                    imageFile={"mtg_logo.svg"}
                    buttonText={"Zapisz się"}
                    onButtonClick={() => handleJoinClick(tournament.id)}
                  />
                )}
              </Col>
            ))}
          </Row>
        </div>
        {isAdmin && (
          <div style={{ position: 'fixed', right: 80, bottom: 60}}>
            <Button
              text="Utwórz"
              onClick={handleCreateClick}
            />
          </div>
        )}
      </OuterContainer>
    </main>
  );
}