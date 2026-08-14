'use client';

import { StockManager } from '@/components/seller/StockManager';
import { getInventory } from '@/services/seller.service';
import type { InventoryItem } from '@/types/seller';
import { useEffect, useState } from 'react';

export default function SellerInventoryPage() {
  const [items, setItems] = useState<InventoryItem[]>([]);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      setItems(await getInventory());
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Gestion du stock</h1>
      <StockManager items={items} onUpdated={load} />
    </div>
  );
}
