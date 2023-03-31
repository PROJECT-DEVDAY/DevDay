import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import { useDispatch } from 'react-redux';

import PropTypes from 'prop-types';

import style from './index.module.scss';

import { setChangeDate } from '@/store/challengeCreate/challengeCreateSlice';
import 'react-datepicker/dist/react-datepicker.css';

export const DayPicker = ({ day, showDatePicker }) => {
  const dispatch = useDispatch();
  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState(new Date());
  const onChange = dates => {
    const [start, end] = dates;
    setStartDate(start);
    setEndDate(end);
    dispatch(setChangeDate({ startDate: start, endDate: end }));
    if (start && end) {
      showDatePicker();
    }
  };
  return (
    <>
      {day && (
        <DatePicker
          className={style.DateSelect}
          dateFormat="yy. MM. dd"
          selected={startDate}
          onChange={onChange}
          startDate={startDate}
          endDate={endDate}
          selectsRange
          inline
        />
      )}
      {!day && (
        <DatePicker
          className={style.DateSelect}
          dateFormat="yy. MM. dd"
          selected={startDate}
          onChange={date => setStartDate(date)}
        />
      )}
    </>
  );
};

DayPicker.propTypes = {
  day: PropTypes.bool,
};

DayPicker.defaultProps = {
  day: true,
};
