// pages/details/details.js
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
        page.requetDetails(res.data.books[res.data.index])
      },
    })
  },
  requetDetails: function (res){
    var reg = res.url.match("(http[s]?://([a-zA-Z\\d]+\\.)+[a-zA-Z\\d]+)/?")
    var domain = reg[1];
    wx.showToast({
      title: '数据加载中',
      icon: 'loading',
      duration: 3000
    });
    var bookKey = res.url;
    var page = this;
    wx.request({
      url: getApp().data.baseUrl + '/intro',
      data:{
        url:res.url
      },
      success:function(res){
        if(res.data.status == 1){
          res.data.data.domain=domain;
          page.setData({book:res.data.data})
          console.log(res.data.data)
          wx.setStorage({
            key: bookKey,
            data: res.data.data,
          })
        }else{
          page.showLocalData(bookKey,res.data.errorMsg);
        }
      },
      fail:function(res){
        page.showLocalData(bookKey, res.errorMsg);
      }
    })
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
          duration: 2000
        });
      },
    })
  },
  bindDefaultImage:function(){
    this.data.book.bookCoverImgUrl = "../../images/laokuoteng.jpg"
    this.setData({book:this.data.book})
  }
})