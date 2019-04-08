import Vue from "../node_modules/vue/dist/vue.min.js";
import VueResource from "vue-resource";
import Toastr from "./app/components/toastr.js";
import Dropdown from "./app/directives/dropdown.js";
import Form from "./app/mixins/form.js";

Vue.use(VueResource);
Vue.use(Toastr);
Vue.directive("dropdown", Dropdown);

new Vue({
  el: "#app",
  mixins: [Form],
  data() {
    return { data: {} };
  },
  created() {
    this.$http.get(
      jsRoutes.controllers.Index.networkList().url
    ).then((success) => {
      this.$set(this.data, "networkList", success.data);
    }, (error) => {
      this.formError(error.data);
    });
  },
  methods: {
    submit() {
      this.$set(this.form, "error", {});

      this.$http.post(
        jsRoutes.controllers.Index.login().url,
        this.form.data
      ).then((success) => {
        location.assign(success.data);
      }, (error) => {
        this.formError(error.data);
      });
    }
  }
});