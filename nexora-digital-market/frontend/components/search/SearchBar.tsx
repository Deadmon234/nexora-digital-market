'use client';

import { useRouter } from 'next/navigation';
import { FormEvent, useState } from 'react';
import { cn } from '@/utils/cn';

interface SearchBarProps {
  defaultValue?: string;
  placeholder?: string;
  variant?: 'default' | 'hero';
}

export function SearchBar({
  defaultValue = '',
  placeholder = 'Rechercher un produit...',
  variant = 'default',
}: SearchBarProps) {
  const router = useRouter();
  const [query, setQuery] = useState(defaultValue);
  const isHero = variant === 'hero';

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    const trimmed = query.trim();
    if (trimmed) {
      router.push(`/search?q=${encodeURIComponent(trimmed)}`);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="flex w-full gap-2">
      <input
        type="search"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder={placeholder}
        className={cn(
          'flex-1 rounded-full border px-5 py-3 text-sm focus:outline-none focus:ring-2',
          isHero
            ? 'border-white/20 bg-white text-nexora-navy placeholder:text-slate-500 focus:ring-nexora-green'
            : 'border-slate-300 bg-white focus:border-nexora-blue focus:ring-nexora-blue/20',
        )}
      />
      <button
        type="submit"
        className={cn(
          'rounded-full px-5 py-3 text-sm font-bold transition-colors',
          isHero
            ? 'bg-nexora-coral text-white hover:bg-[#d9553c]'
            : 'bg-nexora-blue text-white hover:bg-nexora-blue-dark',
        )}
      >
        Rechercher
      </button>
    </form>
  );
}
