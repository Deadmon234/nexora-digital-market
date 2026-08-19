export function validateEmail(email: string): string | null {
  const trimmed = email.trim();
  if (!trimmed) return 'Email requis';
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(trimmed)) return 'Email invalide';
  return null;
}

export function validatePassword(password: string): string | null {
  if (!password) return 'Mot de passe requis';
  if (password.length < 8) return 'Le mot de passe doit contenir au moins 8 caractères';
  if (!/[a-zA-Z]/.test(password)) return 'Le mot de passe doit contenir au moins une lettre';
  if (!/\d/.test(password)) return 'Le mot de passe doit contenir au moins un chiffre';
  return null;
}

export function validateName(name: string, label: string): string | null {
  const trimmed = name.trim();
  if (!trimmed) return `${label} requis`;
  if (trimmed.length > 100) return `${label} trop long (max 100 caractères)`;
  return null;
}
