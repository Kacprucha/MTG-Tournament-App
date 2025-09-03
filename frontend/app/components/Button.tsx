"use client";

import React from 'react';
import { Spin } from 'antd';
import clsx from 'clsx';

interface ButtonProps {
  text: string;
  onClick?: () => void;
  disabled?: boolean;
  loading?: boolean;
  danger?: boolean; 
  htmlType?: 'button' | 'submit' | 'reset'; 
}

export default function Button({ text, onClick, disabled = false, loading = false, danger = false, htmlType = 'button' }: ButtonProps) {
  const baseClasses = "px-4 py-2 rounded-full text-sm font-bold transition-colors duration-200 flex items-center justify-center gap-2";
  
  const variantClasses = clsx({
    // Style dla przycisku standardowego
    'border border-cyan-400 text-white hover:bg-cyan-400 hover:text-black': !danger && !disabled,
    // Style dla przycisku "danger"
    'border border-red-500 text-red-500 hover:bg-red-500 hover:text-white': danger && !disabled,
    // Style dla przycisku wyłączonego
    'border border-gray-600 text-gray-500 bg-gray-800 cursor-not-allowed': disabled,
  });

  return (
    <button
      type={htmlType}
      onClick={onClick}
      className={clsx(baseClasses, variantClasses)}
      disabled={disabled || loading}
    >
      {loading && <Spin size="small" />}
      
      <span>{text}</span>
    </button>
  );
}
