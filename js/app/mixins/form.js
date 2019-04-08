import Vue from "../../../node_modules/vue/dist/vue.min.js";
import _ from "underscore";

export default {
  data() {
    return {
      form: {
        data: {},
        error: {},
        key: {}
      }
    };
  },
  methods: {
    formData(form, key, item, newKey) {
      _.each(item, (v, k) => {
        const formKey = key[k];
        if (formKey) {
          form[newKey ? newKey(formKey) : formKey] = v;
        }
      });
      return form;
    },
    formError(item) {
      let isFirst = true;
      _.each(item, (v, k) => {
        if (isFirst) {
          _.each(v, (msg) => {
            Vue.$toastr.error(msg);
          });
          isFirst = false;
        }
        this.$set(this.form.error, k, v);
      });
    }
  }
}