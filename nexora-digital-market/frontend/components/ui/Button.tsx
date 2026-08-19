import type { ButtonHTMLAttributes } from 'react';
import { cn } from '@/utils/cn';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline';
}

const variants = {
  primary: 'bg-nexora-blue text-white shadow-sm hover:bg-nexora-blue-dark focus-visible:ring-nexora-blue',
  secondary: 'bg-nexora-green/15 text-nexora-navy hover:bg-nexora-green/25 focus-visible:ring-nexora-green',
  outline: 'border border-nexora-blue/30 bg-white text-nexora-blue hover:bg-nexora-blue/5 focus-visible:ring-nexora-blue',
};

export function Button({
  className,
  variant = 'primary',
  children,
  ...props
}: ButtonProps) {
  return (
    <button
      className={cn(
        'inline-flex items-center justify-center rounded-lg px-4 py-2 text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 disabled:opacity-50',
        variants[variant],
        className,
      )}
      {...props}
    >
      {children}
    </button>
  );
}
