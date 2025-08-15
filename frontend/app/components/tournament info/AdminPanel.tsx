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
      <Button text="Edytuj Turniej" onClick={handleEdit} />
      <Button text="Zarządzaj Graczami" onClick={handleManagePlayers} />
      <Button text="Zakończ Turniej" onClick={handleEndTournament} />
    </div>
  );
};

export default AdminPanel;