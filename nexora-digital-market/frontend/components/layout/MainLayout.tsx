import { Navbar } from '@/components/layout/Navbar';

export function MainLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-gray-50 text-gray-900">
      <Navbar />
      <main>{children}</main>
    </div>
  );
}
