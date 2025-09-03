"use client";

import { useState, useEffect } from "react";

interface TimerProps {
  isRunning: boolean; 
}

export default function Timer({ isRunning }: TimerProps) {
  const [time, setTime] = useState(0); // w sekundach

  useEffect(() => {
    let interval: NodeJS.Timeout | null = null;
    
    if (isRunning) {
      interval = setInterval(() => {
        setTime((prev) => prev + 1);
      }, 1000);
    } else {
      setTime(0);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isRunning]);

  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60).toString().padStart(2, "0");
    const secs = (seconds % 60).toString().padStart(2, "0");
    return `${mins}:${secs}`;
  };

  return (
    <div className="w-full h-32 bg-cyan-400 text-[#474044] rounded-lg p-4 text-center content-center text-6xl font-bold">
      {formatTime(time)}
    </div>
  );
}
