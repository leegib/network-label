import "air-datepicker";
import "../../../node_modules/jquery.inputmask/dist/jquery.inputmask.bundle.js";
import "style-loader!css-loader!../../../node_modules/air-datepicker/dist/css/datepicker.min.css";

import {mkObj} from "../../app/util.js";

export default {
  bind(el, binding, vnode) {
    (function ($) {
      $.fn.datepicker.language['kr'] = {
        days: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
        daysShort: ['일', '월', '화', '수', '목', '금', '토'],
        daysMin: ['일', '월', '화', '수', '목', '금', '토'],
        months: ['1월','2월','3월','4월','5월','6월', '7월','8월','9월','10월','11월','12월'],
        monthsShort: ['01','02','03','04','05','06','07','08','09','10','11','12'],
        today: '오늘',
        clear: '리셋',
        dateFormat: 'yyyy-mm-dd',
        timeFormat: 'hh:ii aa',
        firstDay: 0
      };
    })(jQuery);

    const vm = vnode.context;
    const params = binding.value;
    const newKey = params.key ? params.key.replace(".", "_") : "";

    const defaultOptions = {
      autoClose: true,
      language : "kr",
      navTitles: {
        days  : 'yyyy - mm',
        months: 'yyyy',
        years : 'yyyy1 - yyyy2'
      },
      toggleSelected: false,
      onSelect(val) {
        if (params.onChange) {
          params.onChange(val);
        }

        if (params.key && params.obj) {
          const obj = mkObj(vm, params.obj.split("."));

          vm.$set(obj, params.key, val);
          if (params.key != newKey) {
            vm.$set(obj, newKey, val);
          }
        }

        if (params.id && params.storage) {
          params.storage.setItem(params.id, val);
        }

        if (params.maxDateKey) {
          const $input = $(vm.$refs[params.maxDateKey]);
          const datepicker = $input.datepicker().data('datepicker');
          if (datepicker) {
            datepicker.update("minDate", new Date(val));
            if (val > $input.val() && val) {
              datepicker.selectDate(new Date(val));
            }
          }
        }

        if (params.minDateKey) {
          const $input = $(vm.$refs[params.minDateKey]);
          const datepicker = $input.datepicker().data('datepicker');
          if (datepicker) {
            datepicker.update("maxDate", new Date(val));
            if (val < $input.val() && val) {
              datepicker.selectDate(new Date(val));
            }
          }
        }
      }
    };

    const datepicker = $(el).datepicker($.extend(defaultOptions, params.options)).data("datepicker");
    $("DIV.datepicker").css("z-index", 10000);

    $(el).inputmask($.extend({
      oncomplete(event) {
        const val = event.target.value;
        if (val) {
          datepicker.selectDate(new Date(val));
        }
      }
    }, params.mask));

    if (params.val) {
      datepicker.selectDate(new Date(params.val));
    }

    if (params.key && params.obj) {
      vm.$watch(`${params.obj}.${newKey}`, (val) => {
        if (val) {
          if (val != el.value) {
            datepicker.selectDate(new Date(val));
          }
        } else {
          datepicker.clear();
        }
      });
    }
  }
};