import Moment from "moment";

// 숫자 형식 3자리 쉼표 표기
export const CommaNumber = {
  install(Vue) {
    Vue.filter("commaNumber", (val) => {
      if (val) {
        return Number(val).toLocaleString();
      } else {
        return val;
      }
    })
  }
};

// 날짜 형식 표기
export const Date = {
  install(Vue) {
    Vue.filter("date", (val, format) => {
      return Moment(val).format(format ? format : "YYYY-MM-DD");
    });
  }
};