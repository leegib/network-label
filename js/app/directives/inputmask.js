import "../../../node_modules/jquery.inputmask/dist/jquery.inputmask.bundle.js";
import {mkObj} from "../../app/util.js";

export default {
  bind(el, binding, vnode) {
    const vm = vnode.context;
    const params = binding.value;
    const newKey = params.key ? params.key.replace(".", "_") : "";

    const objArr = (objStr) => {
      return objStr.split(".");
    };

    const options = {
      oncomplete(event) {
        const val = event.target.value;

        if (params.onChange) {
          params.onChange(val);
        }

        if (params.key && params.obj) {
          const obj = mkObj(vm, objArr(params.obj));
          vm.$set(obj, params.key, val);
          if (params.key != newKey) {
            vm.$set(obj, newKey, val);
          }
        }
      }
    };

    if (params.key && params.obj) {
      options.oncleared = () => {
        vm.$set(mkObj(vm, objArr(params.obj)), params.key, null);
      }
    }

    if (params.type == "numeric") {
      params.options = $.extend({
        alias: "numeric",
        allowMinus: false,
        autoGroup: true,
        autoUnmask: true,
        groupSeparator: ","
      }, params.options);
    }

    $(el).inputmask($.extend(options, params.options));

    if (params.val) {
      $(el).inputmask("setvalue", params.val);
    }

    if (params.key && params.obj) {
      vm.$watch(`${params.obj}.${newKey}`, (val) => {
        if (!val) {
          $(el).inputmask("setvalue", null);
        }
      });
    }
  }
}