import type { ReactNode } from 'react';

export function Card({ title, children }: { title?: string; children: ReactNode }) {
  return (
    <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
      {title ? <h2 className="text-lg font-semibold text-slate-900">{title}</h2> : null}
      <div className={title ? 'mt-3' : undefined}>{children}</div>
    </section>
  );
}
