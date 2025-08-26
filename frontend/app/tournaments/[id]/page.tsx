import TournamentPageClient from "@/app/components/tournament info/TournamentPageClient";
import Alert from "antd/es/alert/Alert";
import { Suspense } from "react";

// Typ parametru z dynamicznego routa
interface TournamentPageProps {
  params: { id: string };
}

export default async function Tournament ({ params }: TournamentPageProps) {
  const awaitedParams = await params;
  const id = awaitedParams.id;

  const tournamentId = parseInt(id, 10);
  
  if (isNaN(tournamentId)) {
      return (
      <div className="min-h-screen w-full bg-[#293132] flex items-center justify-center text-white">
        <Alert message="Błąd id" description="Id turnieju nie prawidłowe." type="error" showIcon className="mt-4" />
      </div>
    );
  }

  return (
    <Suspense fallback={<div>Ładowanie turnieju...</div>}>
      <TournamentPageClient tournamentId={tournamentId} />
    </Suspense>
  );
}
