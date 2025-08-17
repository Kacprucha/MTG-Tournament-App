interface WinnerSelectionProps {
  participants: string[];
}

export default function WinnerSelection({ participants }: WinnerSelectionProps) {
  return (
    <div className="border-2 border-cyan-400 rounded-lg p-4 text-white">
      <div className="mb-2 font-semibold">Kto wygrał:</div>
      <div className="flex flex-col gap-2">
        {participants.map((p, idx) => (
          <label key={idx} className="flex items-center gap-2">
            <input type="checkbox" />
            {p}
          </label>
        ))}
      </div>
    </div>
  );
}
