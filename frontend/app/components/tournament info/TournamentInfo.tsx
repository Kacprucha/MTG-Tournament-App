interface TournamentInfoProps {
  tournament: {
    name: string;
    type: string;
    addon: string;
    date: string;
    participants: number;
  };
}

export default function TournamentInfo({ tournament }: TournamentInfoProps) {
  return (
    <div className="border border-cyan-400 rounded-md p-4 text-white flex flex-col gap-2">
      <p>
        <strong>Nazwa turnieju:</strong>
        <br />
        <span className="text-cyan-400">{tournament.name}</span>
      </p>
      <p>
        <strong>Typ rozgrywki:</strong>
        <br />
        <span className="text-cyan-400">{tournament.type}</span>
      </p>
      <p>
        <strong>Rozgrywany dodatek:</strong>
        <br />
        <span className="text-cyan-400">{tournament.addon}</span>
      </p>
      <p>
        <strong>Data rozgrywek:</strong>
        <br />
        <span className="text-cyan-400">{tournament.date}</span>
      </p>
      <p>
        <strong>Ilość uczestników:</strong>
        <br />
        <span className="text-cyan-400">{tournament.participants}</span>
      </p>
    </div>
  );
}
