import { loadTossPayments } from '@tosspayments/payment-sdk';

export default function Page() {
  const payShot = async () => {
    const tossPayments = await loadTossPayments(
      process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY,
    ); // 회원 결제

    await tossPayments.requestPayment('카드', {
      amount: 15000,
      orderId: Math.random().toString(36).slice(2),
      orderName: 'DevDay 참가비',
      successUrl: `${window.location.origin}/challenge/success`,
      failUrl: `${window.location.origin}/challenge/fail`,
      windowTarget: 'self',
    });
  };

  return (
    <div>
      <h3>결제 예시화면</h3>
      <button type="button" className="btn btn-primary" onClick={payShot}>
        카드결제
      </button>
    </div>
  );
}
