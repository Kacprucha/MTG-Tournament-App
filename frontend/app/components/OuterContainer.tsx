import { ReactNode } from "react";
import UserIcon from "./UserIcon";

interface OuterContainerProps {
  children: ReactNode;
}

export default function OuterContainer({ children }: OuterContainerProps) {
  return (
    <div
      className="fixed inset-0 border-2 border-cyan-400 rounded-lg "
      style={{
        inset: '80px 60px 41px 60px'
      }}
    >
      {children}
    </div>
  );
}
