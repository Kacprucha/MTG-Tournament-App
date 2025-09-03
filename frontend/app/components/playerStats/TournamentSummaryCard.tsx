import React from 'react';

interface TournamentSummaryCardProps {
  tournamentName: string;
  gamesPlayed: number;
  totalPoints: number;
}

const StatPill: React.FC<{ value: string | number, isRound?: boolean }> = ({ value, isRound = false }) => (
  <div className={`
    bg-cyan-500 text-white font-semibold text-center
    ${isRound ? 'rounded-full w-10 h-10 flex items-center justify-center' : 'rounded-lg px-4 py-1'}
  `}>
    {value}
  </div>
);

const TournamentSummaryCard: React.FC<TournamentSummaryCardProps> = ({
  tournamentName,
  gamesPlayed,
  totalPoints,
}) => {
  return (
    <div className="border border-cyan-400 rounded-lg p-6 bg-[#293132] flex flex-col gap-6 h-full">
      {/* Sekcja Aktualny turniej */}
      <div className="flex flex-col items-start gap-2">
        <span className="text-white font-bold">Aktualny turniej:</span>
        <StatPill value={tournamentName} />
      </div>

      {/* Sekcja Rozegrane gry */}
      <div className="flex flex-col items-start gap-2">
        <span className="text-white font-bold">Rozegrane gry:</span>
        <StatPill value={gamesPlayed} isRound={true} />
      </div>

      {/* Sekcja Ilość zdobytych punktów */}
      <div className="flex flex-col items-start gap-2">
        <span className="text-white font-bold">Ilość zdobytych punktów:</span>
        <StatPill value={Math.floor(totalPoints)} />
      </div>
    </div>
  );
};

export default TournamentSummaryCard;