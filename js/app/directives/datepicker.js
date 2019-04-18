import "air-datepicker";
import "../../../node_modules/jquery.inputmask/dist/jquery.inputmask.bundle.js";
import "style-loader!css-loader!../../../node_modules/air-datepicker/dist/css/datepicker.min.css";

import {mkObj} from "../../app/util.js";

export default {
  bind(el, binding, vnode) {
    (function ($) {
      const days = [
        msg("day.sun"), msg("day.mon"), msg("day.tue"), msg("day.wed"), msg("day.thu"),
        msg("day.fri"), msg("day.sat")
      ];

      $.fn.datepicker.language['kr'] = {
        days: [
          msg("day.sun_day"), msg("day.mon_day"), msg("day.tue_day"), msg("day.wed_day"), msg("day.thu_day"),
          msg("day.fri_day"), msg("day.sat_day")
        ],
        daysShort: days,
        daysMin: days,
        months: [
          msg("months", 1), msg("months", 2), msg("months", 3), msg("months", 4), msg("months", 5),
          msg("months", 6), msg("months", 7), msg("months", 8), msg("months", 9), msg("months", 10),
          msg("months", 11), msg("months", 12)
        ],
        monthsShort: ['01','02','03','04','05','06','07','08','09','10','11','12'],
        today: msg("today"),
        clear: msg("clear"),
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