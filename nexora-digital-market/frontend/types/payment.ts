export type PaymentMethod = 'CARD' | 'PAYPAL' | 'BANK_TRANSFER';
export type PaymentStatus = 'PENDING' | 'COMPLETED' | 'FAILED' | 'REFUNDED';
export type WithdrawalStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'PAID';

export interface Payment {
  id: number;
  orderId: number;
  orderNumber: string;
  amount: number;
  method: PaymentMethod;
  status: PaymentStatus;
  transactionRef?: string;
  createdAt: string;
}

export interface PaymentPayload {
  orderId: number;
  method: PaymentMethod;
  cardNumber?: string;
}

export interface SellerBalance {
  availableBalance: number;
  totalEarned: number;
  totalWithdrawn: number;
}

export interface Commission {
  id: number;
  sellerOrderId: number;
  orderNumber: string;
  orderAmount: number;
  commissionRate: number;
  commissionAmount: number;
  sellerAmount: number;
  createdAt: string;
}

export interface Withdrawal {
  id: number;
  amount: number;
  bankAccount: string;
  status: WithdrawalStatus;
  createdAt: string;
  processedAt?: string;
}

export interface WithdrawalPayload {
  amount: number;
  bankAccount: string;
}
