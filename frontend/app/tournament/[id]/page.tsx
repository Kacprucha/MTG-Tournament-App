import TournamentInfo from "../../components/tournament info/TournamentInfo";
import AchievementsList from "../../components/tournament info/AchievementsList";
import Scoreboard from "../../components/tournament info/Scoreboard";
import OuterContainerHorizontal from "../../components/OuterContainerHorizontal";
import Button from "@/app/components/Button";

// Typ parametru z dynamicznego routa
interface TournamentPageProps {
  params: Promise<{ id: string }>;
}

// Tymczasowy mock – w przyszłości tutaj podłączysz zapytanie do API
const mockTournaments = {
  "1": {
    id: "1",
    name: "Turniej 2",
    type: "draft",
    addon: "dupa",
    date: "02.08.2025",
    participants: 10,
    achievements: [
      "The most amount of flying creatures at any given time",
      "The most amount of mana a player can produce at a given time",
      "The most amount of damage dealt in a single instance",
      "The least amount of turns for a win",
    ],
    scoreboard: [
  { name: "Dupa 123", points: 1237, achievements: { 1: 3, 2: 5, 3: 0, 4: 1 } },
  { name: "Gracz 2", points: 1180, achievements: { 1: 1, 2: 2, 3: 4, 4: 0 } },
],
  },
  "2": {
    id: "2",
    name: "Turniej testowy",
    type: "sealed",
    addon: "beta",
    date: "15.08.2025",
    participants: 8,
    achievements: ["Win in 2 turns", "Most creatures summoned in one turn"],
    scoreboard: [{ name: "Testowy gracz", points: 900, achievements: { 1: 3, 2: 5, 3: 0, 4: 1 } }, ],
  },
};

export default async function Tournament ({ params }: TournamentPageProps) {
  const { id } = await params;
  const tournament = mockTournaments[id as keyof typeof mockTournaments];

  if (!tournament) {
    return (
      <div className="min-h-screen w-full bg-[#293132] flex items-center justify-center text-white">
        <p>Turniej o id {id} nie istnieje.</p>
      </div>
    );
  }
  return (
    <div className="min-h-screen w-full bg-[#293132] relative">
      <OuterContainerHorizontal>
        {/* lewa kolumna */}
        <TournamentInfo tournament={tournament} />
        {/* środek */}
        <AchievementsList achievements={tournament.achievements} />
        {/* prawa kolumna */}
        <Scoreboard scoreboard={tournament.scoreboard} achievements={tournament.achievements}/>
      </OuterContainerHorizontal>
    </div>
  );
}
