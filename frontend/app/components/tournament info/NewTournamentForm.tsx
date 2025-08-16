"use client";

import { useState } from "react";

export default function NewTournamentForm() {
  const [form, setForm] = useState({
    name: "",
    type: "draft",
    addon: "",
  });

  const currentDate = new Date().toISOString().slice(0, 10);

  const handleChange = (field: string, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const payload = {
      ...form,
      date: currentDate,
      participants: [],
    };

    console.log("Nowy turniej:", payload);

    // Tutaj wywołasz POST do API
    // await fetch("/api/tournaments", { method: "POST", body: JSON.stringify(payload) });
  };

  return (
    <form onSubmit={handleSubmit} className="bg-[#1f2425] text-white p-6 rounded-2xl flex flex-col gap-4 w-full max-w-md">
      <h2 className="text-2xl font-bold">Stwórz nowy turniej</h2>

      <label>
        Nazwa turnieju:
        <input
          className="bg-gray-700 px-2 py-1 rounded w-full"
          value={form.name}
          onChange={(e) => handleChange("name", e.target.value)}
        />
      </label>

      <label>
        Typ rozgrywki:
        <select
          className="bg-gray-700 px-2 py-1 rounded w-full"
          value={form.type}
          onChange={(e) => handleChange("type", e.target.value)}
        >
          <option value="draft">Draft</option>
          <option value="sealed">Sealed</option>
          <option value="commander">Commander</option>
          <option value="modern">Modern</option>
        </select>
      </label>

      <label>
        Dodatek:
        <input
          className="bg-gray-700 px-2 py-1 rounded w-full"
          value={form.addon}
          onChange={(e) => handleChange("addon", e.target.value)}
        />
      </label>

      <p>Data: <span className="font-semibold">{currentDate}</span></p>

      <button type="submit" className="px-4 py-1 border border-cyan-400 text-white rounded-full text-sm font-bold hover:bg-cyan-400 hover:text-black transition">
        Stwórz turniej
      </button>
    </form>
  );
}
