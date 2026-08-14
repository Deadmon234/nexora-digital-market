'use client';

import { DataTable } from '@/components/admin/DataTable';
import { getAdminOrders } from '@/services/admin.service';
import type { AdminOrderSummary } from '@/types/admin';
import { useEffect, useState } from 'react';

export default function AdminOrdersPage() {
  const [orders, setOrders] = useState<AdminOrderSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAdminOrders()
      .then(setOrders)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="text-slate-600">Chargement...</p>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Commandes</h1>
      <DataTable
        data={orders}
        columns={[
          { key: 'orderNumber', label: 'N° commande' },
          { key: 'status', label: 'Statut' },
          { key: 'totalAmount', label: 'Montant', render: (row) => `${row.totalAmount.toFixed(2)} €` },
          { key: 'itemCount', label: 'Articles' },
          { key: 'sellerCount', label: 'Vendeurs' },
          { key: 'createdAt', label: 'Date', render: (row) => new Date(row.createdAt).toLocaleDateString('fr-FR') },
        ]}
      />
    </div>
  );
}
