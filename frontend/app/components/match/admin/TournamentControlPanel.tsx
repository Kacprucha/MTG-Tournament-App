import React from 'react';
import Button from '../../Button';

interface TournamentControlPanelProps {
  currentRound: number;
  totalRounds: number;
  completedGames: number;
  onNewPairingClick: () => void;
}

const TournamentControlPanel: React.FC<TournamentControlPanelProps> = ({
  currentRound,
  totalRounds,
  completedGames,
  onNewPairingClick,
}) => {
  return (
    <div className="border-2 border-cyan-400 rounded-lg p-6 flex flex-col gap-8 h-full">
      {/* Sekcja Runda Turnieju */}
      <div className="flex flex-col items-start gap-2">
        <h2 className="text-white font-bold text-lg">Runda Turnieju:</h2>
        <div className="bg-cyan-500 text-white font-semibold rounded-lg px-6 py-1 text-center">
          {`${currentRound} / ${totalRounds}`}
        </div>
      </div>

      {/* Sekcja Zakończone gry */}
      <div className="flex flex-col items-start gap-2">
        <h2 className="text-white font-bold text-lg">Zakończone gry:</h2>
        <div className="bg-cyan-500 text-white font-semibold rounded-lg px-6 py-1 text-center">
          {completedGames}
        </div>
      </div>

      {/* Przycisk Nowe losowanie */}
      <div className="mt-auto self-center">
        <Button text={"Rozpocznij faze pucharowa"} onClick={onNewPairingClick} disabled={totalRounds !== completedGames}/>
      </div>
    </div>
  );
};

export default TournamentControlPanel;