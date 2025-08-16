"use client"

import React, { useState, useEffect } from "react";
import { Row, Col, Spin, Alert } from "antd"; // Importujemy komponenty z Ant Design
import axios from "axios";
import { useSession } from "next-auth/react";
import TournamentCard from "./TournamentCard";
import OuterContainer from "./OuterContainer";
import Button from "./Button";
import { useRouter } from 'next/navigation';

interface Tournament {
  id: number;
  title: string;
  type: string;
  participants: string[];
}

const MOCK_TOURNAMENTS: Tournament[] = [
  {
    id: 1,
    title: "Winter Championship",
    type: "Sealed",
    participants: ["testuser", "PlayerTwo", "PlayerThree", "PlayerFour", "PlayerFive", "PlayerSix"],
  },
  {
    id: 2,
    title: "Summer Cup",
    type: "Standard",
    participants: ["testuser", "BetaTester", "CharlieDuke", "DeltaForce"],
  },
  {
    id: 3,
    title: "Pro Tour Qualifier",
    type: "Modern",
    participants: ["ProPlayer1", "WannabePro", "GrinderX", "Spike"],
  },
  {
    id: 4,
    title: "Friday Night Magic",
    type: "Draft",
    participants: ["Johnny", "Timmy", "CasualCarl", "NewbieNick", "RegularRick"],
  },
  {
    id: 5,
    title: "Store Anniversary",
    type: "Pauper",
    participants: ["BudgetBob", "PennyPincher"],
  },
  {
    id: 6,
    title: "Commander Clash",
    type: "Commander",
    participants: ["MultiplayerMatt", "GroupHugGwen", "StaxSteve", "ComboCarl"],
  },
  {
    id: 7,
    title: "Legacy Legends",
    type: "Legacy",
    participants: ["OldSchoolOscar", "VintageVictor", "EternalEve"],
  },
  {
    id: 8,
    title: "Vintage Vault",
    type: "Vintage",
    participants: ["TimelessTina", "ClassicCarter"],
  },
  {
    id: 9,
    title: "Holiday Special",
    type: "Themed",
    participants: ["FestiveFiona", "JollyJack", "CheerfulChloe"],
  },
  {
    id: 10,
    title: "Charity Event",
    type: "Charity",
    participants: ["GenerousGeorge", "KindKaren", "HelpfulHank"],
  },
  {
    id: 11,
    title: "Local League",
    type: "League",
    participants: ["LeagueLeader", "CompetitiveCathy", "FriendlyFred"],
  },
  {
    id: 12,
    title: "Online Open",
    type: "Online",
    participants: ["NetPlayer1", "WebWarrior", "DigitalDynamo"],
  },
  {
    id: 13,
    title: "Flashback Friday",
    type: "Flashback",
    participants: ["NostalgicNina", "RetroRalph"],
  },
  {
    id: 14,
    title: "Battle of the Planeswalkers",
    type: "Planeswalker",
    participants: ["WalkerWendy", "StrategistSam"],
  },
  {
    id: 15,
    title: "Epic Showdown",
    type: "Epic",
    participants: ["HeroicHannah", "LegendaryLeo"],
  },
];

export default function TournamentsGrid () {
  const { data: session } = useSession();
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const router = useRouter();
  const isAdmin = session?.user?.roles?.includes("ADMIN");
  const currentUsername = session?.user?.username;

  // Efekt do pobierania danych, gdy komponent się zamontuje
  // useEffect(() => {
  //   if (session?.accessToken) {
  //     const fetchTournaments = async () => {
  //       try {
  //         setLoading(true);
  //         const response = await axios.get(
  //           "http://localhost:8080/api/tournaments",
  //           {
  //             headers: {
  //               Authorization: `Bearer ${session.accessToken}`,
  //             },
  //           }
  //         );
  //         setTournaments(response.data);
  //         setError(null);
  //       } catch (err) {
  //         setError("Nie udało się pobrać danych o turniejach.");
  //         console.error(err);
  //       } finally {
  //         setLoading(false);
  //       }
  //     };

  //     fetchTournaments();
  //   } else {
  //       setLoading(false);
  //   }
  // }, [session]);
  useEffect(() => {
        setLoading(true);
        const timer = setTimeout(() => {
            setTournaments(MOCK_TOURNAMENTS);
            setLoading(false);
        }, 1500);
        return () => clearTimeout(timer);
    }, []);

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

  const handleCardClick = (id: number) => {
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
                <TournamentCard
                  title={tournament.title}
                  type={tournament.type}
                  imageFile={"mtg_logo.svg"}
                  buttonText={(tournament.participants.includes(currentUsername) || isAdmin) ? "Zobacz" : "Zapisz się"}
                  onButtonClick={() => handleCardClick(tournament.id)}
                />
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