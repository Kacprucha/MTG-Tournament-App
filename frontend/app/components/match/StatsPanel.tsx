import { useEffect, useState } from "react";
import StatRow from "./StatRow";
import { useSession } from "next-auth/react";
import { Alert, Spin } from "antd";
import { Achievement } from "@/types/tournament";

interface StatsPanelProps {
  achievements: Achievement[]; 
  statValues: { [achievementId: number]: number }; 
  onStatChange: (achievementId: number, newValue: number) => void; 
}

export default function StatsPanel({ achievements, statValues, onStatChange }: StatsPanelProps) {
  if (!achievements) {
    return <Alert message="Brak definicji osiągnięć." type="info" />;
  }

  return (
    <div className="border-2 border-cyan-400 rounded-lg p-4 text-white flex flex-col gap-3 h-full">
      <div className="flex-1 overflow-y-auto pr-2">
        {achievements.map(ach => (
          <StatRow
            key={ach.id}
            label={ach.name}
            value={statValues[ach.id] ?? 0}
            onChange={(newValue) => onStatChange(ach.id, newValue)}
          />
        ))}
      </div>
    </div>
  );

  // return (
  //   <div className="border-2 border-cyan-400 rounded-lg p-4 text-white flex flex-col gap-3">
  //     {statDefinitions.map(stat => (
  //         <StatRow
  //           key={stat.id}
  //           label={stat.label}
  //           value={0} // Użyj wartości ze stanu (lub 0, jeśli nie istnieje)
  //           // Użyj partial application, aby przekazać stat.id do handlera
  //           onChange={(newValue) => handleStatChange(stat.id, newValue)}
  //         />
  //       ))}
  //   </div>
  // );
}
