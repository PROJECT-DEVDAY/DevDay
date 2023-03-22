import React, { useState } from 'react';
import PropTypes from 'prop-types';

import style from './index.module.scss';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import { FiCalendar } from 'react-icons/fi';

export default function DayPicker() {
  const [startDate, setStartDate] = useState(new Date());
  return (
    <label className={style.DateBox}>
      <DatePicker 
      className={style.DateSelect}
      dateFormat="yyyy년 MM월 dd일"
      selected={startDate} 
      onChange={date => setStartDate(date)} 
      />
      <FiCalendar className={style.Icon}/>
      
    </label>
  );
}

DayPicker.propTypes = {
  date : PropTypes.date
};

DayPicker.defaultProps = {
  date : null
};
