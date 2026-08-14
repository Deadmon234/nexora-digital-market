import './globals.css';
import { Providers } from '@/components/providers/Providers';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Nexora Digital Market',
  description: 'Plateforme e-commerce multi-vendeurs',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="fr">
      <body>
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
