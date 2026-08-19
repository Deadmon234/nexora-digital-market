import { Footer } from '@/components/layout/Footer';
import { Navbar } from '@/components/layout/Navbar';

export function MainLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex min-h-screen flex-col bg-nexora-ivory text-nexora-navy">
      <Navbar />
      <main className="flex-1">{children}</main>
      <Footer />
    </div>
  );
}
