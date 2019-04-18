import Vue from "../node_modules/vue/dist/vue.min.js";
import VueResource from "vue-resource";
import VueRouter from "vue-router";

import Toastr from "./app/components/toastr.js";

import Datepicker from "./app/directives/datepicker.js";
import Dropdown from "./app/directives/dropdown.js";
import FullHeight from "./app/directives/full_height.js";
import Grid from "./app/directives/grid.js";

import {CommaNumber, Date} from "./app/filters.js";

import OrderList from "./pallet_label_print/order_list.js";

Vue.use(VueResource);
Vue.use(VueRouter);
Vue.use(Toastr);
Vue.use(CommaNumber);
Vue.use(Date);

Vue.directive("datepicker", Datepicker);
Vue.directive("dropdown", Dropdown);
Vue.directive("full-height", FullHeight);
Vue.directive("grid", Grid);

new Vue({
  router: new VueRouter({
    mode: "history",
    routes: [{
      name: "orderList",
      path: "/",
      component: OrderList
    }]
  })
}).$mount("#app");