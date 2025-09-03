import React from 'react';
import MatchListItem from './MatchListItem';
import { MatchSummary } from '@/types/tournament';

interface Match {
  id: number;
  player1: string;
  player2: string;
  round: number;
  status: string;
}

interface MatchListProps {
  matches: MatchSummary[];
}

const MatchList: React.FC<MatchListProps> = ({ matches }) => {
  const handleViewMatch = (matchId: number) => {
    console.log(`Przechodzenie do podglądu meczu o ID: ${matchId}`);
    // Tutaj logika nawigacji, np. router.push(`/matches/${matchId}`)
  };

  return (
    // Główny kontener listy z podwójną ramką
    <div className="border-2 border-cyan-400 rounded-lg p-4 h-full">
      {matches.length > 0 ? (
        matches.map(match => (
          <MatchListItem
            key={match.id}
            player1={match.participantUsernames.at(0)!}
            player2={match.participantUsernames.at(1)!}
            roundInfo={`Runda ${match.round}`}
            status={match.status}
            onViewClick={() => handleViewMatch(match.id)}
          />
        ))
      ) : (
        <p className="text-gray-400 text-center p-8">Brak meczów do wyświetlenia.</p>
      )}
    </div>
  );
};

export default MatchList;