import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import { FiCalendar } from 'react-icons/fi';

import PropTypes from 'prop-types';

import style from './index.module.scss';

import 'react-datepicker/dist/react-datepicker.css';

export default function MonthPicker() {
  const [startDate, setStartDate] = useState(new Date());
  return (
    <label className={style.DateBox} htmlFor="month-picker">
      <DatePicker
        className={style.SelectMonth}
        dateFormat="yyyy년 MM월"
        showMonthYearPicker
        selected={startDate}
        onChange={date => setStartDate(date)}
      />
      <FiCalendar className={style.Icon} />
    </label>
  );
}
