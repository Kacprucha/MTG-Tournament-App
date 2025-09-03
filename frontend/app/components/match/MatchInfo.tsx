interface MatchInfoProps {
  round: number;
  totalRounds: number;
  opponent: string;
  tableNumber: number;
}

export default function MatchInfo({ round, totalRounds, opponent, tableNumber }: MatchInfoProps) {
  return (
    <div className="border-2 border-cyan-400 rounded-lg p-4 text-white flex flex-col gap-2">
      <div>
        <span className="font-semibold">Runda Turnieju:</span>{" "}
        <span className="text-cyan-400">{round} / {totalRounds}</span>
      </div>
      <div>
        <span className="font-semibold">Twój przeciwnik:</span>{" "}
        <span className="text-cyan-400">{opponent}</span>
      </div>
      <div>
        <span className="font-semibold">Numer stolika:</span>{" "}
        <span className="text-cyan-400">{tableNumber}</span>
      </div>
    </div>
  );
}
