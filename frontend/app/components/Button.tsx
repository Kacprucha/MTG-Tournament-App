interface ButtonProps {
  text: string;
  onClick?: () => void;
}

export default function Button({ text, onClick }: ButtonProps) {
  return (
    <button
      onClick={onClick}
      className="px-4 py-2 border border-cyan-400 text-white rounded-full text-sm font-bold hover:bg-cyan-400 hover:text-black transition"
    >
      {text}
    </button>
  );
}
