export default function Page() {
  return (
    <div>
      <h3>결제 실패</h3>
      <p>
        결제 실패화면입니다. 결제 실패에 대해 error코드를 화면에 노출합니다.
        자세한 정보는{' '}
        <a href="https://docs.tosspayments.com/reference/error-codes#failurl%EB%A1%9C-%EC%A0%84%EB%8B%AC%EB%90%98%EB%8A%94-%EC%97%90%EB%9F%AC">
          토스 페이먼트 실패 처리
        </a>{' '}
        를 참고해주세요.
      </p>
    </div>
  );
}
