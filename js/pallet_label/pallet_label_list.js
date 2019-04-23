import _ from "underscore";
import Fetch from "../app/mixins/fetch.js";
import Grid from "../app/mixins/grid.js";
import PalletLabelAdd from "./pallet_label_add.js";

export default {
  name: "palletLabelList",
  template: "#pallet-label-list",
  mixins: [Fetch, Grid],
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (to.name == vm.$options.name) {
        if (from.name) {
          vm.$parent.item(to.params.orderId);
          vm.fetch();
        } else {
          vm.refresh(to.params.orderId, () => {
            vm.$set(vm.$parent.data, "isReady", true);
          });
        }
      }
    });
  },
  beforeRouteUpdate(to, from, next) {
    if (to.name == this.$options.name) {
      this.$parent.item(to.params.orderId);
      this.fetch();
    }
    next();
  },
  methods: {
    _fetch(successHandler = () => {}) {
      this.$http.get(
        jsRoutes.controllers.PalletLabel.palletLabelList(this.$parent.data.item.id).url
      ).then((success) => {
        _.each(success.data, (item) => {
          this.parseDateItem(item, ["dispatchDate"]);
        });
        this.$set(this.data, "list", success.data);
        successHandler();
      }, (error) => {
        this.formError(error.data);
      });
    },
    dispatchQuantity() {
      return _.reduce(this.data.list, (sum, item) => {
        return sum + item.quantity;
      }, 0);
    },
    refresh(orderId, successHandler = () => {}) {
      this.$parent.checkFetchData(() => {
        this.$parent.item(orderId);
        this.fetch(successHandler);
      });
    }
  },
  components: {
    PalletLabelAdd
  }
}