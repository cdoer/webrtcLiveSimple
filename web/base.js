var webos = window.webos = window.webos || {};
webos = $.extend(webos, {
    request: function (params) {
        var _this = this;
        params = params || {};
        var realParams = $.extend({
            cache: false,
            dataType: "json",
            type: "POST",
            error: function (xhr) {
                _this.error("数据请求失败!");
            }
        }, params);
        realParams.url = webos.BASEPATH + realParams.url;
        $.ajax(realParams);
    },
    register: function (moduleName, callback) {
        this.fnc[moduleName] = callback;
    },
    execute: function (moduleName, params) {
        if (this.fnc[moduleName]) {
            this.fnc[moduleName](params);
        }
    },
    fnc: {}
})
