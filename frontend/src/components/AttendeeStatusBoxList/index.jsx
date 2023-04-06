import AttendeeStatusBox from '../AttendeeStatusBox';

const AttendeeStatusBoxList = ({ list = [] }) => {
  return list.map((item, index) => {
    return (
      <AttendeeStatusBox
        className="mb-4"
        key={index}
        name={item.name}
        date={item.date}
        check={item.check}
      />
    );
  });
};

export default AttendeeStatusBoxList;
