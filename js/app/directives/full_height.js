import _ from "underscore";

export default {
  bind(el) {
    $(window).on("resize", _.throttle(() => {
      $(el).css("height", `${$(window).height() - $(el).offset().top}px`);
    }, 100));
  }
};