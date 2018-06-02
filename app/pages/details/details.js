// pages/details/details.js
var regStr = "(http[s]?://([a-zA-Z\\d]+\\.)+[a-zA-Z\\d]+)/?"
Page({

  /**
   * 页面的初始数据
   */
  data: {
    book:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var page = this;
     wx.getStorage({
      key: options.key,
      success: function(res) {
        var gbook = res.data.books[res.data.index];
        page.setData({
          book:{
            name:gbook.name,
            author:gbook.author,
            domain:gbook.url.match(regStr)[1]
          }
        })
        page.requetDetails(res.data.books[res.data.index])
      },
    })
  },
  requetDetails: function (res){
    var domain = res.url.match(regStr)[1];
    wx.showNavigationBarLoading();
    var bookKey = res.url;
    var page = this;
  
   wx.util.http({
      url: getApp().data.baseUrl + '/intro',
      data: {
        url: res.url
      },
      success: function (res) {
        wx.hideNavigationBarLoading();
          res.domain = domain;
          page.setData({ book: res })
          console.log(res)
          wx.setStorage({
            key: bookKey,
            data: res,
          })
        
      },
      fail: function (res) {
        wx.hideNavigationBarLoading();
        page.showLocalData(bookKey, res);
      }
    });
  },
  showLocalData:function(key,error){
    var page = this;
    wx.getStorage({
      key: key,
      success: function (res) {
        page.setData({ book: res })
      },
      fail: function () {
        wx.showToast({
          title: error,
          icon: 'none',
          duration: 1000
        });
      },
    })
  },
  bindDefaultImage:function(){
    this.data.book.bookCoverImgUrl = "../../images/laokuoteng.jpg"
    this.setData({book:this.data.book})
  },
  selectOrigin: function () {
    var list = [];
    this.data.book.
    wx.showActionSheet({
      itemList: ['item1', 'item2', 'item3', 'item4'],
      success: function (e) {
        console.log(e.tapIndex)
      }
    })
  }
})