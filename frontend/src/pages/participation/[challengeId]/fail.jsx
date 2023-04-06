import Link from 'next/link';

import { Button } from '@/components/Button';
import Container from '@/components/Container';

const fail = ({ challengeId, code, message }) => {
  return (
    <Container>
      <Container.SubPageHeader title="결제 실패" disableBefore />
      <Container.MainBody>
        <div>
          <h2 className="text-2xl font-bold my-8">결제에 실패했습니다.</h2>
          <p className="text-lg">{message}</p>
          <p className="text-sm my-4">CODE: {code}</p>
        </div>
      </Container.MainBody>
      <Container.MainFooter className="px-4 py-4">
        <div className="gap-2 grid grid-cols-2">
          <Link href="/">
            <Button color="danger" label="메인으로" />
          </Link>
          <Link href={`/participation/${challengeId}/pay`}>
            <Button label="다시 결제하기" />
          </Link>
        </div>
      </Container.MainFooter>
    </Container>
  );
};

export const getServerSideProps = async context => {
  const { challengeId } = context.params;
  const { message, code } = context.query;

  return {
    props: {
      code,
      message,
      challengeId,
    },
  };
};

export default fail;

/* <p>
  결제 실패화면입니다. 결제 실패에 대해 error코드를 화면에 노출합니다.
  자세한 정보는{' '}
  <a href="https://docs.tosspayments.com/reference/error-codes#failurl%EB%A1%9C-%EC%A0%84%EB%8B%AC%EB%90%98%EB%8A%94-%EC%97%90%EB%9F%AC">
    토스 페이먼트 실패 처리
  </a>{' '}
  를 참고해주세요.
</p> */
