import { apiRequest } from '@/services/api.service';
import type {
  Commission,
  Payment,
  PaymentPayload,
  SellerBalance,
  Withdrawal,
  WithdrawalPayload,
} from '@/types/payment';

export function processPayment(data: PaymentPayload) {
  return apiRequest<Payment>('/api/payments', { method: 'POST', body: data });
}

export function getPaymentForOrder(orderId: number) {
  return apiRequest<Payment>(`/api/payments/order/${orderId}`);
}

export function getSellerBalance() {
  return apiRequest<SellerBalance>('/api/sellers/me/balance');
}

export function getSellerCommissions() {
  return apiRequest<Commission[]>('/api/sellers/me/commissions');
}

export function getWithdrawals() {
  return apiRequest<Withdrawal[]>('/api/sellers/me/withdrawals');
}

export function requestWithdrawal(data: WithdrawalPayload) {
  return apiRequest<Withdrawal>('/api/sellers/me/withdrawals', { method: 'POST', body: data });
}
