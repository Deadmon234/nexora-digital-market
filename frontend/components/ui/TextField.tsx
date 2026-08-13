import type { InputHTMLAttributes } from 'react';

interface TextFieldProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
}

export function TextField({ label, error, id, ...props }: TextFieldProps) {
  const inputId = id ?? props.name;
  return (
    <div className="flex flex-col gap-1.5">
      <label htmlFor={inputId} className="text-sm font-medium text-slate-700">
        {label}
      </label>
      <input
        id={inputId}
        {...props}
        className="rounded-xl border border-slate-300 px-4 py-2.5 text-sm outline-none focus:border-slate-900"
      />
      {error ? <p className="text-sm text-red-600">{error}</p> : null}
    </div>
  );
}
