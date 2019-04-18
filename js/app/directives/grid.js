import Vue from "../../../node_modules/vue/dist/vue.min.js";
import _ from "underscore";
import AgGrid from "../../../node_modules/ag-grid-community/dist/ag-grid-community.js";
import "style-loader!css-loader!../../../node_modules/ag-grid-community/dist/styles/ag-grid.css";
import "style-loader!css-loader!../../../node_modules/ag-grid-community/dist/styles/ag-theme-balham.css";

export default {
  bind(el, binding, vnode) {
    const vm = vnode.context;
    const grid = {
      enableColResize: true,
      enableFilter: true,
      enableSorting: true,
      rowSelection: "single",
      onRowDoubleClicked: () => {
        if (vm.submit) {
          vm.submit();
        }
      },
      onSelectionChanged: (g) => {
        const item = g.api.getSelectedRows()[0];
        vm.$set(vm.data, "item", item);
      },
      onFilterChanged: (g) => {
        vm.$set(vm.data, "rows", _.map(g.api.getRenderedNodes(), (item) => {
          return item.data;
        }));
      },
      localeText: {
        loadingOoo: msg("data_before"),
        noRowsToShow: msg("data_empty")
      }
    };
    $(el).css("flex", 1);
    new AgGrid.Grid(el, $.extend(grid, binding.value));

    vm.$watch("data.list", (val) => {
      grid.api.setRowData(val);
    });
  }
}