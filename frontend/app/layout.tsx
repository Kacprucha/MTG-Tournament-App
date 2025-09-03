import type { Metadata } from "next";
import { Geist, Geist_Mono, Kanit } from "next/font/google";
import { AntdRegistry } from '@ant-design/nextjs-registry'; 
import { App, ConfigProvider } from 'antd';
import plPL from 'antd/locale/pl_PL';
import "./globals.css";
import Providers from "./components/Providers"
import Navbar from "./components/Navbar";
import { TournamentProvider } from "@/context/TournamentContext";
import { SessionWatcher } from "./components/SessionWatcher";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

const kanit = Kanit({
  variable: "--font-family-kanit",
  subsets: ["latin"],
  weight: ["400", "500", "600", "700"],
});

export const metadata: Metadata = {
  title: "MTG Tournament App",
  description: "A web application for managing Magic: The Gathering tournaments",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${geistSans.variable} ${geistMono.variable} antialiased`}>
        <AntdRegistry>
          <ConfigProvider locale={plPL}>
            <Providers> 
              <TournamentProvider>
                <SessionWatcher />
                <Navbar />
                <main>{children}</main> 
              </TournamentProvider>
            </Providers>
          </ConfigProvider>
        </AntdRegistry>
      </body>
    </html>
  );
}
