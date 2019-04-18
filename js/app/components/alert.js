import "jquery-confirm";
import "style-loader!css-loader!../../../node_modules/jquery-confirm/dist/jquery-confirm.min.css";

export default {
  install(vue, options) {
    vue.$alert = {
      confirm(confirmMsg, handler) {
        $.confirm({
          title: false,
          content: `<div class="ui small header">${confirmMsg}</div>`,
          useBootstrap: false,
          animateFromElement: false,
          boxWidth: "400px",
          buttons: {
            confirm: {
              text: "확인",
              btnClass: "btn-blue",
              action: handler
            },
            cancel: {
             text: "취소"
            }
          }
        });
      }
    };
  }
}