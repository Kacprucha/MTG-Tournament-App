import { TournamentDetails } from "@/types/tournament";
import { useMemo, useState } from "react";

interface TournamentInfoProps {
  tournament: Pick<TournamentDetails, 'name' | 'type' | 'addon' | 'date' | 'participantUsernames' > | null;
  isAdmin?: boolean;
}

export default function TournamentInfo({ tournament, isAdmin }: TournamentInfoProps) {
  const [editableData, setEditableData] = useState({
    name: tournament?.name,
    type: tournament?.type,
    addon: tournament?.addon,
  });

  const participantsCount = useMemo(() => tournament?.participantUsernames.length, [tournament?.participantUsernames]);

  const handleChange = (field: keyof typeof editableData, value: string) => {
    setEditableData((prev) => ({ ...prev, [field]: value }));
  };

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
                value={editableData.name}
                onChange={(e) => handleChange("name", e.target.value)}
              />
            </p>

            {/* Edytowalny typ */}
            <p>
              <strong>Typ rozgrywki:</strong>
              <br />
              <select
                className="bg-gray-700 px-2 py-1 rounded text-white"
                value={editableData.type}
                onChange={(e) => handleChange("type", e.target.value)}
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
                value={editableData.addon}
                onChange={(e) => handleChange("addon", e.target.value)}
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
