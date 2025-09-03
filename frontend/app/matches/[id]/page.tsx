import MatchPageClient from "@/app/components/match/MatchPageClient";
import { Alert } from "antd";

interface MatchPageProps {
  params: Promise<{ id: string }>;
}

export default async function MatchPage({ params }: MatchPageProps) {
  const awaitedParams = await params;
  const id = awaitedParams.id;

  const matchId = parseInt(id, 10);

  if (isNaN(matchId)) {
      return (
      <div className="min-h-screen w-full bg-[#293132] flex items-center justify-center text-white">
        <Alert message="Błąd id" description="Id meczu nie prawidłowe." type="error" showIcon className="mt-4" />
      </div>
    );
  }
  return (
    <div className="min-h-screen w-full bg-[#293132] relative">
      <MatchPageClient matchId={matchId} />
    </div>
  );
}
