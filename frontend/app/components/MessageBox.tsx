import Button from "./Button";

interface MessageBoxProps {
  text: string;
}

export default function MessageBox({ text }: MessageBoxProps) {
  return (
    <div className="m-auto border border-cyan-400 rounded-md p-6 max-w-md text-center">
      <p className="text-white mb-6">
        {text}
      </p>
      <div className="flex gap-4 justify-center">
        <Button text="Zaloguj się" />
        <Button text="Zarejestruj się" />
      </div>
    </div>
  );
}
