export default function ControlButtons() {
  return (
    <div className="flex flex-col gap-3 text-center">
      <button className="px-4 py-2 rounded-full border-2 border-cyan-400 text-cyan-400 hover:bg-cyan-400 hover:text-black transition">
        START
      </button>
      <button className="px-4 py-2 rounded-full border-2 border-blue-400 text-blue-400 hover:bg-blue-400 hover:text-black transition">
        PAUZA
      </button>
      <button className="px-4 py-2 rounded-full border-2 border-red-400 text-red-400 hover:bg-red-400 hover:text-black transition">
        KONIEC
      </button>
    </div>
  );
}
