import { isInteger } from 'lodash';

const DAY = ['일', '월', '화', '수', '목', '금', '토'];

export const getStartWithEndDate = (startDate, endDate) => {
  const start = new Date(startDate);
  const end = new Date(endDate);
  const startMonth = `${start.getMonth() + 1}`.padStart(2, '0');
  const startDay = `${start.getDate()}`.padStart(2, '0');

  const endMonth = `${end.getMonth() + 1}`.padStart(2, '0');
  const endDay = `${end.getDate()}`.padStart(2, '0');

  return `${startMonth}.${startDay}(${
    DAY[start.getDay()]
  })~${endMonth}.${endDay}(${DAY[end.getDay()]})`;
};
export const getDateDiff = (d1, d2) => {
  const date1 = new Date(d1);
  const date2 = new Date(d2);

  const diffDate = date1.getTime() - date2.getTime();

  return Math.abs(diffDate / (1000 * 60 * 60 * 24)); // 밀리세컨 * 초 * 분 * 시 = 일
};

export const getWeekDiff = (d1, d2) => {
  const date1 = new Date(d1);
  const date2 = new Date(d2);

  const diffDate = date1.getTime() - date2.getTime();

  const day = Math.abs(diffDate / (1000 * 60 * 60 * 24)) + 1; // 밀리세컨 * 초 * 분 * 시 = 일
  const week = day / 7;
  if (isInteger(week)) {
    return {
      week,
      day: 0,
    };
  }

  return {
    week: Math.floor(week),
    day: day % 7,
  };
};

export const getDatesStartToLast = (startDate, lastDate) => {
  const regex = /^\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$/;
  if (!(regex.test(startDate) && regex.test(lastDate)))
    return 'Not Date Format';
  const result = [];
  const curDate = new Date(startDate);
  while (curDate <= new Date(lastDate)) {
    result.push(curDate.toISOString().split('T')[0]);
    curDate.setDate(curDate.getDate() + 1);
  }
  return result;
};
