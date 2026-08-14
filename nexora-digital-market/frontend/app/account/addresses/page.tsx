'use client';

import { AddressForm } from '@/components/client/AddressForm';
import { Button } from '@/components/ui/Button';
import {
  createAddress,
  deleteAddress,
  getAddresses,
  setDefaultAddress,
  updateAddress,
} from '@/services/account.service';
import type { Address, AddressPayload } from '@/types/client';
import { useEffect, useState } from 'react';

export default function AccountAddressesPage() {
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState<Address | null>(null);
  const [showForm, setShowForm] = useState(false);

  async function load() {
    setLoading(true);
    try {
      setAddresses(await getAddresses());
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function handleCreate(data: AddressPayload) {
    await createAddress(data);
    setShowForm(false);
    await load();
  }

  async function handleUpdate(data: AddressPayload) {
    if (!editing) return;
    await updateAddress(editing.id, data);
    setEditing(null);
    await load();
  }

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-900">Mes adresses</h1>
        {!showForm && !editing && (
          <Button type="button" onClick={() => setShowForm(true)}>
            Ajouter
          </Button>
        )}
      </div>

      {showForm && (
        <AddressForm
          submitLabel="Ajouter l'adresse"
          onSubmit={handleCreate}
          onCancel={() => setShowForm(false)}
        />
      )}

      {editing && (
        <AddressForm
          initial={editing}
          submitLabel="Mettre à jour"
          onSubmit={handleUpdate}
          onCancel={() => setEditing(null)}
        />
      )}

      {addresses.length === 0 && !showForm ? (
        <p className="text-slate-600">Aucune adresse enregistrée.</p>
      ) : (
        <ul className="space-y-3">
          {addresses.map((address) => (
            <li key={address.id} className="rounded-xl border border-slate-200 bg-white p-4">
              <div className="flex items-start justify-between gap-4">
                <div className="text-sm">
                  <p className="font-medium">
                    {address.label}
                    {address.defaultAddress && (
                      <span className="ml-2 text-xs text-slate-500">(par défaut)</span>
                    )}
                  </p>
                  <p className="mt-1 text-slate-600">
                    {address.street}, {address.postalCode} {address.city}, {address.country}
                  </p>
                </div>
                <div className="flex flex-wrap gap-2">
                  {!address.defaultAddress && (
                    <Button type="button" variant="secondary" onClick={() => setDefaultAddress(address.id).then(load)}>
                      Par défaut
                    </Button>
                  )}
                  <Button type="button" variant="outline" onClick={() => setEditing(address)}>
                    Modifier
                  </Button>
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => deleteAddress(address.id).then(load)}
                  >
                    Supprimer
                  </Button>
                </div>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
