//app.js

App({
  onLaunch: function () {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
       console.log(res)
       wx.request({
         url: 'http://118.24.120.20/fictionService/auth',
         data: {
           code: res.code,
         },
         success: function (res) {
           getApp().globalData.baseUrl = "http://118.24.120.20/fictionService/";
         }
       })
      }
    })
  },
  globalData: {
    userInfo: {
     
    }
  }
})