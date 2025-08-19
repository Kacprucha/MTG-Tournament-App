import React from 'react';
import StatDisplayRow from './StatDisplayRow';

interface Stat {
  label: string;
  value: number;
}

interface PlayerStatsListProps {
  stats: Stat[];
}

const PlayerStatsList: React.FC<PlayerStatsListProps> = ({ stats }) => {
  return (
    <div className="border border-cyan-400 rounded-lg p-6 bg-[#293132] flex flex-col gap-4 h-full">
      {stats.map((stat) => (
        <StatDisplayRow key={stat.label} label={stat.label} value={stat.value} />
      ))}
    </div>
  );
};

export default PlayerStatsList;