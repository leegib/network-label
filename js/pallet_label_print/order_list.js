import _ from "underscore";
import Moment from "moment";
import Fetch from "../app/mixins/fetch.js";
import Form from "../app/mixins/form.js";
import Grid from "../app/mixins/grid.js";

export default {
  name: "orderList",
  template: "#order-list",
  mixins: [Fetch, Form, Grid],
  data() {
    return {
      storage: sessionStorage,
      moment: Moment,
      data: { item: {} }
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (to.name == vm.$options.name) {
        vm.checkFetch();
        $(window).resize();
      }
    });
  },
  methods: {
    _fetch(successHandler = () => {}) {
      this.$http.post(
        jsRoutes.controllers.PalletLabel.orderList(this.data.searchWord).url,
        this.form.data
      ).then((success) => {
        _.each(success.data, (item) => {
          this.parseDateItem(item, ["orderDate", "dueDate"]);
        });
        this.$set(this.data, "list", success.data);
        successHandler();
      }, (error) => {
        this.formError(error.data);
      });
    },
    checkFetch() {
      if (this.checkForm()) {
        this.fetch();
      }
    },
    checkForm() {
      return _.every(["startDate", "endDate"], (k) => {
        return this.form.data[k];
      });
    }
  }
}