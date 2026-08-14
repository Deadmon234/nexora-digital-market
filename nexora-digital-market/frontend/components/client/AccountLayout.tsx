import { Navbar } from '@/components/layout/Navbar';
import { AccountSidebar } from '@/components/client/AccountSidebar';

export function AccountLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <div className="mx-auto flex max-w-6xl flex-col gap-6 px-4 py-8 lg:flex-row">
        <AccountSidebar />
        <div className="flex-1">{children}</div>
      </div>
    </div>
  );
}
