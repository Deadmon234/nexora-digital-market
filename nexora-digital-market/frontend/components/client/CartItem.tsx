'use client';

import type { CartItem as CartItemType } from '@/types/client';
import { formatPrice } from '@/services/catalog.service';
import Link from 'next/link';
import { Button } from '@/components/ui/Button';

interface CartItemProps {
  item: CartItemType;
  onUpdateQuantity: (itemId: number, quantity: number) => void;
  onRemove: (itemId: number) => void;
  loading?: boolean;
}

export function CartItemRow({ item, onUpdateQuantity, onRemove, loading }: CartItemProps) {
  return (
    <div className="flex flex-col gap-4 rounded-xl border border-slate-200 bg-white p-4 sm:flex-row sm:items-center">
      <div className="flex flex-1 gap-4">
        {item.imageUrl ? (
          // eslint-disable-next-line @next/next/no-img-element
          <img src={item.imageUrl} alt={item.productName} className="h-20 w-20 rounded-lg object-cover" />
        ) : (
          <div className="flex h-20 w-20 items-center justify-center rounded-lg bg-slate-100 text-xs text-slate-400">
            N/A
          </div>
        )}
        <div>
          <Link href={`/products/${item.productSlug}`} className="font-medium text-slate-900 hover:underline">
            {item.productName}
          </Link>
          <p className="text-sm text-slate-500">{item.sellerName}</p>
          <p className="mt-1 text-sm font-medium">{formatPrice(item.unitPrice)}</p>
        </div>
      </div>
      <div className="flex items-center gap-3">
        <input
          type="number"
          min="1"
          max={item.availableStock}
          value={item.quantity}
          disabled={loading}
          onChange={(e) => onUpdateQuantity(item.id, Number(e.target.value))}
          className="w-16 rounded-lg border border-slate-300 px-2 py-1 text-sm"
        />
        <span className="min-w-[80px] text-right font-semibold">{formatPrice(item.lineTotal)}</span>
        <Button type="button" variant="outline" disabled={loading} onClick={() => onRemove(item.id)}>
          Retirer
        </Button>
      </div>
    </div>
  );
}
