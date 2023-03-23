import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import { FiCalendar } from 'react-icons/fi';

import PropTypes from 'prop-types';

import style from './index.module.scss';

import 'react-datepicker/dist/react-datepicker.css';

export const DayPicker = () => {
  const [startDate, setStartDate] = useState(new Date());
  return (
    <label className={style.DateBox} htmlFor="dayPicker">
      <DatePicker
        className={style.DateSelect}
        dateFormat="yyyy년 MM월 dd일"
        selected={startDate}
        onChange={date => setStartDate(date)}
      />
      <FiCalendar className={style.Icon} />
    </label>
  );
};
