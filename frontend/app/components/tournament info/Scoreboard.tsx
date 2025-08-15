"use client";

import { useState, useMemo } from "react";

interface ScoreboardProps {
  scoreboard: {
    name: string;
    points: number;
    achievements?: { [key: number]: number }; // np. { 1: 5, 2: 10 }
  }[];
  achievements: string[];
}

export default function Scoreboard({ scoreboard, achievements }: ScoreboardProps) {
  const [selectedStat, setSelectedStat] = useState<string>("points");

  const getStatValue = (row: typeof scoreboard[number]) => {
    if (selectedStat === "points") {
      return row.points;
    }
    const achievementIndex = parseInt(selectedStat.replace("achievement-", ""), 10);
    return row.achievements?.[achievementIndex] ?? 0;
  };

  const sortedScoreboard = useMemo(() => {
    return [...scoreboard].sort((a, b) => getStatValue(b) - getStatValue(a));
  }, [scoreboard, selectedStat]);

  return (
    <div className="border border-cyan-400 rounded-md p-4 text-white flex flex-col">
      <div className="flex justify-between items-center mb-2">
        <h2 className="font-bold">Tabela wyników:</h2>
        <select
          value={selectedStat}
          onChange={(e) => setSelectedStat(e.target.value)}
          className="bg-[#293132] border border-cyan-400 rounded px-2 py-1 text-white"
        >
          <option value="points">Punktacja</option>
          {achievements.map((_, idx) => (
            <option key={idx} value={`achievement-${idx + 1}`}>
              Osiągnięcie {idx + 1}
            </option>
          ))}
        </select>
      </div>

      <ul className="space-y-1">
        {sortedScoreboard.map((row, idx) => (
          <li
            key={idx}
            className="flex justify-between border-b border-cyan-400 py-1"
          >
            <span>
              {idx + 1}. {row.name}
            </span>
            <span className="font-bold">{getStatValue(row)}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
