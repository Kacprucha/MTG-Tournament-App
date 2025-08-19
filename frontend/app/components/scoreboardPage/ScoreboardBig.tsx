"use client";

import { useSession } from "next-auth/react";
import { useState, useMemo } from "react";

interface ScoreboardProps {
  tournamentName: string;
  scoreboard: {
    name: string;
    points: number;
    achievements?: { [key: number]: number }; // np. { 1: 5, 2: 10 }
  }[];
  achievements: string[];
}

export default function Scoreboard({ tournamentName, scoreboard, achievements }: ScoreboardProps) {
  const { data: session } = useSession();
  const [selectedStat, setSelectedStat] = useState<string>("points");

  const isAdmin = session?.user?.roles?.includes("ADMIN");

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
    <div className="p-4 text-white flex flex-col">
      <div className="flex justify-between items-center mb-2 flex-col">
        <h1 className="text-2xl font-bold mb-2">Tabela wyników:</h1>
        <h1 className="text-xl font-bold mb-2">{tournamentName}</h1>
        {isAdmin && (
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
        )}
      </div>

      <ul className="space-y-1 w-full md:max-w-2xl mx-auto">
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
