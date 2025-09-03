"use client";

import React, { useState, useEffect } from 'react';
import { useTournament } from '@/context/TournamentContext';
import { useSession } from 'next-auth/react';
import axios from 'axios';
import { Spin, Alert, message } from 'antd';
import { TournamentDetails, ScoreboardEntry, Achievement } from '@/types/tournament';
import Button from '@/app/components/Button';

interface EditPlayerScoreProps {
  username: string;
}

interface FormState {
  points: number;
  achievements: { [achievementId: number]: number };
}

const EditPlayerScoreClientPage = ({username} : EditPlayerScoreProps) => {
  const { tournamentId } = useTournament();
  const { data: session } = useSession();
  
  const [scoreboardEntry, setScoreboardEntry] = useState<ScoreboardEntry | null>(null);
  const [achievementsDef, setAchievementsDef] = useState<Achievement[]>([]);

  const [form, setForm] = useState<FormState>({
    points: 0,
    achievements: {},
  });

  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (tournamentId && session?.accessToken) {
      setLoading(true);
      axios.get<TournamentDetails>(`http://localhost:8080/tournaments/${tournamentId}`, {
        headers: { Authorization: `Bearer ${session.accessToken}` }
      })
      .then(response => {
        const entry = response.data.scoreboard.find(s => s.username === username);
        if (entry) {
          setScoreboardEntry(entry);
          setAchievementsDef(response.data.achievements);
          setForm({
            points: entry.points,
            achievements: entry.achievements,
          });
        }
      })
      .finally(() => setLoading(false));
    }
  }, [tournamentId, session, username]);

  const handlePointsChange = (value: string | number | null) => {
    setForm(prev => ({
      ...prev,
      points: Number(value) || 0, // Zabezpieczenie przed null/undefined
    }));
  };

  const handleAchievementChange = (achievementId: number, value: string | number | null) => {
    setForm(prev => ({
      ...prev,
      achievements: {
        ...prev.achievements,
        [achievementId]: Number(value) || 0,
      },
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!scoreboardEntry) return;

    setSubmitting(true);
    
    const payload = form;
    
    try {
      await axios.put(`http://localhost:8080/scoreboard/${scoreboardEntry.id}`, payload, {
        headers: { Authorization: `Bearer ${session?.accessToken}` }
      });
      message.success("Wyniki zostały zaktualizowane!");
    } catch (err) {
      message.error("Błąd podczas aktualizacji.");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) { 
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Spin size='large' />
      </div>
    );
  }
  if (!scoreboardEntry) { 
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message={`Nie znaleziono wyników dla gracza ${username}.`} type="error" />
      </div>
    );
  }

  return (
    <div className="flex justify-center p-8">
      <form onSubmit={handleSubmit} className="bg-[#1f2425] text-white p-6 rounded-2xl flex flex-col gap-4 w-full max-w-lg">
        <h2 className="text-2xl font-bold text-white self-center">Edytuj wyniki dla: {username}</h2>

        <label>
          Punkty ogólne:
          <input
            type="number"
            step="0.5"
            className="bg-gray-700 px-2 py-1 rounded w-full"
            value={form.points}
            onChange={(e) => handlePointsChange(e.target.valueAsNumber)}
          />
        </label>

        <hr className="border-gray-600" />
        <h3 className="text-lg font-semibold">Osiągnięcia:</h3>

        {achievementsDef.map(ach => (
          <label key={ach.id}>
            {ach.name}:
            <input
              type="number"
              step="0.5"
              className="bg-gray-700 px-2 py-1 rounded w-full"
              value={form.achievements[ach.id] ?? 0}
              onChange={(e) => handleAchievementChange(ach.id, e.target.valueAsNumber)}
            />
          </label>
        ))}

        <Button text={"Zapisz zmiany"} htmlType="submit" loading={submitting} />
      </form>
    </div>
  );
};

export default EditPlayerScoreClientPage;