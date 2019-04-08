import Vue from "../../../node_modules/vue/dist/vue.min.js";
import Toastr from "toastr";
import "style-loader!css-loader!../../../node_modules/toastr/build/toastr.min.css";

export default {
  install(Vue, options) {
    Toastr.options = {
      "timeOut": 2000,
      "positionClass": "toast-bottom-right"
    };
    Vue.$toastr = Toastr;
  }
}