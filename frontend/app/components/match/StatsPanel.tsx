import { useEffect, useState } from "react";
import StatRow from "./StatRow";
import { useSession } from "next-auth/react";
import { Alert, Spin } from "antd";

interface StatDefinition {
  id: string;
  label: string;
}
interface StatValues {
  [statId:string]: number;
}

export default function StatsPanel() {
  const { data: session } = useSession();

  const [statDefinitions, setStatDefinitions] = useState<StatDefinition[]>([]);
  const [statValues, setStatValues] = useState<StatValues>({});
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStatDefinitions = async () => {
      // Symulacja pobierania danych z backendu
      try {
        setLoading(true);
        // ------ MOCKOWE DANE ------
        const mockDefinitions: StatDefinition[] = [
          { id: "achiv_1", label: "Ilość życia zdobytego w danej grze" },
          { id: "achiv_2", label: "Ilość stowrzonych tokenów w danej grze" },
          { id: "achiv_3", label: "Ilość poświęconych jednostek w danej grze"},
        ];
        // Gdy backend będzie gotowy, zastąpisz to wywołaniem axios
        // const response = await axios.get("/api/match/stat-definitions", { headers: ... });
        // const definitions = response.data;
        
        setStatDefinitions(mockDefinitions);
        
        // Inicjalizacja stanu wartości na podstawie pobranych definicji
        const initialValues = mockDefinitions.reduce((acc, stat) => {
          acc[stat.id] = 0;
          return acc;
        }, {} as StatValues);
        setStatValues(initialValues);

      } catch (err) {
        setError("Nie udało się pobrać konfiguracji statystyk.");
      } finally {
        setLoading(false);
      }
    };

    fetchStatDefinitions();
  }, []);

  const handleStatChange = (statId: string, newValue: number) => {
    setStatValues(prevValues => ({
      ...prevValues, // Skopiuj istniejące wartości
      [statId]: newValue, // Zaktualizuj wartość dla konkretnej statystyki
    }));
  };

  const handleSubmit = async () => {
    console.log("Wysyłanie danych na backend:", statValues);
    try {
      // UWAGA: Zastąp ten URL i logikę swoim prawdziwym endpointem
      /*
      if (!session?.accessToken) {
        throw new Error("Brak autoryzacji");
      }
      await axios.post("/api/match/results", statValues, {
        headers: {
          Authorization: `Bearer ${session.accessToken}`,
        },
      });
      */
      alert("Dane zapisane pomyślnie!");
    } catch (err) {
      console.error("Błąd podczas zapisywania danych:", err);
      alert("Wystąpił błąd podczas zapisu.");
    }
  };

  if (loading) {
    return <Spin tip="Wczytywanie formularza..." />;
  }
  if (error) {
    return <Alert message={error} type="error" />;
  }

  return (
    <div className="border-2 border-cyan-400 rounded-lg p-4 text-white flex flex-col gap-3">
      {statDefinitions.map(stat => (
          <StatRow
            key={stat.id}
            label={stat.label}
            value={0} // Użyj wartości ze stanu (lub 0, jeśli nie istnieje)
            // Użyj partial application, aby przekazać stat.id do handlera
            onChange={(newValue) => handleStatChange(stat.id, newValue)}
          />
        ))}
    </div>
  );
}
