//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    books: [{}]
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
         var res= wx.getStorageSync(allBook[i]);
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
    this.setData({ books: this.data.books })
  },
})
