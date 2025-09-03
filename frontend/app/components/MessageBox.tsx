"use client"
import { signIn } from "next-auth/react"
import Button from "./Button";

interface MessageBoxProps {
  text: string;
}

export default function MessageBox({ text }: MessageBoxProps) {
  // Funkcja, która obsługuje kliknięcie przycisku "Zaloguj się"
  const handleLogin = () => {
    signIn("keycloak")
  }

  // Funkcja, która obsługuje kliknięcie przycisku "Zarejestruj się"
  const handleRegister = () => {
    const registrationUrl = `${process.env.NEXT_PUBLIC_KEYCLOAK_BASE_URL}/realms/${process.env.NEXT_PUBLIC_KEYCLOAK_REALM}/protocol/openid-connect/registrations?client_id=${process.env.NEXT_PUBLIC_KEYCLOAK_ID}&response_type=code&scope=openid%20email&redirect_uri=${encodeURIComponent(window.location.origin)}`
    window.location.href = registrationUrl
  }

  return (
    <div className="m-auto border border-cyan-400 rounded-md p-6 max-w-md text-center">
      <p className="text-white mb-6">
        {text}
      </p>
      <div className="flex gap-4 justify-center">
        <Button text="Zaloguj się" onClick={handleLogin}/>
        <Button text="Zarejestruj się" onClick={handleRegister}/>
      </div>
    </div>
  );
}
