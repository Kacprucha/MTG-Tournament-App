"use client";

import { Achievement, ScoreboardEntry } from "@/types/tournament";
import { useState, useMemo } from "react";

interface ScoreboardProps {
  scoreboard: ScoreboardEntry[];
  achievements: Achievement[];
}

export default function Scoreboard({ scoreboard, achievements }: ScoreboardProps) {
  const [selectedStatId, setSelectedStatId] = useState<string>("points");

  const getStatValue = (row: ScoreboardEntry) => {
    if (selectedStatId === "points") {
      return row.points;
    }

    return row.achievements?.[selectedStatId] ?? 0;
  };

  const sortedScoreboard = useMemo(() => {
     if (!scoreboard) return [];
    return [...scoreboard].sort((a, b) => getStatValue(b) - getStatValue(a));
  }, [scoreboard, selectedStatId]);

  return (
    <div className="border border-cyan-400 rounded-md p-4 text-white flex flex-col">
      <div className="flex justify-between items-center mb-2">
        <h2 className="font-bold">Tabela wyników:</h2>
        <select
          value={selectedStatId}
          onChange={(e) => setSelectedStatId(e.target.value)}
          className="bg-[#293132] border border-cyan-400 rounded px-2 py-1 text-white"
        >
          <option value="points">Punktacja</option>
          {achievements.map((ach, idx) => (
            <option key={ach.id} value={ach.id.toString()}>
              Osiągnięcie {idx + 1}
            </option>
          ))}
        </select>
      </div>

      <ul className="space-y-1">
        {sortedScoreboard.map((row, idx) => (
          <li
            key={row.userKeycloakId}
            className="flex justify-between border-b border-cyan-400 py-1"
          >
            <span>
              {idx + 1}. {row.username}
            </span>
            <span className="font-bold">{getStatValue(row)}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
