/** Retire les balises HTML et caractères de contrôle d'une chaîne utilisateur. */
export function sanitizeText(input: string): string {
  return input
    .replace(/<[^>]*>/g, '')
    .replace(/[\x00-\x08\x0B\x0C\x0E-\x1F]/g, '')
    .trim();
}

export function sanitizeEmail(email: string): string {
  return email.toLowerCase().trim();
}
