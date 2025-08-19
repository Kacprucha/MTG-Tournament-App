"use client";

import React from 'react';
import WinProgressRow from './WinProgressRow';


interface TournamentStageCardProps {
  stageName: string;
  participants: string[];
  matchWinner: string[];
  // ... ewentualnie inne pola jak 'round', 'stageName' etc.
}

const TournamentStageCard: React.FC<TournamentStageCardProps> = ({ stageName, participants, matchWinner }) => {
  
  const calculateWins = (playerName: string): number => {
    return matchWinner.filter(winner => winner === playerName).length;
  };

  return (
    <div className="border border-cyan-400 rounded-lg p-6 bg-[#293132] flex flex-col gap-8 w-full h-full">
      {/* Sekcja: Etap rozgrywki */}
      <div className="flex flex-col items-start gap-3">
        <h2 className="text-white font-bold text-xl">Etap rozgrywki:</h2>
        <div className="bg-cyan-500 text-white font-semibold rounded-full px-6 py-2">
          {stageName}
        </div>
      </div>

      {/* Sekcja: Progres zwycięstw */}
      <div className="flex flex-col items-start gap-4">
        <h2 className="text-white font-bold text-xl">Progres zwycięstw:</h2>
        
        {/* Kontener na listę graczy */}
        <div className="flex flex-col gap-3">
          {participants.map(playerName => (
            <WinProgressRow 
              key={playerName}
              playerName={playerName}
              wins={calculateWins(playerName)} 
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default TournamentStageCard;