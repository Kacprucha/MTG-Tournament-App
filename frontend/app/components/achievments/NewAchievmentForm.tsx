"use client";

import React, { useState } from 'react';
import { useTournament } from '@/context/TournamentContext';
import { useRouter } from 'next/navigation';
import { Alert, Form, Input, message, Select } from 'antd';
import { useSession } from 'next-auth/react';
import axios, { AxiosError } from 'axios';
import { AchievementAggregationType } from '@/types/enums';
import Button from '../Button';

const { Option } = Select;

const NewAchievementForm = () => {
  const { tournamentId } = useTournament(); 
  const { data: session } = useSession();
  const router = useRouter();
  const [form, setForm] = useState({
    name: "",
    price: "",
    aggregationType: AchievementAggregationType.SUM,
  });

  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleChange = (field: keyof typeof form, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!tournamentId || !session?.accessToken) {
      setError("Brak aktywnego turnieju lub autoryzacji.");
      return;
    }

    const payload = {
      name: form.name,
      price: form.price,
      aggregationType: form.aggregationType,
      tournamentId: tournamentId,
    };

    console.log("Wysyłanie nowego osiągnięcia:", payload);

    setSubmitting(true);
    setError(null);

    try {
      await axios.post(
        "http://localhost:8080/achievements",
        payload,
        { headers: { Authorization: `Bearer ${session.accessToken}` } }
      );
      
      message.success('Nowe osiągnięcie zostało pomyślnie dodane!');
      router.push(`/achievements`);
      router.refresh(); 
    } catch (err: unknown) {
      if (err instanceof AxiosError) {
        setError(err.response?.data?.message || "Wystąpił błąd podczas dodawania osiągnięcia.");
      } else {
        setError("Wystąpił nieoczekiwany błąd.");
      }
    } finally {
      setSubmitting(false);
    }
  };

  if (!tournamentId) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Nie wybrano turnieju" description="Musisz najpierw wybrać turniej, aby dodać do niego osiągnięcie." type="warning" />
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className="bg-[#1f2425] text-white p-6 rounded-2xl flex flex-col gap-4 w-full max-w-md">
      <h2 className="text-2xl font-bold text-white self-center">Stwórz nowe osiągnięcie</h2>

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
          value={form.aggregationType}
          onChange={(e) => handleChange("aggregationType", e.target.value)}
        >
          <option value={AchievementAggregationType.SUM}>Suma (SUM)</option>
          <option value={AchievementAggregationType.MAX}>Maksimum (MAX)</option>
          <option value={AchievementAggregationType.MIN}>Minimum (MIN)</option>
        </select>
      </label>

      <Button text={"Dodaj osiągnięcie"} htmlType="submit"/>
    </form>
  );
};

export default NewAchievementForm;