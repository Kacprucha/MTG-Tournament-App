import { ReactNode } from "react";

interface OuterContainerProps {
  children: ReactNode;
  footer?: ReactNode;
}

export default function OuterContainerHorizontal({ children, footer }: OuterContainerProps) {
  return (
    <div
      className="fixed border-2 border-cyan-400 rounded-lg grid grid-cols-[250px_1fr_300px] gap-4 p-4"
      style={{
        inset: "80px 60px 41px 60px",
      }}
    >
      {children}
    </div>
  );
}
