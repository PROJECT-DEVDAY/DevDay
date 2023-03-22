import React,{ useState } from 'react';
import PropTypes from 'prop-types';

import style from './index.module.scss';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import { FiCalendar } from 'react-icons/fi';
export default function MonthPicker() {
  const [startDate, setStartDate] = useState(new Date());
  return (
    <label className={style.DateBox}>
      <DatePicker
      className={style.SelectMonth}
      dateFormat="yyyy년 MM월"
      showMonthYearPicker 
      selected={startDate} 
      onChange={date => setStartDate(date)} 
      />
      <FiCalendar className={style.Icon}/>
      
    </label>
  );
}

MonthPicker.propTypes = {
  date : PropTypes.date
};

MonthPicker.defaultProps = {
  date : null
};
