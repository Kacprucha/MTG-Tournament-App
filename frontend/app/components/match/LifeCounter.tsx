"use client";
import { useState } from "react";

export default function LifeCounter() {
  const [life, setLife] = useState(20);

  return (
    <div className="w-full h-32 bg-cyan-400 text-[#474044] rounded-lg p-4 flex justify-between items-center text-6xl font-bold">
      <button
        onClick={() => setLife((prev) => Math.max(prev - 1, 0))}
        className="px-4"
      >
        –
      </button>
      <span>{life}</span>
      <button
        onClick={() => setLife((prev) => prev + 1)}
        className="px-4"
      >
        +
      </button>
    </div>
  );
}
