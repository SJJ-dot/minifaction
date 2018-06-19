//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    books: []
  },
  onPullDownRefresh: function () {
    wx.stopPullDownRefresh()
    if (this.data.books.length == 0) return

    wx.showNavigationBarLoading()
    var completeCount = 0
    var page = this;
    for (var i = 0; i < page.data.books.length; i++) {
      page.requetDetails(i, function () {
        completeCount++
        if (completeCount == page.data.books.length) {
          wx.hideNavigationBarLoading()
        }
      });
    }
  },

  requetDetails: function (index, complete_) {
    var book = this.data.books[index];
    // wx.showNavigationBarLoading();
    var page = this;
    wx.util.http({
      url: getApp().data.baseUrl + '/intro',
      data: {
        url: book.url
      },
      success: function (res) {
        // wx.hideNavigationBarLoading();
        res.domain = book.domain;
        res.index = book.index;
        // page.setData({ book: res })
        wx.setStorage({
          key: book.url,
          data: res,
          complete: function () {
            var books = page.data.books
            res.key = wx.util.getGBookKey(res)
            
            var newNum = res.chapterList.length - books[index].chapterList.length
            res.newNum = newNum >= 0 ? newNum : 0
            books[index] = res
            books.sort(function (a, b) {
              var aN = a.newNum || 0
              var bN = b.newNum || 0
              return aN > bN ? -1 : aN < bN ? 1 : 0
            })
            page.setData({
              books: books,
            })
            complete_()
          }
        })

      },
      fail: function (res) {
        // wx.hideNavigationBarLoading();
        complete_()
      }
    });
  },

  onShow: function () {
    var page = this;
    wx.getStorageInfo({
      success: function (res) {
        var regStr = "book:.+-author:.+"
        var allBook = [];
        for (var i = 0; i < res.keys.length; i++) {
          if (res.keys[i].match(regStr)) {
            allBook.push(res.keys[i])
          }
        }
        var books = [];
        for (var i = 0; i < allBook.length; i++) {
          var res = wx.getStorageSync(allBook[i]);
          var book = res.books[res.index];
          var detailsB = wx.getStorageSync(book.url)
          detailsB.key = wx.util.getGBookKey(book)
          books.push(detailsB)
        }

        page.setData({
          books: books,
        })
      },
    })
  },
  bindDefaultImage: function (res) {
    var index = res.target.dataset.index
    this.data.books[index].bookCoverImgUrl = "../../images/laokuoteng.jpg"
    this.setData({
      books: this.data.books
    })
  },
  deleteBook: function (arg) {
    var page = this;
    wx.showModal({
      title: '删除选中书籍？',
      content: '点击确定后将会彻底删除选中书籍的所有记录',
      success: function (res) {
        if (res.confirm) {
          var index = arg.currentTarget.dataset.index;
          var target = page.data.books[index]
          var newBooks = page.data.books.splice(index, 1)
          page.setData({
            books: page.data.books
          })
          wx.getStorage({
            key: wx.util.getGBookKey(target),
            success: function (res) {
              wx.removeStorage({
                key: wx.util.getGBookKey(target),
                success: function (res) { },
              })
              var count = 0;
              var allCount = 0;
              for (var i = 0; i < res.data.books.length; i++) {
                var b = res.data.books[i]
                wx.getStorage({
                  key: b.url,
                  success: function (res) {
                    wx.removeStorage({
                      key: res.data.url,
                      success: function (res) { },
                    })
                    allCount += res.data.chapterList.length
                    for (var j = 0; j < res.data.chapterList.length; j++) {
                      wx.removeStorage({
                        key: res.data.chapterList[j].url,
                        success: function (res) { },
                        complete: function () {
                          count++;
                          if (count == allCount) {
                            wx.showToast({
                              title: '删除成功',
                            })
                          }
                        }
                      })
                    }
                  },
                })
              }
            },
          })
        }
      }
    })
  }
})