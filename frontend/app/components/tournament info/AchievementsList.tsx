interface AchievementsListProps {
  achievements: string[];
}

export default function AchievementsList({ achievements }: AchievementsListProps) {
  return (
    <div className="border border-cyan-400 rounded-md p-4 text-white">
      <h2 className="font-bold mb-2">Osiągnięcia możliwe do zdobycia:</h2>
      <ul className="list-decimal list-inside space-y-1">
        {achievements.map((ach, idx) => (
          <li key={idx}>{ach}</li>
        ))}
      </ul>
    </div>
  );
}
