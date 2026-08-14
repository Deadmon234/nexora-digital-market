'use client';

import type { InventoryItem } from '@/types/seller';
import { addStock, decreaseStock } from '@/services/seller.service';
import { useState } from 'react';
import { Button } from '@/components/ui/Button';

interface StockManagerProps {
  items: InventoryItem[];
  onUpdated: () => void;
}

export function StockManager({ items, onUpdated }: StockManagerProps) {
  const [quantities, setQuantities] = useState<Record<number, number>>({});
  const [loadingId, setLoadingId] = useState<number | null>(null);

  async function handleAdjust(offerId: number, type: 'add' | 'decrease') {
    const quantity = quantities[offerId] ?? 1;
    setLoadingId(offerId);
    try {
      if (type === 'add') {
        await addStock(offerId, quantity);
      } else {
        await decreaseStock(offerId, quantity);
      }
      onUpdated();
    } finally {
      setLoadingId(null);
    }
  }

  if (items.length === 0) {
    return <p className="text-slate-600">Aucun produit en stock.</p>;
  }

  return (
    <div className="space-y-3">
      {items.map((item) => (
        <div
          key={item.offerId}
          className="flex flex-col gap-3 rounded-xl border border-slate-200 bg-white p-4 sm:flex-row sm:items-center sm:justify-between"
        >
          <div>
            <p className="font-medium text-slate-900">{item.productName}</p>
            <p className={`text-sm ${item.lowStock ? 'text-red-600' : 'text-slate-500'}`}>
              Stock : {item.stock} {item.lowStock && '(faible)'}
            </p>
          </div>
          <div className="flex items-center gap-2">
            <input
              type="number"
              min="1"
              value={quantities[item.offerId] ?? 1}
              onChange={(e) =>
                setQuantities({ ...quantities, [item.offerId]: Number(e.target.value) })
              }
              className="w-20 rounded-lg border border-slate-300 px-2 py-1 text-sm"
            />
            <Button
              type="button"
              variant="secondary"
              disabled={loadingId === item.offerId}
              onClick={() => handleAdjust(item.offerId, 'add')}
            >
              +
            </Button>
            <Button
              type="button"
              variant="outline"
              disabled={loadingId === item.offerId}
              onClick={() => handleAdjust(item.offerId, 'decrease')}
            >
              −
            </Button>
          </div>
        </div>
      ))}
    </div>
  );
}
