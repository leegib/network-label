import Vue from "../../node_modules/vue/dist/vue.min.js";
import _ from "underscore";
import Fetch from "../app/mixins/fetch.js";
import Form from "../app/mixins/form.js";
import Grid from "../app/mixins/grid.js";
import Modal from "../app/mixins/modal.js";
import Select from "../app/mixins/select.js";

export default {
  template: "#pallet-label-add",
  mixins: [Fetch, Form, Grid, Modal, Select],
  data() {
    return {
      data: { order: {} }
    };
  },
  methods: {
    _fetch(successHandler = () => {}) {
      this.$http.get(
        jsRoutes.controllers.PalletLabel.boxLabelList(this.data.order.id).url
      ).then((success) => {
        _.each(success.data, (item) => {
          this.parseDateItem(item, ["productionDate"]);
          item.isSelect = false;
        });
        this.$set(this.data, "list", success.data);
        this.fetchPair();
        successHandler();
      }, (error) => {
        this.formError(error.data);
      });
    },
    fetchPair() {
      this.$set(this.$refs.org.data, "list", this.selectList(false));
      this.$set(this.$refs.slt.data, "list", this.selectList(true));
    },
    productQuantity() {
      return _.reduce(this.selectList(true), (sum, item) => {
        return sum + item.quantity;
      }, 0);
    },
    selectAllFetch(isSelect) {
      this.selectAll(isSelect);
      this.fetchPair();
    },
    show() {
      this.modalShow();
      this.$set(this.data, "isLoading", false);
      this.$set(this.data, "order", $.extend({}, this.$parent.$parent.data.item));
      this.fetch();
    },
    submit() {
      this.$set(this.data, "isLoading", true);

      this.$http.post(
        jsRoutes.controllers.PalletLabel.palletLabelAdd(this.data.order.id).url,
        { boxLabel: this.selectList(true) }
      ).then((success) => {
        this.modalClose();
        Vue.$toastr.success(msg("adit_success"));
        this.$parent.fetch();
        this.$parent.print(success.data);
      }, (error) => {
        this.formError(error.data);
      }).finally(() => {
        this.$set(this.data, "isLoading", false);
      });
    }
  },
  components: {
    org: {
      template: "#pallet-label-add-org",
      mixins: [Grid],
      data() {
        return { data: {} };
      },
      methods: {
        select(item) {
          item.isSelect = true;
          this.$parent.fetchPair();
        }
      }
    },
    slt: {
      template: "#pallet-label-add-slt",
      mixins: [Grid],
      data() {
        return { data: {} };
      },
      methods: {
        select(item) {
          item.isSelect = false;
          this.$parent.fetchPair();
        }
      }
    }
  }
}