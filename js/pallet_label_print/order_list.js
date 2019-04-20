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
        if (vm.checkForm()) {
          vm.fetch(() => {
            vm.$set(vm.data, "isReady", true);
          });
        }
      }
      $(window).resize();
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
      if (this.data.isReady) {
        this.fetch();
        this.$router.push({ name: this.$options.name });
      }
    },
    checkFetchData(successHandler = () => {}) {
      if (this.checkForm()) {
        this.fetch(successHandler);
      } else {
        this.$router.push({ name: this.$options.name });
      }
    },
    checkForm() {
      return _.every(["startDate", "endDate"], (k) => {
        return this.form.data[k];
      });
    },
    item() {
      const item = _.findWhere(this.data.list, { id: this.$route.params.orderId });
      if (item) {
        this.$set(this.data, "item", item);
      } else {
        this.$router.push({ name: this.$options.name });
      }
    },
    select(item) {
      this.$router.push({
        name: "palletLabelList",
        params: { orderId: item.id }
      });
    }
  }
}