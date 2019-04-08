import Vue from "../node_modules/vue/dist/vue.min.js";
import Dropdown from "./app/directives/dropdown.js";

Vue.directive("dropdown", Dropdown);

new Vue({
  el: "#app"
});