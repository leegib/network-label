import Vue from "../../../node_modules/vue/dist/vue.min.js";

export default {
  methods: {
    renderBoolean(row) {
      return `<i class="${row.value ? 'check' : 'close'} icon"></i>`;
    },
    renderNumber(row) {
      return Vue.filter("commaNumber")(row.value);
    }
  }
}