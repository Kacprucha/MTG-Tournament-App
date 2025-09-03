import React from 'react';
import { FaCrown } from 'react-icons/fa'; // Importujemy ikonę korony

interface WinProgressRowProps {
  playerName: string;
  wins: number;
}

const WinProgressRow: React.FC<WinProgressRowProps> = ({ playerName, wins }) => {
  return (
    <div className="flex items-center gap-4"> {/* Używamy flexbox z odstępem */}
      {/* Nazwa gracza */}
      <span className="text-gray-300 text-lg w-32">{playerName}</span>
      
      {/* Kontener na korony */}
      <div className="flex items-center gap-2">
        {Array.from({ length: wins }).map((_, index) => (
          <FaCrown key={index} className="text-yellow-400 text-2xl" />
        ))}
      </div>
    </div>
  );
};

export default WinProgressRow;