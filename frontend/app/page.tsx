import Image from "next/image";

import OuterContainer from "./components/OuterContainer";
import UserIcon from "./components/UserIcon";
import MessageBox from "./components/MessageBox";

export default function Home() {
  return (
    <main className="min-h-screen w-full bg-[#293132] relative">
      <div className="flex justify-end mr-5 mt-5">
          <UserIcon />
      </div>
      <OuterContainer>
        <div className="flex flex-1 items-center justify-center">
          <MessageBox text="Proszę zaloguj się lub zarejestruj się aby przejść dalej"/>
        </div>
      </OuterContainer>
    </main>
  );
}
