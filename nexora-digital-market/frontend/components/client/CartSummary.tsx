import { formatPrice } from '@/services/catalog.service';
import Link from 'next/link';
import { Button } from '@/components/ui/Button';

interface CartSummaryProps {
  itemCount: number;
  totalAmount: number;
  checkoutHref?: string;
}

export function CartSummary({ itemCount, totalAmount, checkoutHref = '/checkout' }: CartSummaryProps) {
  return (
    <div className="rounded-2xl border border-slate-200 bg-white p-6">
      <h2 className="text-lg font-semibold text-slate-900">Récapitulatif</h2>
      <div className="mt-4 space-y-2 text-sm">
        <div className="flex justify-between">
          <span className="text-slate-600">Articles</span>
          <span>{itemCount}</span>
        </div>
        <div className="flex justify-between text-base font-bold">
          <span>Total</span>
          <span>{formatPrice(totalAmount)}</span>
        </div>
      </div>
      {itemCount > 0 && (
        <Link href={checkoutHref} className="mt-6 block">
          <Button className="w-full">Passer commande</Button>
        </Link>
      )}
    </div>
  );
}
