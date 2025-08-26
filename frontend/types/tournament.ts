import { TournamentStatus } from "./enums";

interface Participant {
  keycloakId: string | null;
  username: string;
}

export interface ScoreboardEntry {
  id: number;
  userKeycloakId: string | null;
  username: string;
  points: number;
  achievements: { [achievementId: string]: number };
}

export interface Achievement {
  id: number;
  tournamentId: number;
  name: string;
  description?: string;
  price?: string;
  aggregationType: 'SUM' | 'MAX' | 'MIN';
  winnerId?: string;
  winnerUsername?: string;
}

interface MatchSummary {
    id: number;
    round: number;
    status: string;
    participantUsernames: string[];
    winnerUsername?: string;
}

export interface TournamentDetails {
  id: number;
  name: string;
  type: string;
  addon: string;
  date: string;
  status: TournamentStatus;
  isLegacy: boolean;
  participantIds: (string | null)[];
  participantUsernames: string[];
  scoreboard: ScoreboardEntry[];
  matches: MatchSummary[]; 
  achievements: Achievement[];
}