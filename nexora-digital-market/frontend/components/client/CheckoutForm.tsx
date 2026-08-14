'use client';

import type { Address } from '@/types/client';
import { formatPrice } from '@/services/catalog.service';
import type { Cart } from '@/types/client';

interface CheckoutFormProps {
  cart: Cart;
  addresses: Address[];
  selectedAddressId: number | null;
  onSelectAddress: (id: number) => void;
  onSubmit: () => Promise<void>;
  loading?: boolean;
}

export function CheckoutForm({
  cart,
  addresses,
  selectedAddressId,
  onSelectAddress,
  onSubmit,
  loading,
}: CheckoutFormProps) {
  return (
    <div className="space-y-6">
      <div className="rounded-2xl border border-slate-200 bg-white p-6">
        <h2 className="text-lg font-semibold text-slate-900">Adresse de livraison</h2>
        {addresses.length === 0 ? (
          <p className="mt-2 text-sm text-slate-600">
            Aucune adresse enregistrée. Ajoutez-en une dans votre compte.
          </p>
        ) : (
          <ul className="mt-4 space-y-2">
            {addresses.map((address) => (
              <li key={address.id}>
                <label className="flex cursor-pointer items-start gap-3 rounded-lg border border-slate-200 p-3">
                  <input
                    type="radio"
                    name="address"
                    checked={selectedAddressId === address.id}
                    onChange={() => onSelectAddress(address.id)}
                  />
                  <span className="text-sm">
                    <span className="font-medium">{address.label}</span>
                    {address.defaultAddress && (
                      <span className="ml-2 text-xs text-slate-500">(par défaut)</span>
                    )}
                    <br />
                    {address.street}, {address.postalCode} {address.city}, {address.country}
                  </span>
                </label>
              </li>
            ))}
          </ul>
        )}
      </div>

      <div className="rounded-2xl border border-slate-200 bg-white p-6">
        <h2 className="text-lg font-semibold text-slate-900">Récapitulatif</h2>
        <p className="mt-2 text-sm text-slate-600">{cart.itemCount} article(s)</p>
        <p className="mt-1 text-xl font-bold">{formatPrice(cart.totalAmount)}</p>
        <button
          type="button"
          disabled={loading || !selectedAddressId || cart.itemCount === 0}
          onClick={() => onSubmit()}
          className="mt-4 w-full rounded-lg bg-slate-900 px-4 py-2 text-sm font-medium text-white disabled:opacity-50"
        >
          {loading ? 'Traitement...' : 'Continuer vers le paiement'}
        </button>
        <p className="mt-3 text-xs text-slate-500">
          Le paiement en ligne sera disponible en Phase 7.
        </p>
      </div>
    </div>
  );
}
