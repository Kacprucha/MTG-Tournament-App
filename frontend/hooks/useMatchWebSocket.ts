"use client";

import { useState, useEffect, useRef } from 'react';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useSession } from 'next-auth/react';

export interface WebSocketMessage {
  type: 'GAME_STARTED' | 'WINNER_SELECTED' | 'GAME_FINISHED_REDIRECT' | 'FINISH_ATTEMPT';
  selectingPlayer?: string;
  chosenWinner?: string;
  reportingPlayerId?: number;
  chosenWinnerUsername?: string;
  reportedStats?: string;
}

interface WebSocketHook {
  isConnected: boolean;
  lastMessage: WebSocketMessage | null;
  sendMessage: (destination: string, body?: any) => void;
}

export const useMatchWebSocket = (matchId: number | null): WebSocketHook => {
  const { data: session } = useSession();
  const clientRef = useRef<Client | null>(null);
  
  const [isConnected, setIsConnected] = useState(false);
  const [lastMessage, setLastMessage] = useState<WebSocketMessage | null>(null);

  useEffect(() => {
    // Łączymy się tylko, jeśli mamy ID meczu
    if (!matchId) {
      return;
    }

    // Jeśli klient już istnieje, nie twórz nowego
    if (clientRef.current) {
      return;
    }

    // Tworzymy klienta STOMP
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-game'),

      connectHeaders: {
        Authorization: `Bearer ${session!.accessToken}`,
      },
      
      // Logi do debugowania
      debug: (str) => {
        console.log(new Date(), str);
      },
      
      // Opcjonalnie: opóźnienie re-konekcji
      reconnectDelay: 5000,
      
      onConnect: () => {
        console.log('Połączono z WebSocket!');
        setIsConnected(true);

        // Subskrybujemy temat dla tego konkretnego meczu
        client.subscribe(`/topic/match/${matchId}`, (message: IMessage) => {
          try {
            const parsedMessage: WebSocketMessage = JSON.parse(message.body);
            setLastMessage(parsedMessage);
          } catch (error) {
            console.error("Błąd parsowania wiadomości WebSocket:", error);
          }
        });
      },
      onDisconnect: () => {
        setIsConnected(false);
        console.log('Rozłączono z WebSocket.');
      },
      onStompError: (frame) => {
        console.error('Błąd brokera STOMP:', frame.headers['message']);
        console.error('Szczegóły:', frame.body);
      },
    });

    // Aktywujemy połączenie
    client.activate();
    clientRef.current = client;

    return () => {
      if (clientRef.current?.active) {
        clientRef.current.deactivate();
      }
      clientRef.current = null;
    };
  }, [matchId, session?.accessToken]);

  // Funkcja do wysyłania wiadomości
  const sendMessage = (destination: string, body: any = {}) => {
    if (clientRef.current?.connected) {
      clientRef.current.publish({
        destination: destination,
        body: JSON.stringify(body),
      });
    } else {
      console.error("Nie można wysłać wiadomości, brak połączenia z WebSocket.");
    }
  };

  return { isConnected, lastMessage, sendMessage };
};