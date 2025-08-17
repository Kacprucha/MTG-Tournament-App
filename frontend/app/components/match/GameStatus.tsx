interface GameStatusProps {
  status: string;
}

export default function GameStatus({ status }: GameStatusProps) {
  return (
    <div className="border-2 border-cyan-400 rounded-lg p-4 text-white">
      <div className="font-semibold">Status rozgrywki:</div>
      <div className="mt-2 px-2 py-1 bg-cyan-400 text-black rounded-full text-center text-sm">
        {status}
      </div>
    </div>
  );
}
