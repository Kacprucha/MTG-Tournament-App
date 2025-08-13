"use client"
import { useSession } from "next-auth/react"

import OuterContainer from "./components/OuterContainer";
import MessageBox from "./components/MessageBox";
import TournamentsGrid from "./components/TournamentsGrid";

function LoginPrompt() {
  return (
    <main className="min-h-screen w-full bg-[#293132] relative">
      <OuterContainer>
        <div className="flex items-center justify-center h-10/12">
          <MessageBox text="Proszę zaloguj się lub zarejestruj się aby przejść dalej"/>
        </div>
      </OuterContainer>
    </main>
  )
}

export default function Home() {
  const { data: session, status } = useSession()

  if (status === "loading") {
    return <p className="text-balance">Sprawdzanie sesji...</p> 
  }

  if (session) {
    return <TournamentsGrid/>
  } else {
    return <LoginPrompt />
  }
}
