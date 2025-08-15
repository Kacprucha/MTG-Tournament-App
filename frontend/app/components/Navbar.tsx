"use client" 

import { useSession, signIn, signOut } from "next-auth/react" 
import { useRouter } from "next/navigation"
import Button from "./Button"
import UserIcon from "./UserIcon"

export default function Navbar() {
  const { data: session, status } = useSession()
  const router = useRouter()

  const userName = session?.user?.name
  const isAdmin = session?.user?.roles?.includes("ADMIN")

  // --- Definicje dynamicznych funkcji dla przycisków ---

  const handleCurrentGameClick = () => {
    router.push("/current-game")
  }

  const handleLiderBoardClick = () => {
    router.push("/leaderboard")
  }

  const handleMyStatsClick = () => {
    if (isAdmin) {
      router.push("/all-stats")
    } else {
      router.push("/my-stats")
    }
  }

  const handleAchivementsClick = () => {
    if (isAdmin) {
      router.push("/achievements-admin")
    } else {
      router.push("/achievements")
    }
  }

  const handleLogoutClick = () => {
    if (session) {
      // const logoutUrl = new URL("http://localhost:8443/realms/app/protocol/openid-connect/logout");
      // logoutUrl.searchParams.set("id_token_hint", session.idToken ? session.idToken : "");
      // logoutUrl.searchParams.set("post_logout_redirect_uri", window.location.origin);
      
      // window.location.href = logoutUrl.toString();
      
      signOut({ redirect: false });
    }
  }

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
    <header className="text-white p-5 sticky top-0 z-50">
      <div className="w-full flex justify-between items-center px-6 md:px-10">
        <div className="flex items-center gap-4">
          {session && (
            <>
              <Button text="Aktualna gra" onClick={handleCurrentGameClick}/>
              <Button text="Tabela wyników" onClick={handleLiderBoardClick}/>
              <Button text="Moje wyniki" onClick={handleMyStatsClick}/>
              <Button text="Osiągnięcia" onClick={handleAchivementsClick}/>
              <Button text="Wyloguj" onClick={handleLogoutClick}/>
            </>
          )}
        </div>

        <div className="flex justify-end items-center">
            {session && (
                <span className="text-xl self-center mr-5">
                    {isAdmin ? "- | Admin" : `${userName}`}
                </span>
            )}
            <UserIcon />
        </div>
      </div>
    </header>
  )
}