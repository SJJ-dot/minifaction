//app.js

App({
  onLaunch: function () {
    wx.util = require("./utils/util.js");
    wx.login({
      success: res => {

        wx.util.http({
          url: 'https://hishen.top/fs/auth',
          data: {
            code: res.code,
          },
          success: function (res) {
            wx.setStorage({
              key: "auth",
              data: true
            })
            getApp().data.baseUrl = "https://hishen.top/fs/";
          },
          reject: function (res) {
            wx.setStorageSync("auth", false)
          },
          fail: function () {
            if (wx.getStorageSync("auth"))
              getApp().data.baseUrl = "https://hishen.top/fs/";
          }
        })
      }
    })
  },
  data: {},
  http: function (obj) {
    wx.request({
      url: obj.url,
      data: obj.data,
      success: function (res) {
        console.log(res.data.status)
        console.log(res.statusCode)
        if (res.statusCode == 200 && res.data.status == 1) {
          obj.success(res.data.data);
        } else {
          var errMsg = res.data.status + "-" + res.data.error;
          if (reject in obj)
            obj.reject(errMsg);
          if (fail in obj)
            obj.fail(errMsg);
          wx.showToast({
            title: errMsg,
            icon: 'none',
            duration: 2000
          });
        }
      },
      fail: function (res) {
        console.log(res)
        console.log(res.errMsg)
        if (fail in obj)
          obj.fail(res.errMsg)
        wx.showToast({
          title: res.errMsg,
          icon: 'none',
          duration: 2000
        });
      }
    })
  }
})