// pages/search/search.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    bookList:[],
  },
  searchBook:function(arg){
    var searchPage = this;
    wx.showLoading({
      title: '加载中……',
    })
    wx.request({
      url: getApp().globalData.baseUrl+'/search', //仅为示例，并非真实的接口地址
      data: {
        searchKey: arg.detail.value.trim(),
      },
      success: function (res) {
        wx.hideLoading()
        if(res.data.status == 1){
          console.log(res.data.data)
          searchPage.setData({ bookList: res.data.data })
        }else{
          wx.showToast({
            title: res.data.errorMsg,
          })
        }
      },
      fail:function(res){
        wx.hideLoading()
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})