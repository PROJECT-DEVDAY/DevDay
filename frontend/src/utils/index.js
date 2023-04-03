export const getStartWithEndDate = (startDate, endDate) => {
  const start = new Date(startDate);
  const end = new Date(endDate);

  const startMonth = `${start.getMonth()}`.padStart(2, '0');
  const startDay = `${start.getDate()}`.padStart(2, '0');

  const endMonth = `${end.getMonth()}`.padStart(2, '0');
  const endDay = `${end.getDate()}`.padStart(2, '0');

  return `${startMonth}.${startDay}~${endMonth}.${endDay}`;
};
