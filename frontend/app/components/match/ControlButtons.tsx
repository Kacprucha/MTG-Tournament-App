import { MatchStatus } from "@/types/enums";
import Button from "../Button";

interface ControlButtonsProps {
  onStartClick:() => void;
  onEndClick:() => void;
  matchStatus: MatchStatus;
}

export default function ControlButtons({onStartClick, onEndClick, matchStatus} : ControlButtonsProps) {
  return (
    <div className="flex flex-col gap-3 text-center">
      <Button text="START" onClick={onStartClick} disabled={matchStatus !== MatchStatus.PENDING}/>
      <Button text="KONIEC" onClick={onEndClick} danger disabled={matchStatus !== MatchStatus.IN_PROGRESS}/>
    </div>
  );
}
