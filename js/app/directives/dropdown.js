export default {
  bind(el, binding, vnode) {
    const params = binding.value;
    if (params) {
      $(el).dropdown({
        forceSelection: false,
        onChange(value) {
          vnode.context.$set(params.obj, params.key, value);
        }
      });

      const key = params.key.replace(".", "_");
      vnode.context.$watch(`data.${key}`, (val) => {
        $(el).dropdown("set selected", val);
      });

      if (params.val) {
        vnode.context.$set(vnode.context.data, key, params.val);
      }
    } else {
      $(el).dropdown();
    }
  }
}