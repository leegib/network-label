export default {
  data() {
    return {
      data: { isInit: false }
    };
  },
  methods: {
    modalClose() {
      $(this.$el).modal("hide");
    },
    modalShow() {
      if (this.data.isInit) {
        $(this.$el).modal("show");
      } else {
        $(this.$el).modal({
          autofocus: false,
          closable : false
        }).modal("show");
        this.$set(this.data, "isInit", true);
      }
    }
  }
}