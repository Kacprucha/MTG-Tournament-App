import { MatchStatus } from "./enums";

export interface MatchDetails {
  id: number;
  tournamentId: number;
  tournamentName: string;
  status: MatchStatus;
  type: string;
  round: number;
  tableNumber: number;
  bestOf: number;
  participantIds: string[];
  participantUsernames: string[];
  winnerId?: string;
  winnerUsername?: string;
  gameWinners: string[];
  achievements: { [participantId: string]: { [achievementId: string]: number } };
}