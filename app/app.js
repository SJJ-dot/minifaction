//app.js

App({
  onLaunch: function () {
    // 登录
    wx.login({
      success: res => {
        wx.request({
          url: 'https://hishen.top/fs/auth',
          data: {
            code: res.code,
          },
          success: function (res) {
            if (res.statusCode == 200) {
              var authRes = res.data.status == 1 && res.data.data;
              wx.setStorage({
                key: "auth",
                data: authRes
              })
              if (authRes)
                getApp().data.baseUrl = "https://hishen.top/fs/";
            } else {
              if (wx.getStorageSync("auth"))
                getApp().data.baseUrl = "https://hishen.top/fs/";
            }
          },
          fail:function(){
            if (wx.getStorageSync("auth"))
              getApp().data.baseUrl = "https://hishen.top/fs/";
          }
        })
      }
    })
  },
  data: {}
})