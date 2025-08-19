import React from 'react';

interface MatchListItemProps {
  player1: string;
  player2: string;
  roundInfo: string;
  status: string;
  onViewClick: () => void; 
}

const MatchListItem: React.FC<MatchListItemProps> = ({
  player1,
  player2,
  roundInfo,
  status,
  onViewClick,
}) => {
  return (
    <div className="flex items-center justify-between w-full p-3 border-b-2 border-cyan-800 last:border-b-0">
      {/* Grupa informacji o meczu */}
      <div className="flex items-center gap-8 flex-1">
        <span className="text-white font-semibold w-48">{`${player1} vs ${player2}`}</span>
        <span className="text-gray-400 w-32">{roundInfo}</span>
        <span className="text-gray-400 flex-1">{status}</span>
      </div>
      
      {/* Przycisk akcji */}
      <button 
        onClick={onViewClick}
        className="border border-cyan-400 text-cyan-400 font-semibold rounded-full px-6 py-1 hover:bg-cyan-400 hover:text-gray-900 transition-colors duration-200"
      >
        Podgląd
      </button>
    </div>
  );
};

export default MatchListItem;