import React from "react";
import Image from "next/image";
import "./TournamentCard.css";
import Button from "./Button";

interface TournamentCardProps {
  title: string;
  type: string;
  imageFile: string;
  onButtonClick: () => void;
}

const TournamentCard: React.FC<TournamentCardProps> = ({ title, type, imageFile, onButtonClick }: TournamentCardProps) => {
  return (
    <div className="tournament-card">
      <div className="tournament-header">
        <h3 className="tournament-title">{title}</h3>
        <p className="tournament-type">{type}</p>
      </div>

      <div className="tournament-image-wrapper">
        <Image priority={true}
          src={`/images/tournaments/${imageFile}`}
          alt={title}
          width={300}
          height={180}
          className="tournament-image"
        />
      </div>

      <div className="tournament-footer">
        <Button text="Zobacz szczegóły" onClick={onButtonClick}/>
      </div>
    </div>
  );
};

export default TournamentCard;
