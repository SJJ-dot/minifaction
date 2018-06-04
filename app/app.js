//app.js

App({
  onLaunch: function () {
    wx.util = require("./utils/util.js");
    wx.login({
      success: res => {

        wx.util.http({
          url: 'https://hishen.top/fs/auth',
          data: {
            version: "0.0.10",
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
  data: {}
})