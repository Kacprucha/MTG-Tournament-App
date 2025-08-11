import { ReactNode } from "react";

interface OuterContainerProps {
  children: ReactNode;
}

export default function OuterContainer({ children }: OuterContainerProps) {
  return (
    <div
      className="absolute border border-cyan-400 rounded-lg flex flex-col"
      style={{
        top: "80px",
        bottom: "41px",
        left: "60px",
        right: "60px",
      }}
    >
      {children}
    </div>
  );
}
