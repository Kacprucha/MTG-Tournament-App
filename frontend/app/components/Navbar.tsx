"use client" 

import { useSession, signIn, signOut } from "next-auth/react" 
import { useRouter } from "next/navigation"
import Button from "./Button"
import UserIcon from "./UserIcon"
import { useTournament } from '@/context/TournamentContext';
import Image from "next/image"
import Link from "next/link"
import NavLink from "./NavLink"
import { message } from "antd"
import axios from "axios"

export default function Navbar() {
  const { data: session, status } = useSession()
  const router = useRouter()

  const userName = session?.user?.name
  const isAdmin = session?.user?.roles?.includes("ADMIN")
  const { tournamentName, tournamentId } = useTournament();
  const tournamentNameLable = tournamentName || "-";

  // --- Definicje dynamicznych funkcji dla przycisków ---

  const handleLogoutClick = async  () => {
    if (session?.idToken) {
      const issuerUrl = process.env.NEXT_PUBLIC_KEYCLOAK_ISSUER;
      const clientId = process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID;
      const postLogoutRedirectUri = "http://localhost:3000/logout-callback";

      if (!issuerUrl) {
        signOut({ callbackUrl: postLogoutRedirectUri });
        return;
      }
      
      let logoutUrl = `${issuerUrl}/protocol/openid-connect/logout?post_logout_redirect_uri=${encodeURIComponent(postLogoutRedirectUri)}`;

      if (session?.idToken) {
        logoutUrl += `&id_token_hint=${session.idToken}`;
        
        if (clientId) {
          logoutUrl += `&client_id=${clientId}`;
        }
      }

      window.location.href = logoutUrl;
      
    }
  };

  const handleFindCurrentGame = async () => {
    if (!tournamentId || !session) {
      message.warning("Najpierw wybierz turniej.");
      return;
    }
    try {
      const response = await axios.get(`http://localhost:8080/matches/current?tournamentId=${tournamentId}`, {
        headers: { Authorization: `Bearer ${session.accessToken}` }
      });

      if (response.data) {
        router.push(`/matches/${response.data.id}`);
      } else {
        message.info("Nie masz obecnie żadnych aktywnych meczów.");
      }
    } catch (error) {
      message.error("Błąd podczas wyszukiwania meczu.");
    }
  };

  // --- Renderowanie komponentu ---

  // Nie renderuj nic, dopóki sesja się nie załaduje, aby uniknąć "mrugania"
  if (status === "loading") {
    return (
      <header className="text-white p-4">
        <div className="container mx-auto flex justify-between items-center">
          <div className="text-lg font-bold">MTG App</div>
        </div>
      </header>
    )
  }

  return (
    <header className="bg-[#293132] text-white p-5 sticky top-0 z-50">
      <div className="w-full flex justify-between items-center px-6 md:px-10">
        <div className="flex items-center gap-4">
          {session && (
            <>
              <Link href="/" className="flex items-center gap-2">
                <span className="text-xl font-bold">MTG App</span>
              </Link>
              { isAdmin ? (
                <NavLink href="/matches/admin/dashboard" >Aktualne gry</NavLink>
              ) 
              :
              (
                <Button text="Aktualna gra" onClick={handleFindCurrentGame}/>
              )}
              <NavLink href="/scoreboard">Tabela wyników</NavLink>
              { isAdmin ? (
                <NavLink href="/admin/players">Wyniki graczy</NavLink>
              ) 
              :
              (
                <NavLink href="/my-stats">Moje wyniki</NavLink>
              )}
              <NavLink href="/achievements">Osiągnięcia</NavLink>
              <Button text="Wyloguj" onClick={handleLogoutClick}/>
            </>
          )}
          {!session && (
            <Link href="/" className="flex items-center gap-2">
              <span className="text-xl font-bold">MTG App</span>
            </Link>
          )}
          
        </div>

        <div className="flex justify-end items-center">
            {session && (
                <span className="text-xl self-center mr-5">
                    {isAdmin ? `${tournamentNameLable} | Admin` : `${userName}`}
                </span>
            )}
            <UserIcon />
        </div>
      </div>
    </header>
  )
}