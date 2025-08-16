"use client"; 

import React from "react";
import Button from "../Button";

const AdminPanel = () => {
  const handleEdit = () => {
    console.log("Edytuj Turniej");
  };

  const handleManagePlayers = () => {
    console.log("Zarządzaj Graczami");
  };

  const handleEndTournament = () => {
    console.log("Zakończ Turniej");
  };

  return (
    <div className="flex gap-4">
      <Button text="Opublikuj" onClick={handleEdit} />
      <Button text="Wystartuj" onClick={handleManagePlayers} />
      <Button text="Zakończ" onClick={handleEndTournament} />
    </div>
  );
};

export default AdminPanel;