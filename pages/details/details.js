// pages/details/details.js

Page({

  /**
   * 页面的初始数据
   */
  data: {
    scrollHeight: 200,
    book: {
      index: 0,
    },
    isShowChapter: 0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var page = this;

    wx.createSelectorQuery().select('#detailsHead').boundingClientRect(function (rect) {
      page.setData({ scrollHeight: wx.getSystemInfoSync().windowHeight - rect.height, });
    }).exec()

    wx.getStorage({
      key: options.key,
      success: function (res) {
        var gbook = res.data.books[res.data.index];
        gbook.domain = wx.util.getDomain(gbook.url);
        gbook.index = page.data.book.index;
        wx.getStorage({
          key: gbook.url,
          success: function (res) {
            page.setData({
              book: res.data
            })
          },
          fail: function () {
            page.setData({
              book: gbook
            })
          }
        })
        page.requetDetails(gbook)
      },
    })
  },
  requetDetails: function (res) {
    var domain = res.domain;
    wx.showNavigationBarLoading();
    var url = res.url;
    var page = this;
    wx.util.http({
      url: getApp().data.baseUrl + '/intro',
      data: {
        url: res.url
      },
      success: function (res) {
        wx.hideNavigationBarLoading();
        res.domain = domain;
        res.index = page.data.book.index;
        page.setData({ book: res })
        wx.setStorage({
          key: url,
          data: res,
        })

      },
      fail: function (res) {
        wx.hideNavigationBarLoading();
      }
    });
  },
  bindDefaultImage: function () {
    this.data.book.bookCoverImgUrl = "../../images/laokuoteng.jpg"
    this.setData({ book: this.data.book })
  },
  selectOrigin: function () {
    var page = this;
    var key = wx.util.getGBookKey(page.data.book);
    wx.getStorage({
      key: key,
      success: function (res) {
        var list = [];
        for (var i = 0; i < res.data.books.length; i++) {
          list.push(wx.util.getDomain(res.data.books[i].url))
        }
        wx.showActionSheet({
          itemList: list,
          success: function (select) {
            res.data.index = select.tapIndex;
            wx.setStorage({
              key: key,
              data: res.data,
              success: function () {
                wx.redirectTo({
                  url: './details?key=' + key
                })
              }
            })
          },
        })
      },
    });

  },
  chapterList: function () {
    var page = this;
    var num = page.data.isShowChapter + 1
    num = num > 2 ? 0 : num
    this.setData({
      isShowChapter: num
    })
  },
  startRead: function () {
    var page = this;
    wx.navigateTo({
      url: '../read/read?url=' + page.data.book.url,
    })
  }
})