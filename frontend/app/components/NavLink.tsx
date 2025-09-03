"use client";

import Link from 'next/link'; 
import { usePathname } from 'next/navigation'; 
import React from 'react';

interface NavLinkProps {
  href: string; 
  children: React.ReactNode; 
}

const NavLink: React.FC<NavLinkProps> = ({ href, children }) => {
  const pathname = usePathname();

  const isActive = pathname === href;

  const baseClasses = "px-4 py-2 border border-cyan-400 rounded-full text-sm font-bold transition";
  const activeClasses = "bg-cyan-400 text-black"; 
  const hoverClasses = "hover:bg-cyan-400 hover:text-black"; 

  const finalClasses = `
    ${baseClasses}
    ${isActive ? activeClasses : hoverClasses}
  `;

  return (
    <Link href={href} className={finalClasses.trim()}>
      {children}
    </Link>
  );
};

export default NavLink;