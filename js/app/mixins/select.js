import _ from "underscore";

export default {
  methods: {
    selectAll(isSelect) {
      _.each(this.data.list, (item) => {
        item.isSelect = isSelect;
      });
    },
    selected() {
      return _.findWhere(this.data.list, { isSelect: true });
    },
    selectList(isSelect) {
      return _.filter(this.data.list, (item) => {
        return item.isSelect == isSelect;
      });
    }
  }
}