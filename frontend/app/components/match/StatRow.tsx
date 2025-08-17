"use client";

import React from "react";
import { FaPlus, FaMinus } from "react-icons/fa"; // Używamy ikon z react-icons

// Definiujemy, jakie właściwości (props) będzie przyjmował nasz komponent
interface StatRowProps {
  label: string; // Etykieta, np. "Ilość życia..."
  value: number; // Aktualna wartość liczbowa
  onChange: (newValue: number) => void; // Funkcja wywoływana, gdy wartość się zmienia
}

const StatRow: React.FC<StatRowProps> = ({ label, value, onChange }) => {
  const handleIncrement = () => {
    onChange(value + 1);
  };

  const handleDecrement = () => {
    // Opcjonalnie: zapobiegamy zejściu poniżej zera
    if (value > 0) {
      onChange(value - 1);
    }
  };

  return (
    // Główny kontener używający flexbox do ułożenia etykiety i kontrolki
    <div className="flex items-center justify-between w-full p-4">
      {/* Etykieta po lewej stronie */}
      <span className="text-gray-300 text-lg">{label}</span>

      {/* Kontrolka do zmiany wartości po prawej stronie */}
      <div className="flex items-center border border-cyan-400 rounded-lg py-1 px-3">
        {/* Przycisk minus */}
        <button
          onClick={handleDecrement}
          className="text-white text-xl p-2 transition-transform active:scale-90"
          aria-label="Zmniejsz wartość"
        >
          <FaMinus />
        </button>

        {/* Wyświetlana wartość */}
        <span className="font-bold text-white text-2xl mx-4 min-w-[40px] text-center">
          {value}
        </span>

        {/* Przycisk plus */}
        <button
          onClick={handleIncrement}
          className="text-white text-xl p-2 transition-transform active:scale-90"
          aria-label="Zwiększ wartość"
        >
          <FaPlus />
        </button>
      </div>
    </div>
  );
};

export default StatRow;