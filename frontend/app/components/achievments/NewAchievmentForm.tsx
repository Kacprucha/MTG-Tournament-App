"use client";

import React, { useState } from 'react';
import { useTournament } from '@/context/TournamentContext';



const NewAchievementForm = () => {
  const { tournamentId } = useTournament(); 
  const [form, setForm] = useState({
    name: "",
    price: "",
    type: "sum",
  });

  const handleChange = (field: string, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const payload = {
      ...form,
      tournamentId: tournamentId,
    };

    console.log("Nowe osiągnięcie:", payload);

    // Tutaj wywołasz POST do API
    // await fetch("/api/achivments", { method: "POST", body: JSON.stringify(payload) });
  };

  return (
    <form onSubmit={handleSubmit} className="bg-[#1f2425] text-white p-6 rounded-2xl flex flex-col gap-4 w-full max-w-md">
      <h2 className="text-2xl font-bold">Stwórz nowe osiągnięcie</h2>

      <label>
        Nazwa osiągnięcia:
        <input
          className="bg-gray-700 px-2 py-1 rounded w-full"
          value={form.name}
          onChange={(e) => handleChange("name", e.target.value)}
        />
      </label>

      <label>
        Nagroda:
        <input
          className="bg-gray-700 px-2 py-1 rounded w-full"
          value={form.price}
          onChange={(e) => handleChange("price", e.target.value)}
        />
      </label>

      <label>
        Typ zaliczenia:
        <select
          className="bg-gray-700 px-2 py-1 rounded w-full"
          value={form.type}
          onChange={(e) => handleChange("type", e.target.value)}
        >
          <option value="draft">sum</option>
          <option value="sealed">max</option>
        </select>
      </label>

      <button type="submit" className="px-4 py-1 border border-cyan-400 text-white rounded-full text-sm font-bold hover:bg-cyan-400 hover:text-black transition">
        Dodaj osiągnięcie
      </button>
    </form>
  );
};

export default NewAchievementForm;