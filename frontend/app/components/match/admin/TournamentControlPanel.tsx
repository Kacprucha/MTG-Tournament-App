import React from 'react';

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
      <div className="mt-auto">
        <button 
          onClick={onNewPairingClick}
          className="w-full border-2 border-indigo-400 text-indigo-400 font-bold rounded-full py-2 hover:bg-indigo-400 hover:text-white transition-colors duration-200"
        >
          Nowe losowanie
        </button>
      </div>
    </div>
  );
};

export default TournamentControlPanel;