import { TournamentDetails } from "@/types/tournament";
import { useMemo, useState } from "react";

interface TournamentInfoProps {
  tournament: Pick<TournamentDetails, 'name' | 'type' | 'addon' | 'date' | 'participantUsernames' > | null;
  isAdmin?: boolean;
  onFieldChange: (field: keyof TournamentDetails, value: string) => void; 
}

export default function TournamentInfo({ tournament, isAdmin, onFieldChange }: TournamentInfoProps) {

  const participantsCount = useMemo(() => tournament?.participantUsernames.length, [tournament?.participantUsernames]);

  return (
    <div className="border border-cyan-400 rounded-md p-4 text-white flex flex-col gap-2">
        {isAdmin ? (
          <>
            {/* Edytowalna nazwa */}
            <p>
              <strong>Nazwa turnieju:</strong>
              <br />
              <input
                className="bg-gray-700 px-2 py-1 rounded text-white"
                value={tournament?.name}
                onChange={(e) => onFieldChange('name', e.target.value)}
                placeholder="Nazwa turnieju"
              />
            </p>

            {/* Edytowalny typ */}
            <p>
              <strong>Typ rozgrywki:</strong>
              <br />
              <select
                className="bg-gray-700 px-2 py-1 rounded text-white"
                value={tournament?.type}
                onChange={(e) => onFieldChange("type", e.target.value)}
              >
                <option value="draft">Draft</option>
                <option value="sealed">Sealed</option>
                <option value="commander">Commander</option>
                <option value="modern">Modern</option>
              </select>
            </p>

            {/* Edytowalny dodatek */}
            <p>
              <strong>Rozgrywany dodatek:</strong>
              <br />
              <input
                className="bg-gray-700 px-2 py-1 rounded text-white"
                value={tournament?.addon}
                onChange={(e) => onFieldChange('addon', e.target.value)}
                placeholder="Dodatek"
              />
            </p>
          </>
        ) : (
        <>
          <p>
            <strong>Nazwa turnieju:</strong>
            <br />
            <span className="text-cyan-400">{tournament?.name}</span>
          </p>
          <p>
            <strong>Typ rozgrywki:</strong>
            <br />
            <span className="text-cyan-400">{tournament?.type}</span>
          </p>
          <p>
            <strong>Rozgrywany dodatek:</strong>
            <br />
            <span className="text-cyan-400">{tournament?.addon}</span>
          </p>
        </>
        )}

        <p>
         <strong>Data rozgrywek:</strong>
         <br />
         <span className="text-cyan-400">{tournament?.date}</span>
       </p>
       <p>
         <strong>Ilość uczestników:</strong>
         <br />
         <span className="text-cyan-400">{participantsCount}</span>
       </p>
    </div>
  );
}
