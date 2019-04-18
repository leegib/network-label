import Vue from "../../../node_modules/vue/dist/vue.min.js";
import _ from "underscore";

export default {
  data() {
    return {
      data: {
        limit: 500
      },
      fetch: _.debounce(this._fetch, 100)
    }
  },
  methods: {
    // 객체 내부의 객체를 상단으로 올림
    objectFlatten(target) {
      const _objectFlatten = (target, key = "", obj = {}) => {
        if (Array.isArray(target)) {
          obj[key] = _.map(target, (item, index) => {
            return _objectFlatten(item);
          });
          return obj[key];
        } else if (typeof target === "object") {
          _.each(target, (v, k) => {
            const upKey = key ? `${key}.${k}` : k;
            _objectFlatten(target[k], upKey, obj);
          });
          return obj;
        } else {
          obj[key] = target;
        }
      };
      return _objectFlatten(target);
    },
    // 문자열 형태의 숫자 정렬
    orderList(list, targetList = [], handler = () => {}) {
      // 숫자의 최대값 구함
      _.each(targetList, (target, index) => {
        targetList[index] = {
          name: target,
          max: _.max(_.map(list, (item) => {
            return item[target] ? item[target].split("-").reverse()[0].length : 0;
          }))
        };
      });

      // 숫자의 최대값에 맞추어 0 입력
      _.each(list, (item) => {
        handler(item);
        _.each(targetList, (target) => {
          if (item[target.name]) {
            let targetArr = item[target.name].split("-").reverse();
            const length = targetArr[0].length;
            for (let i = 0; i < target.max - length; i++) {
              targetArr[0] = `0${targetArr[0]}`;
            }
            item[`${target.name}Order`] = _.reduce(targetArr.reverse(), (head, tail) => {
              return `${head}${tail}`;
            });
          }
        });
      });
      return list;
    },
    // 소수점 자리수 표기
    parseFloatItem(item, targetList = [], point = 2) {
      _.each(targetList, (target) => {
        const parseVal = parseFloat(item[target]);
        item[`${target}Val`] = parseVal ? parseVal.toFixed(point) : "";
      });
    },
    // 날짜 형식 지정
    parseDateItem(item, targetList = []) {
      _.each(targetList, (target) => {
        item[target] = Vue.filter("date")(item[target]);
      });
      return item;
    }
  }
}