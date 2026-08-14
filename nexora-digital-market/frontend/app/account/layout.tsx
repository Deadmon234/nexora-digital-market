import { AccountLayout } from '@/components/client/AccountLayout';

export default function AccountRootLayout({ children }: { children: React.ReactNode }) {
  return <AccountLayout>{children}</AccountLayout>;
}
