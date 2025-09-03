import React from 'react';

interface StatDisplayRowProps {
  label: string;
  value: number | string;
}

const StatDisplayRow: React.FC<StatDisplayRowProps> = ({ label, value }) => {
  return (
    <div className="flex items-center justify-between w-full">
      <span className="text-gray-300 text-lg">{label}</span>
      <div className="bg-cyan-500 text-white font-bold rounded-lg px-6 py-2 min-w-[100px] text-center">
        {value}
      </div>
    </div>
  );
};

export default StatDisplayRow;