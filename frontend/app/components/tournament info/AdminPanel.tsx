"use client"; 

import React, { useState } from "react";
import Button from "../Button";
import { useSession } from "next-auth/react";
import { message, Modal } from "antd";
import axios, { AxiosError } from "axios";
import { FaExclamationCircle } from "react-icons/fa";
import { TournamentStatus } from "@/types/enums";
import { TournamentDetails } from "@/types/tournament";

interface AdminPanelProps {
  tournamentData: TournamentDetails; 
  onTournamentDeleted: () => void;
  onActionSuccess: () => void;
}

const AdminPanel: React.FC<AdminPanelProps> = ({ tournamentData, onTournamentDeleted, onActionSuccess }) => {
  const { data: session } = useSession();
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [loadingAction, setLoadingAction] = useState<string | null>(null);

  const apiClient = axios.create({
    baseURL: "http://localhost:8080",
    headers: { Authorization: `Bearer ${session?.accessToken}` },
  });

  const handleChangeStatus = async (newStatus: TournamentStatus) => {
    setLoadingAction(newStatus); 
    try {
      await apiClient.put(`/tournaments/${tournamentData.id}/status`, { status: newStatus });
      message.success(`Status turnieju zmieniony na ${newStatus}`);
      onActionSuccess(); 
    } catch (err: unknown) {
      if (err instanceof AxiosError) {
        message.error(err.response?.data?.message || "Nie udało się zmienić statusu.");
      } else {
        message.error("Wystąpił nieoczekiwany błąd.");
      }
    } finally {
      setLoadingAction(null); 
    }
  };

  const handleStartTournament = async () => {
    setLoadingAction(TournamentStatus.IN_PROGRESS);
    try {
      await apiClient.post(`/tournaments/${tournamentData.id}/start`);
      message.success('Nowe mecze zostały pomyślnie wygenerowane!');
      
      onActionSuccess();
    } catch (err: unknown) {
      if (err instanceof AxiosError) {
        message.error(err.response?.data?.message || 'Nie udało się wystartować turnieju.');
      } else {
        message.error("Wystąpił nieoczekiwany błąd.");
      }
    } finally {
      setLoadingAction(null);
    }
  };

  const handlePublishOrUpdate = async () => {
    setLoadingAction('publish');
    
    const payload = {
      id: tournamentData.id,
      name: tournamentData.name,
      type: tournamentData.type,
      addon: tournamentData.addon,
      date: tournamentData.date,
      status: tournamentData.status,
    };

    if (tournamentData.status === TournamentStatus.PENDING) {
      payload.status = TournamentStatus.PUBLISHED; // Zmieniamy status
    }

    try {
      await apiClient.put(`/tournaments/${tournamentData.id}`, payload);
      message.success('Turniej został pomyślnie zaktualizowany!');
      onActionSuccess();
    } catch (err: unknown) {
      if (err instanceof AxiosError) {
        message.error(err.response?.data?.message || "Wystąpił błąd.");
      } else {
        message.error("Wystąpił nieoczekiwany błąd.");
      }
    } finally {
      setLoadingAction(null);
    }
  };

  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };
  
  const handleOk = async () => {
    setIsDeleting(true); 
    
    if (!session?.accessToken) {
      message.error("Brak autoryzacji.");
      setIsDeleting(false);
      setIsModalVisible(false);
      return;
    }

    try {
      await axios.delete(
        `http://localhost:8080/tournaments/${tournamentData.id}`,
        { headers: { Authorization: `Bearer ${session.accessToken}` } }
      );
      
      message.success('Turniej został pomyślnie usunięty!');
      setIsModalVisible(false); // Zamknij modal po sukcesie
      onTournamentDeleted(); // Wywołaj callback

    } catch (err: unknown) {
      console.error("Błąd podczas usuwania turnieju:", err);
      if (err instanceof AxiosError) {
        message.error(err.response?.data?.message || 'Nie udało się usunąć turnieju.');
      } else {
        message.error("Wystąpił nieoczekiwany błąd.");
      }
    } finally {
      setIsDeleting(false); // Wyłącz spinner
    }
  };

  return (
    <>
      <div className="flex gap-4">
        <Button
          text={tournamentData.status === TournamentStatus.PENDING ? "Opublikuj" : "Zapisz Zmiany"}
          onClick={handlePublishOrUpdate}
          // Przekazujemy stany `loading` i `disabled`
          loading={loadingAction === TournamentStatus.PUBLISHED}
          disabled={tournamentData.status === TournamentStatus.FINISHED}
        />
        <Button
          text="Wystartuj"
          onClick={handleStartTournament}
          loading={loadingAction === TournamentStatus.IN_PROGRESS}
          disabled={tournamentData.status !== TournamentStatus.PUBLISHED}
        />
        <Button
          text="Zakończ"
          onClick={() => handleChangeStatus(TournamentStatus.FINISHED)}
          loading={loadingAction === TournamentStatus.FINISHED}
          disabled={tournamentData.status !== TournamentStatus.IN_PROGRESS}
        />
        <Button text="Usuń" onClick={showModal} danger />
      </div>
      <Modal
        title={
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <FaExclamationCircle style={{ color: '#ff4d4f' }} />
            Czy na pewno chcesz usunąć ten turniej?
          </div>
        }
        open={isModalVisible} // Widoczność kontrolowana przez stan
        onOk={handleOk}
        onCancel={handleCancel}
        okText="Tak, usuń"
        cancelText="Anuluj"
        okButtonProps={{ danger: true, loading: isDeleting }} // Przekazujemy stan ładowania
        confirmLoading={isDeleting}
      >
        <p>Ta operacja jest nieodwracalna. Wszystkie powiązane mecze i wyniki zostaną trwale usunięte.</p>
      </Modal>
    </>
  );
};

export default AdminPanel;