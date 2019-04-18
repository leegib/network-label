// 점으로 구분된 객체 문자열을 객체로 변환
export function mkObj(obj, key, index = 0) {
  if (obj[key[index]]) {
    return mkObj(obj[key[index]], key, index + 1);
  } else {
    return obj;
  }
};