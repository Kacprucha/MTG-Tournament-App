import { Achievement } from "@/types/tournament";

interface AchievementsListProps {
  achievements: Achievement[];
}

export default function AchievementsList({ achievements }: AchievementsListProps) {
  return (
    <div className="border border-cyan-400 rounded-md p-4 text-white">
      <h2 className="font-bold mb-2">Osiągnięcia możliwe do zdobycia:</h2>
      {achievements && achievements.length > 0 ? (
        <ol className="list-decimal list-inside space-y-1">
          {achievements.map((ach, index) => (
            <li key={ach.id}>
              <span>{ach.name}</span>
            </li>
          ))}
        </ol>
      ) : (
        <p className="text-gray-500">Brak zdefiniowanych osiągnięć dla tego turnieju.</p>
      )}
    </div>
  );
}
