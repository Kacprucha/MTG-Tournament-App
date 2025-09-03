"use client";

import NewAchievementForm from "@/app/components/achievments/NewAchievmentForm";
import Alert from "antd/es/alert/Alert";
import { useSession } from "next-auth/react";

export default function NewAchievementPage () {
  const { data: session } = useSession();

  const isAdmin = session?.user?.roles?.includes("ADMIN");

  if (!isAdmin) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Brak uprawnień" description="Tylko administratorzy mogą tworzyć nowe turnieje." type="error" showIcon className="mt-4" />
      </div>
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center">
      <NewAchievementForm />
    </div>
  );
}
