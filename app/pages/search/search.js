// pages/search/search.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    inputShowed: false,
    inputVal: "",
    bookList: [{ "name": "极道天魔", "author": "滚开", "books": [{ "url": "https://www.aszw.org/book/168/168625/", "name": "极道天魔", "author": "滚开", "bookCoverImgUrl": "", "intro": "", "chapterList": [] }, { "url": "https://www.biquge5200.cc/58_58206/", "name": "极道天魔", "author": "滚开", "bookCoverImgUrl": "", "intro": "", "chapterList": [] }, { "url": "https://www.dhzw.org/book/165/165908/", "name": "极道天魔", "author": "滚开", "bookCoverImgUrl": "", "intro": "", "chapterList": [] }, { "url": "http://www.yunlaige.com/book/19984.html", "name": "极道天魔", "author": "滚开", "bookCoverImgUrl": "", "intro": "", "chapterList": [] }] }],
  },
  searchBook:function(arg){
    var searchPage = this;
    wx.showToast({
      title: '数据加载中',
      icon: 'loading',
      duration: 3000
    });
    wx.request({
      url: getApp().data.baseUrl+'/search', //仅为示例，并非真实的接口地址
      data: {
        searchKey: arg.detail.value.trim(),
      },
      success: function (res) {
        if(res.data.status == 1){
          console.log(res.data.data)
          searchPage.setData({ bookList: res.data.data })
        }else{
          wx.showToast({
            title: res.data.errorMsg,
            icon: 'success',
            duration: 1000
          });
        }
      }
    })
  },
  showInput: function () {
    this.setData({
      inputShowed: true
    });
  },
  hideInput: function () {
    this.setData({
      inputVal: "",
      inputShowed: false
    });
  },
  clearInput: function () {
    this.setData({
      inputVal: ""
    });
  },
  inputTyping: function (e) {
    this.setData({
      inputVal: e.detail.value
    });
  }
})