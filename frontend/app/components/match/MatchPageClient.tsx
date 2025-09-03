"use client";

import React, { useState, useEffect, useCallback } from 'react';
import { useSession } from 'next-auth/react';
import axios, { AxiosError } from 'axios';
import { Spin, Alert, message, Modal, Button } from 'antd';

import MatchInfo from './MatchInfo';
import ControlButtons from './ControlButtons';
import StatsPanel from './StatsPanel';
import TournamentStageCard from './TournamentStageCard';
import { MatchDetails } from '@/types/match';
import OuterContainer from '../OuterContainer';
import Timer from './Timer';
import LifeCounter from './LifeCounter';
import WinnerSelection from './WinnerSelectionProps';
import GameStatus from './GameStatus';
import { Achievement, TournamentDetails } from '@/types/tournament';
import { useMatchWebSocket, WebSocketMessage } from '@/hooks/useMatchWebSocket';
import { useRouter } from 'next/navigation';
import { MatchStatus } from '@/types/enums';

interface MatchPageClientProps {
  matchId: number;
}

const MatchPageClient = ({ matchId }: MatchPageClientProps) => {
  const { data: session } = useSession();
  const router = useRouter();

  const [match, setMatch] = useState<MatchDetails | null>(null);
  const [achievementsDef, setAchievementsDef] = useState<Achievement[]>([]);
  const [statValues, setStatValues] = useState<{ [achievementId: number]: number }>({});
  const [tournamentRounds, setTournamentRounds] = useState<number>(-1);
  const [winnerModalVisible, setWinnerModalVisible] = useState(false);
  const [isTimerRunning, setIsTimerRunning] = useState(false);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

   const { isConnected, lastMessage, sendMessage } = useMatchWebSocket(matchId);

  const isAdmin = session?.user?.roles?.includes("ADMIN");
  const currentUsername = session?.user?.username;

  const fetchInitialData = useCallback(async () => { 
    if (matchId && session?.accessToken) {
      const fetchMatch = async () => {
        try {
          setLoading(true);
          const apiClient = axios.create({
            baseURL: "http://localhost:8080/",
            headers: { Authorization: `Bearer ${session.accessToken}` },
          });

          const matchResponse = await apiClient.get<MatchDetails>(`/matches/${matchId}`);
          const matchData = matchResponse.data;
          setMatch(matchData);

          if (!matchData.tournamentId) {
            throw new Error("Mecz nie jest przypisany do żadnego turnieju.");
          }

          if (matchData.status === MatchStatus.IN_PROGRESS) {
            setIsTimerRunning(true);
          } else {
            setIsTimerRunning(false);
          }

          const tournamentResponse = await apiClient.get<TournamentDetails>(
            `/tournaments/${matchData.tournamentId}`
          );
          const tournamentData = tournamentResponse.data;

          setAchievementsDef(tournamentData.achievements);
          
          const initialValues: { [achievementId: string]: number } = {};
          tournamentData.achievements.forEach(ach => {
            const participantIdStr = session.user?.id ?? '';
            const savedValue = matchData.achievements?.[participantIdStr]?.[ach.id];
            initialValues[ach.id.toString()] = savedValue ?? 0;
          });
          setStatValues(initialValues);

          setTournamentRounds(tournamentData.participantUsernames.length - 1)

          setError(null);
        } catch (err: unknown) {
          if (err instanceof AxiosError) {
            setError(err.response?.data?.message || "Nie udało się pobrać danych meczu.");
          } else {
            setError("Wystąpił nieoczekiwany błąd.");
          }
        } finally {
          setLoading(false);
        }
      };
      fetchMatch();
    }
  }, [matchId, session]);

  useEffect(() => {
    fetchInitialData();
  }, [fetchInitialData]);

   useEffect(() => {
    if (lastMessage) {
      // Otrzymano nową wiadomość z serwera
      switch (lastMessage.type) {
        case 'GAME_STARTED':
          message.info('Gra została rozpoczęta!');
          setIsTimerRunning(true);
          fetchInitialData();
          break;
        case 'WINNER_SELECTED':
          // Wyświetl powiadomienie, że drugi gracz dokonuje wyboru
          if (lastMessage.selectingPlayer !== session?.user?.username) {
            message.loading({
              content: `Gracz ${lastMessage.selectingPlayer} wybiera zwycięzcę...`,
              key: 'winnerSelection',
              duration: 10 // Pokaż na 10 sekund
            });
          }
          break;
        case 'FINISH_ATTEMPT':
        // Jesteśmy Graczem B, Gracz A chce zakończyć grę
          if (lastMessage.selectingPlayer !== session?.user?.username) {
            message.info(`Gracz ${lastMessage.selectingPlayer} zakończył grę. Potwierdzanie wyników...`);
          
            // Odsyłamy nasze statystyki w odpowiedzi
            sendMessage(`/app/match/${matchId}/finish-confirm`, {
                // Dane Gracza B (własne)
                confirmingPlayerStats: statValues,
                
                // Dane Gracza A (otrzymane w wiadomości)
                originalReportingPlayerId: lastMessage.reportingPlayerId,
                chosenWinnerUsername: lastMessage.chosenWinnerUsername,
                originalReportedStats: lastMessage.reportedStats,
            });
          }
          break;
        case 'GAME_FINISHED_REDIRECT':
          message.success('Mecz zakończony! Przekierowywanie do wyników...', 3);
          // Zamknij wszystkie powiadomienia i modale
          message.destroy('winnerSelection');
          setWinnerModalVisible(false);
          // Przekieruj obu graczy
          setTimeout(() => router.push('/my-stats'), 3000);
          break;
      }
    }
  }, [lastMessage, session?.user?.username, fetchInitialData, router]);

  const handleStatChange = (achievementId: number, newValue: number) => {
    setStatValues(prev => ({
      ...prev,
      [achievementId]: newValue,
    }));
  };

  const handleStartMatch = () => {
    // Wysyłamy wiadomość na endpoint w WebSocketController
    sendMessage(`/app/match/${matchId}/start`);
  };

  const handleEndMatch = () => {
    setWinnerModalVisible(true);
  };

  const handleWinnerSelected = async (winnerUsername: string) => {
    
    if (!match || !isConnected || !session?.accessToken) {
      message.error("Błąd: Brak połączenia lub autoryzacji. Spróbuj odświeżyć stronę.");
      return;
    }
    // Zamykamy modal od razu dla lepszego UX
    setWinnerModalVisible(false);
    message.loading({ 
      content: 'Oczekiwanie na wyniki od drugiego gracza...', 
      key: 'finishing',
      duration: 30 // Ustaw długi czas, bo czekamy na akcję drugiego gracza
    });

    try {
      sendMessage(`/app/match/${matchId}/finish-attempt`, {
        // `type` nie jest potrzebny, bo backend go uzupełni na podstawie DTO
          chosenWinnerUsername: winnerUsername,
          reportedStats: statValues,
      });

      const achievementsPayload: { [participantId: string]: { [achievementId: string]: number } } = {};
      const currentUserId = session.user.id; 

      if (currentUserId) {
        achievementsPayload[currentUserId] = statValues;
      }
    
      const payload = {
        // Status jest teraz kontrolowany przez backend, więc nie wysyłamy go,
        // ale jeśli musisz, możesz go dodać:
        // status: 'COMPLETED',
        gameWinners: [winnerUsername],
        winnerUsername: winnerUsername, 
        achievements: achievementsPayload,
      };
    
      await axios.put(
        `http://localhost:8080/matches/${match.id}/results`,
        payload,
        {
          headers: { Authorization: `Bearer ${session.accessToken}` },
        }
      );

      message.success({ content: 'Wyniki zapisane! Za chwilę nastąpi przekierowanie.', key: 'savingResults' });

    } catch (err: unknown) {
      if (err instanceof AxiosError) {
        message.error({
          content: err.response?.data?.message || "Nie udało się zapisać wyników.",
          key: "savingResults",
          duration: 5,
        });
      } else {
        message.error({
          content: "Wystąpił nieoczekiwany błąd.",
          key: "savingResults",
          duration: 5,
        });
      }
    }
  };
  
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Spin size="large" fullscreen />
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Błąd" description={error} type="error" />
      </div>
    );
  } 

  if (!match) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Nie znaleziono meczu." type="warning" />
      </div>
    );
  }
  
  // Znajdź przeciwnika
  const opponent = match.participantUsernames.find(name => name !== currentUsername);

  return (
    <OuterContainer>
      <div className="flex flex-1 gap-4 p-4">
        {/* Lewa sekcja */}
        <div className="flex flex-col gap-4 w-1/4">
          <MatchInfo
            round={match.round}
            totalRounds={tournamentRounds} 
            opponent={opponent || 'Przeciwnik'}
            tableNumber={match.tableNumber}
          />
          {/* Logika do zmiany */}
          {(match.status === 'PENDING' || match.status === 'IN_PROGRESS' || isAdmin) && (
            <ControlButtons onStartClick={handleStartMatch} onEndClick={handleEndMatch} matchStatus={match.status} />
          )}
          <Modal
            title="Wybierz zwycięzcę"
            open={winnerModalVisible}
            onCancel={() => setWinnerModalVisible(false)}
            footer={match?.participantUsernames.map(name => (
              <Button key={name} type="primary" onClick={() => handleWinnerSelected(name)}>
                {name}
              </Button>
            ))}
          >
            <p>Kto wygrał tę partię?</p>
          </Modal>
        </div>

        {/* Środek */}
        {match.type === "runa turniejowa" ? (
          <div className="flex-1">
            <StatsPanel 
              achievements={achievementsDef}
              statValues={statValues} 
              onStatChange={handleStatChange}
            />
          </div>
        ) : (
          <div className="flex-1">
            <TournamentStageCard 
              stageName={match.type}
              participants= {match.participantUsernames}
              matchWinner= {match.gameWinners}
            />
          </div>
        )}

        {/* Prawa sekcja */}
        <div className="flex flex-col gap-4 w-1/4">
          <Timer isRunning={isTimerRunning}/>
          <LifeCounter />
          {match.type === "runa turniejowa" && (
            <WinnerSelection participants={match.participantUsernames} />
          )}
          <GameStatus status={match.status} />
          <div style={{ position: 'fixed', bottom: 10, right: 10, padding: 5, background: isConnected ? 'green' : 'red', color: 'white', borderRadius: 5 }}>
            {isConnected ? 'Połączono' : 'Rozłączono'}
          </div>
        </div>
      </div>
    </OuterContainer>
  );
};

export default MatchPageClient;