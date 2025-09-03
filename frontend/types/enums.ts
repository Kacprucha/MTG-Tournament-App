export enum TournamentStatus {
  PENDING = 'PENDING',
  PUBLISHED= 'PUBLISHED',
  IN_PROGRESS = 'IN_PROGRESS',
  FINISHED = 'FINISHED',
  CANCELLED = 'CANCELLED',
}

export enum AchievementAggregationType {
  SUM = 'SUM', 
  MAX = 'MAX',
  MIN = 'MIN'
}

export enum MatchStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = "COMPLETED",
  BYE = 'BYE',
  CANCELLED = 'CANCELLED'
}