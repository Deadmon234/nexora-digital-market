import './globals.css';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Nexora Digital Market',
  description: 'Plateforme e-commerce multi-vendeurs',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="fr">
      <body>{children}</body>
    </html>
  );
}
